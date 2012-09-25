package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topicsearch;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTBugzillaBugCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.SearchResultsAndTopicViewEvent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.EditableView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicBugsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRevisionsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicTagsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicXMLPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.ui.SplitType;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview.assignedtags.TopicTagViewCategoryEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview.assignedtags.TopicTagViewProjectEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview.assignedtags.TopicTagViewTagEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.SearchUICategory;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.SearchUIProject;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.SearchUIProjects;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HanldedSplitLayoutPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.HasData;

/**
 * This presenter is used to display and wire selection of views, including the topic search results view, and the topic XML,
 * details, tags and XML Errors views.
 * 
 * @author Matthew Casperson
 */
@Dependent
public class SearchResultsAndTopicPresenter extends TemplatePresenter implements EditableView {

    public static final String HISTORY_TOKEN = "SearchResultsAndTopicView";
    
    public interface Display extends BaseTemplateViewInterface {
        SplitType getSplitType();

        SimpleLayoutPanel getTopicResultsPanel();

        SimpleLayoutPanel getTopicViewPanel();

        SimpleLayoutPanel getTopicViewActionButtonsPanel();

        SimpleLayoutPanel getTopicResultsActionButtonsPanel();

        HanldedSplitLayoutPanel getSplitPanel();

        DockLayoutPanel getTopicViewLayoutPanel();

        void initialize(final SplitType splitType, final Panel panel);
    }

    /**
     * How long to wait before refreshing the rendered view (in milliseconds).
     */
    private static final int REFRESH_RATE = 1000;

    /** The history token that identifies the a horizontal rendered view split. */
    private static final String SPLIT_TOKEN_HORIZONTAL = "split=h;";

    /** The history token that identifies the a horizontal rendered view split. */
    private static final String SPLIT_TOKEN_VERTICAL = "split=v;";

    /** Setup automatic flushing and rendering. */
    final Timer timer = new Timer() {
        @Override
        public void run() {
            if (selectedView == topicXMLDisplay) {
                topicXMLDisplay.getDriver().flush();
                topicSplitPanelRenderedDisplay.initialize(getTopicOrRevisionTopic().getItem(), isReadOnlyMode(), display.getSplitType());
            }
        }
    };

    /**
     * A click handler to add a tag to a topic
     * 
     * @author Matthew Casperson
     */
    private class AddTagClickhandler implements ClickHandler {

        @Override
        public void onClick(final ClickEvent event) {
            final RESTTagV1 selectedTag = topicTagsDisplay.getMyTags().getValue().getTag().getItem();

            /* Need to deal with re-adding removed tags */
            for (final RESTTagCollectionItemV1 tag : topicProviderData.getDisplayedItem().getItem().getTags().getItems()) {
                if (tag.getItem().getId().equals(selectedTag.getId())) {
                    if (tag.getState() == RESTBaseCollectionItemV1.REMOVE_STATE) {
                        tag.setState(RESTBaseCollectionItemV1.UNCHANGED_STATE);

                        /* Redisplay the view */
                        updateDisplayedTopicView();

                        return;
                    } else {
                        /* Don't add tags twice */
                        Window.alert(PressGangCCMSUI.INSTANCE.TagAlreadyExists());
                        return;
                    }
                }
            }

            /* Get the selected tag, and clone it */
            final RESTTagV1 selectedTagClone = selectedTag.clone(true);
            /* Add the tag to the topic */
            topicProviderData.getDisplayedItem().getItem().getTags().addNewItem(selectedTagClone);
            /* Redisplay the view */
            updateDisplayedTopicView();
        }
    }

    /**
     * A click handler to remove a tag from a topic
     * 
     * @author Matthew Casperson
     */
    private class DeleteTagClickHandler implements ClickHandler {
        private final RESTTagCollectionItemV1 tag;

        public DeleteTagClickHandler(final RESTTagCollectionItemV1 tag) {
            if (tag == null) {
                throw new IllegalArgumentException("tag cannot be null");
            }

            this.tag = tag;
        }

        @Override
        public void onClick(final ClickEvent event) {
            if (topicProviderData.getDisplayedItem() == null) {
                throw new IllegalStateException("topicProviderData.getDisplayedItem() cannot be null");
            }

            if (tag.getState() == RESTBaseCollectionItemV1.ADD_STATE) {
                /* Tag was added and then removed, so we just delete the tag */
                topicProviderData.getDisplayedItem().getItem().getTags().getItems().remove(tag);
            } else {
                /* Otherwise we set the tag as removed */
                tag.setState(RESTBaseCollectionItemV1.REMOVE_STATE);
            }

            updateDisplayedTopicView();
        }
    };

    @Inject
    private Display display;

    @Inject
    private TopicPresenter.Display topicViewDisplay;

    @Inject
    private TopicXMLPresenter.Display topicXMLDisplay;

    /** The rendered topic view display */
    @Inject
    private TopicRenderedPresenter.Display topicRenderedDisplay;

    /** The rendered topic view display in a split panel */
    @Inject
    private TopicRenderedPresenter.Display topicSplitPanelRenderedDisplay;

    @Inject
    private SearchResultsPresenter.Display searchResultsDisplay;

    @Inject
    private TopicXMLErrorsPresenter.Display topicXMLErrorsDisplay;

    @Inject
    private TopicTagsPresenter.Display topicTagsDisplay;

    @Inject
    private TopicBugsPresenter.Display topicBugsDisplay;

    @Inject
    private TopicRevisionsPresenter.Display topicRevisionsDisplay;

    /**
     * This will reference the selected view, so as to maintain the view between clicks
     */
    private TopicViewInterface selectedView;

    /**
     * This will reference the previously selected view,
     */
    private TopicViewInterface previousView;

    /**
     * The query string to be sent to the REST interface, as extracted from the History Token
     */
    private String queryString;

    /** Holds the data required to populate and refresh the topic list */
    private ProviderUpdateData<RESTTopicCollectionItemV1> topicProviderData = new ProviderUpdateData<RESTTopicCollectionItemV1>();

    /**
     * A copy of all the tags in the system, broken down into project->category->tag. Used when adding new tags to a topic.
     */
    private final SearchUIProjects searchUIProjects = new SearchUIProjects();

    /** Keeps a reference to the start row */
    private Integer tableStartRow;

    @Override
    public void go(final HasWidgets container) {
        display.setFeedbackLink(Constants.KEY_SURVEY_LINK + HISTORY_TOKEN);
        
        searchResultsDisplay.setViewShown(true);
        display.setViewShown(true);

        container.clear();
        container.add(display.getTopLevelPanel());

        display.getTopicResultsActionButtonsPanel().setWidget(searchResultsDisplay.getTopActionPanel());
        display.getTopicResultsPanel().setWidget(searchResultsDisplay.getPanel());

        bind();
    }

    /**
     * Add behaviour to the UI elements exposed by the views
     */
    private void bind() {

        super.bind(display, this);

        bindSplitPanelResize();

        final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider = generateTopicListProvider();
        searchResultsDisplay.setProvider(provider);

        /* set the provider, which will update the list */
        topicRevisionsDisplay.setProvider(generateTopicRevisionsListProvider());

        /* setup the bugs provider */
        topicBugsDisplay.setProvider(generateTopicBugListProvider());

        bindTopicListRowClicks();

        bindTopicEditButtons(provider);

        bindAceEditorButtons();

        bindNewTagListBoxes();

        bindViewTopicRevisionButton();

        getTags();
    }

    /**
     * Open a new window with the results of a prettydiff comparison
     * 
     * @param source The source XML
     * @param sourceLabel The source XML label
     * @param diff The diff XML
     * @param diffLabel The diff XML label
     */
    native private void displayDiff(final String source, final String sourceLabel, final String diff, final String diffLabel)
    /*-{
    var diffTable = $wnd.prettydiff(
    {
      source : source,
      sourcelabel : sourceLabel,
      diff : diff,
      difflabel : diffLabel,
      lang : "markup",
      mode : "diff",
      diffview : "sidebyside"
    })[0];

    var win = $wnd.open("", "_blank", "width=" + (screen.width - 200)
        + ", height=" + (screen.height - 200)); // a window object
    if (win != null)
    {
	    win.document.open("text/html", "replace");
	    win.document
	        .write("<HTML><HEAD><TITLE>PressGangCCMS XML Diff</TITLE><link rel=\"stylesheet\" type=\"text/css\" href=\"../prettydiff.css\"><link rel=\"stylesheet\" type=\"text/css\" href=\"prettydiff.css\"></HEAD><BODY>"
	            + diffTable + "</BODY></HTML>");
	    win.document.close();
    }
    }-*/;

    /**
     * Bind behaviour to the view buttons in the topic revisions cell table
     */
    private void bindViewTopicRevisionButton() {
        topicRevisionsDisplay.getDiffButton().setFieldUpdater(new FieldUpdater<RESTTopicCollectionItemV1, String>() {
            @Override
            public void update(final int index, final RESTTopicCollectionItemV1 revisionTopic, final String value) {
                final RESTCalls.RESTCallback<RESTTopicV1> callback = new RESTCalls.RESTCallback<RESTTopicV1>() {
                    @Override
                    public void begin() {
                        topicRevisionsDisplay.addWaitOperation();
                    }

                    @Override
                    public void generalException(final Exception ex) {
                        topicRevisionsDisplay.removeWaitOperation();
                    }

                    @Override
                    public void success(final RESTTopicV1 retValue) {
                        try {
                            final RESTTopicCollectionItemV1 sourceTopic = getTopicOrRevisionTopic();
                            final String retValueLabel = PressGangCCMSUI.INSTANCE.TopicID() + ": " + retValue.getId() + " "
                                    + PressGangCCMSUI.INSTANCE.TopicRevision() + ": " + retValue.getRevision().toString() + " "
                                    + PressGangCCMSUI.INSTANCE.RevisionDate() + ": "
                                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL).format(retValue.getLastModified());
                            final String sourceTopicLabel = PressGangCCMSUI.INSTANCE.TopicID()
                                    + ": "
                                    + sourceTopic.getItem().getId()
                                    + " "
                                    + PressGangCCMSUI.INSTANCE.TopicRevision()
                                    + ": "
                                    + sourceTopic.getItem().getRevision().toString()
                                    + " "
                                    + PressGangCCMSUI.INSTANCE.RevisionDate()
                                    + ": "
                                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL)
                                            .format(sourceTopic.getItem().getLastModified());
                            displayDiff(retValue.getXml(), retValueLabel, sourceTopic.getItem().getXml(), sourceTopicLabel);
                        } finally {
                            topicRevisionsDisplay.removeWaitOperation();
                        }
                    }

                    @Override
                    public void failed() {
                        topicRevisionsDisplay.removeWaitOperation();
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                    }

                };

                RESTCalls.getTopicRevision(callback, revisionTopic.getItem().getId(), revisionTopic.getItem().getRevision());

            }
        });

        topicRevisionsDisplay.getViewButton().setFieldUpdater(new FieldUpdater<RESTTopicCollectionItemV1, String>() {
            @Override
            public void update(final int index, final RESTTopicCollectionItemV1 revisionTopic, final String value) {
                
                /* Reset the reference to the revision topic */
                topicRevisionsDisplay.setRevisionTopic(null);

                if (revisionTopic.getItem().getRevision().equals(topicProviderData.getDisplayedItem().getItem().getRevision())) {
                    /*
                     * The latest revision is actually the same as the main topic, so if that is clicked, we want to edit the
                     * main topic
                     */
                    updateDisplayedTopicView();
                } else {
                    /* Reset the reference to the revision topic */
                    topicRevisionsDisplay.setRevisionTopic(revisionTopic);
                }
                
                /* default to the rendered view */
                if (selectedView == null) {
                    selectedView = topicRevisionsDisplay;
                    changeDisplayedTopicView();
                } else {
                    updateDisplayedTopicView();
                }
            }
        });
    }

    /**
     * Add behaviour to the tag view screen elements
     */
    private void bindNewTagListBoxes() {
        topicTagsDisplay.getProjectsList().addValueChangeHandler(new ValueChangeHandler<SearchUIProject>() {
            @Override
            public void onValueChange(final ValueChangeEvent<SearchUIProject> event) {
                topicTagsDisplay.updateNewTagCategoriesDisplay();
            }
        });

        topicTagsDisplay.getCategoriesList().addValueChangeHandler(new ValueChangeHandler<SearchUICategory>() {
            @Override
            public void onValueChange(final ValueChangeEvent<SearchUICategory> event) {
                topicTagsDisplay.updateNewTagTagDisplay();
            }
        });

        topicTagsDisplay.getAdd().addClickHandler(new AddTagClickhandler());
    }

    /**
     * Gets the tags, so they can be displayed and added to topics
     */
    private void getTags() {
        final RESTCalls.RESTCallback<RESTTagCollectionV1> callback = new RESTCalls.RESTCallback<RESTTagCollectionV1>() {

            @Override
            public void begin() {
                topicTagsDisplay.addWaitOperation();
            }

            @Override
            public void generalException(final Exception ex) {
                topicTagsDisplay.removeWaitOperation();
            }

            @Override
            public void success(final RESTTagCollectionV1 retValue) {
                try {
                    searchUIProjects.initialize(retValue);
                    topicTagsDisplay.initializeNewTags(searchUIProjects);
                } finally {
                    topicTagsDisplay.removeWaitOperation();
                }
            }

            @Override
            public void failed() {
                topicTagsDisplay.removeWaitOperation();
                Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
            }
        };

        RESTCalls.getTags(callback);
    }

    /**
     * Add behaviour to the tag delete buttons
     */
    private void bindTagEditingButtons() {

        /* This will be null if the tags have not been downloaded */
        if (topicTagsDisplay.getEditor() != null) {
            for (final TopicTagViewProjectEditor topicTagViewProjectEditor : topicTagsDisplay.getEditor().projects.getEditors()) {
                for (final TopicTagViewCategoryEditor topicTagViewCategoryEditor : topicTagViewProjectEditor.categories
                        .getEditors()) {
                    for (final TopicTagViewTagEditor topicTagViewTagEditor : topicTagViewCategoryEditor.myTags.getEditors()) {
                        topicTagViewTagEditor.getDelete().addClickHandler(
                                new DeleteTagClickHandler(topicTagViewTagEditor.getTag().getTag()));
                    }
                }
            }
        }
    }

    /**
     * Respond to the split panel resizing by redisplaying the ACE editor component
     */
    private void bindSplitPanelResize() {
        display.getSplitPanel().addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(final ResizeEvent event) {
                if (topicXMLDisplay.getEditor() != null) {
                    topicXMLDisplay.getEditor().redisplay();
                }
            }
        });
    }

    /**
     * @return A provider to be used for the topic revisions display list
     */
    private EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> generateTopicRevisionsListProvider() {
        final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTTopicCollectionItemV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTTopicCollectionItemV1> display) {
                if (topicProviderData.getDisplayedItem() != null && topicProviderData.getDisplayedItem().getItem().getRevisions() != null
                        && topicProviderData.getDisplayedItem().getItem().getRevisions().getItems() != null) {
                    displayNewFixedList(topicProviderData.getDisplayedItem().getItem().getRevisions().getItems());
                } else {
                    resetProvider();
                }
            }
        };
        return provider;
    }

    /**
     * @return A provider to be used for the topic display list
     */
    private EnhancedAsyncDataProvider<RESTBugzillaBugCollectionItemV1> generateTopicBugListProvider() {
        final EnhancedAsyncDataProvider<RESTBugzillaBugCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTBugzillaBugCollectionItemV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTBugzillaBugCollectionItemV1> display) {
                if (topicProviderData.getDisplayedItem() != null
                        && topicProviderData.getDisplayedItem().getItem().getBugzillaBugs_OTM() != null) {
                    displayNewFixedList(topicProviderData.getDisplayedItem().getItem().getBugzillaBugs_OTM().getItems());
                } else {
                    resetProvider();
                }
            }
        };
        return provider;
    }

    /**
     * @return A provider to be used for the topic display list
     */
    private EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> generateTopicListProvider() {
        final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTTopicCollectionItemV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTTopicCollectionItemV1> display) {

                final RESTCalls.RESTCallback<RESTTopicCollectionV1> callback = new RESTCalls.RESTCallback<RESTTopicCollectionV1>() {
                    @Override
                    public void begin() {
                        resetProvider();
                        searchResultsDisplay.addWaitOperation();
                    }

                    @Override
                    public void generalException(final Exception ex) {
                        Window.alert(PressGangCCMSUI.INSTANCE.ErrorGettingTopics());
                        searchResultsDisplay.removeWaitOperation();
                    }

                    @Override
                    public void success(final RESTTopicCollectionV1 retValue) {
                        try {
                            topicProviderData.setItems(retValue.getItems());
                            displayAsynchronousList(topicProviderData.getItems(), retValue.getSize(), display.getVisibleRange()
                                    .getStart());
                        } finally {
                            searchResultsDisplay.removeWaitOperation();
                        }
                    }

                    @Override
                    public void failed() {
                        searchResultsDisplay.removeWaitOperation();
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                    }
                };

                tableStartRow = display.getVisibleRange().getStart();
                final int length = display.getVisibleRange().getLength();
                final int end = tableStartRow + length;

                RESTCalls.getTopicsFromQuery(callback, queryString, tableStartRow, end);
            }
        };
        return provider;
    }

    /**
     * Bind the button click events for the topic editor screens
     */
    private void bindTopicListRowClicks() {
        searchResultsDisplay.getResults().addCellPreviewHandler(new Handler<RESTTopicCollectionItemV1>() {
            @Override
            public void onCellPreview(final CellPreviewEvent<RESTTopicCollectionItemV1> event) {
                /* Check to see if this was a click event */
                final boolean isClick = Constants.JAVASCRIPT_CLICK_EVENT.equals(event.getNativeEvent().getType());

                if (isClick) {
                    topicProviderData.setSelectedItem(event.getValue());
                    topicProviderData.setDisplayedItem(event.getValue().clone(true));
                    loadNewTopicDetails();                    
                }
            }
        });
    }
    
    private void loadNewTopicDetails()
    {
        topicRevisionsDisplay.setRevisionTopic(null);

        /* Update the current view, or display the default */
        if (selectedView == null) {
            /* Switch to the default view if none was selected */
            selectedView = topicRenderedDisplay;
            changeDisplayedTopicView();
        } else {
            /* Otherwise update the display with the new topic */
            updateDisplayedTopicView();
        }

        /* set the revisions to show the loading widget */
        if (topicRevisionsDisplay.getProvider() != null)
            topicRevisionsDisplay.getProvider().resetProvider();

        /* set the bugs to show the loading widget */
        if (topicBugsDisplay.getProvider() != null)
            topicBugsDisplay.getProvider().resetProvider();

        /* A callback to respond to a request for a topic with the revisions expanded */
        final RESTCalls.RESTCallback<RESTTopicV1> topicWithRevisionsCallback = new RESTCalls.RESTCallback<RESTTopicV1>() {
            @Override
            public void begin() {
                topicRevisionsDisplay.addWaitOperation();
            }

            @Override
            public void generalException(final Exception ex) {
                topicRevisionsDisplay.removeWaitOperation();
            }

            @Override
            public void success(final RESTTopicV1 retValue) {
                try {
                    topicProviderData.getDisplayedItem().getItem().setRevisions(retValue.getRevisions());

                    /* refresh the list */
                    topicRevisionsDisplay.getProvider().displayNewFixedList(retValue.getRevisions().getItems());

                } finally {
                    topicRevisionsDisplay.removeWaitOperation();
                }

            }

            @Override
            public void failed() {
                topicRevisionsDisplay.removeWaitOperation();
                Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
            }
        };

        /* A callback to respond to a request for a topic with the tags expanded */
        final RESTCalls.RESTCallback<RESTTopicV1> topicWithTagsCallback = new RESTCalls.RESTCallback<RESTTopicV1>() {
            @Override
            public void begin() {
                topicTagsDisplay.addWaitOperation();
            }

            @Override
            public void generalException(final Exception ex) {
                topicTagsDisplay.removeWaitOperation();
            }

            @Override
            public void success(final RESTTopicV1 retValue) {
                try {
                    /* copy the revisions into the displayed Topic */
                    topicProviderData.getDisplayedItem().getItem().setTags(retValue.getTags());

                    /* If we are looking at the rendered view, update it */
                    if (selectedView == topicTagsDisplay) {
                        updateDisplayedTopicView();
                    }
                } finally {
                    topicTagsDisplay.removeWaitOperation();
                }

            }

            @Override
            public void failed() {
                topicTagsDisplay.removeWaitOperation();
                Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
            }
        };

        /* A callback to respond to a request for a topic with the bugzilla bugs expanded */
        final RESTCalls.RESTCallback<RESTTopicV1> topicWithBugsCallback = new RESTCalls.RESTCallback<RESTTopicV1>() {
            @Override
            public void begin() {
                topicBugsDisplay.addWaitOperation();
            }

            @Override
            public void generalException(final Exception ex) {
                topicBugsDisplay.removeWaitOperation();
            }

            @Override
            public void success(final RESTTopicV1 retValue) {
                try {
                    final RESTBugzillaBugCollectionV1 collection = retValue.getBugzillaBugs_OTM();

                    /* copy the revisions into the displayed Topic */
                    topicProviderData.getDisplayedItem().getItem().setBugzillaBugs_OTM(collection);

                    /* refresh the celltable */
                    topicBugsDisplay.getProvider().displayNewFixedList(collection.getItems());
                } finally {
                    topicBugsDisplay.removeWaitOperation();
                }

            }

            @Override
            public void failed() {
                topicBugsDisplay.removeWaitOperation();
                Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
            }
        };

        /* Initiate the REST calls */
        RESTCalls.getTopicWithTags(topicWithTagsCallback, topicProviderData.getSelectedItem().getItem().getId());
        RESTCalls.getTopicWithRevisions(topicWithRevisionsCallback, topicProviderData.getSelectedItem().getItem().getId());
        RESTCalls.getTopicWithBugs(topicWithBugsCallback, topicProviderData.getSelectedItem().getItem().getId());
    }

    /**
     * Bind the button clicks for the ACE editor buttons
     */
    private void bindAceEditorButtons() {
        topicXMLDisplay.getLineWrap().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                topicXMLDisplay.getEditor().setUseWrapMode(!topicXMLDisplay.getEditor().getUserWrapMode());
                topicXMLDisplay.getLineWrap().setDown(topicXMLDisplay.getEditor().getUserWrapMode());
            }
        });

        topicXMLDisplay.getShowInvisibles().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                topicXMLDisplay.getEditor().setShowInvisibles(!topicXMLDisplay.getEditor().getShowInvisibles());
                topicXMLDisplay.getShowInvisibles().setDown(topicXMLDisplay.getEditor().getShowInvisibles());
            }
        });
    }

    /**
     * Bind the button click events for the various topic views.
     * 
     * @param provider The provider created by the generateTopicListProvider() method
     */
    private void bindTopicEditButtons(final EnhancedAsyncDataProvider<RESTTopicCollectionItemV1> provider) {
        /* Build up a click handler to save the topic */
        final ClickHandler saveClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                if (topicProviderData.getDisplayedItem() != null) {
                    final RESTCalls.RESTCallback<RESTTopicV1> callback = new RESTCalls.RESTCallback<RESTTopicV1>() {
                        @Override
                        public void begin() {
                            display.addWaitOperation();
                        }

                        @Override
                        public void generalException(final Exception ex) {
                            Window.alert(PressGangCCMSUI.INSTANCE.ErrorSavingTopic());
                            display.removeWaitOperation();
                            topicXMLDisplay.getEditor().redisplay();
                        }

                        @Override
                        public void success(final RESTTopicV1 retValue) {
                            try {
                                /* Update the displayed topic */
                                retValue.cloneInto(topicProviderData.getDisplayedItem().getItem(), true);
                                /* Update the selected topic */
                                retValue.cloneInto(topicProviderData.getSelectedItem().getItem(), true);
                                /* Update the topic list */
                                provider.updateRowData(tableStartRow, topicProviderData.getItems());                              
                                
                                loadNewTopicDetails();

                                Window.alert(PressGangCCMSUI.INSTANCE.SaveSuccess());
                            } finally {
                                display.removeWaitOperation();
                                if (topicXMLDisplay.getEditor() != null) {
                                    topicXMLDisplay.getEditor().redisplay();
                                }
                            }
                        }

                        @Override
                        public void failed() {
                            Window.alert(PressGangCCMSUI.INSTANCE.ErrorSavingTopic());
                            display.removeWaitOperation();
                            topicXMLDisplay.getEditor().redisplay();
                        }
                    };

                    /* Sync any changes back to the underlying object */
                    flushChanges();

                    /*
                     * Create a new instance of the topic, with all the properties set to explicitly update
                     */
                    final RESTTopicV1 updateTopic = topicProviderData.getDisplayedItem().getItem().clone(true);
                    updateTopic.explicitSetBugzillaBugs_OTM(updateTopic.getBugzillaBugs_OTM());
                    updateTopic.explicitSetProperties(updateTopic.getProperties());
                    updateTopic.explicitSetSourceUrls_OTM(updateTopic.getSourceUrls_OTM());
                    updateTopic.explicitSetTags(updateTopic.getTags());
                    updateTopic.explicitSetDescription(updateTopic.getDescription());
                    updateTopic.explicitSetLocale(updateTopic.getLocale());
                    updateTopic.explicitSetTitle(updateTopic.getTitle());
                    updateTopic.explicitSetXml(updateTopic.getXml());

                    RESTCalls.saveTopic(callback, updateTopic);
                }
            }
        };

        final ClickHandler topicViewClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicViewDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler topicXMLClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicXMLDisplay;
                    changeDisplayedTopicView();

                }
            }
        };

        final ClickHandler topicRenderedClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicRenderedDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler topicXMLErrorsClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicXMLErrorsDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler topicTagsClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicTagsDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler topicBugsClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicBugsDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler topicRevisionsClickHanlder = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                if (topicProviderData.getDisplayedItem() != null) {
                    selectedView = topicRevisionsDisplay;
                    changeDisplayedTopicView();
                }
            }
        };

        final ClickHandler splitMenuHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                showRenderedSplitPanelMenu();
            }
        };

        final ClickHandler splitMenuCloseHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                showRegularMenu();
            }
        };

        final ClickHandler splitMenuNoSplitHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                eventBus.fireEvent(new SearchResultsAndTopicViewEvent(queryString));
            }
        };

        final ClickHandler splitMenuVSplitHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                eventBus.fireEvent(new SearchResultsAndTopicViewEvent(SPLIT_TOKEN_VERTICAL + queryString));
            }
        };

        final ClickHandler splitMenuHSplitHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                /* Sync any changes back to the underlying object */
                flushChanges();

                eventBus.fireEvent(new SearchResultsAndTopicViewEvent(SPLIT_TOKEN_HORIZONTAL + queryString));
            }
        };

        /* Hook up the click listeners */
        for (final TopicViewInterface view : new TopicViewInterface[] { topicViewDisplay, topicXMLDisplay,
                topicRenderedDisplay, topicXMLErrorsDisplay, topicTagsDisplay, topicBugsDisplay, topicRevisionsDisplay }) {
            view.getRenderedSplit().addClickHandler(splitMenuHandler);
            view.getFields().addClickHandler(topicViewClickHandler);
            view.getXml().addClickHandler(topicXMLClickHandler);
            view.getRendered().addClickHandler(topicRenderedClickHandler);
            view.getSave().addClickHandler(saveClickHandler);
            view.getXmlErrors().addClickHandler(topicXMLErrorsClickHandler);
            view.getTags().addClickHandler(topicTagsClickHandler);
            view.getBugs().addClickHandler(topicBugsClickHandler);
            view.getHistory().addClickHandler(topicRevisionsClickHanlder);

            view.getRenderedSplitOpen().addClickHandler(splitMenuCloseHandler);
            view.getRenderedSplitClose().addClickHandler(splitMenuCloseHandler);
            view.getRenderedNoSplit().addClickHandler(splitMenuNoSplitHandler);
            view.getRenderedVerticalSplit().addClickHandler(splitMenuVSplitHandler);
            view.getRenderedHorizontalSplit().addClickHandler(splitMenuHSplitHandler);
        }
    }

    /**
     * Updates the current topic view
     */
    private void updateDisplayedTopicView() {
        /* Update the page name */
        final StringBuilder title = new StringBuilder(selectedView.getPageName());
        if (this.topicProviderData.getDisplayedItem() != null) {
            title.append(": " + topicProviderData.getDisplayedItem().getItem().getTitle());
        }
        display.getPageTitle().setText(title.toString());

        /*
         * Here we use the initialize function to copy the topic data into the GWT Editors. To save some data being retreived
         * and sent by the server, the revisions view always uses the revisions from the current topic. All other views will
         * display the revision topic if it has been selected.
         */

        /*
         * Need to do an initial call to initialize for the rendered view in the split pane
         */
        topicSplitPanelRenderedDisplay.initialize(getTopicOrRevisionTopic().getItem(), isReadOnlyMode(), display.getSplitType());
        /* By default, stop the automatic updating of the rendered view panel */
        timer.cancel();

        if (selectedView == this.topicRevisionsDisplay) {
            /*
             * The revisions always come from the parent topic (this saves us expanding the revisions when loading a revision
             */
            selectedView.initialize(topicProviderData.getDisplayedItem().getItem(), isReadOnlyMode(), display.getSplitType());
        } else {
            /* All other details come from the revision topic */
            selectedView.initialize(getTopicOrRevisionTopic().getItem(), isReadOnlyMode(), display.getSplitType());
        }

        /* Need to redisplay to work around a bug in the ACE editor */
        if (selectedView == this.topicXMLDisplay) {
            topicXMLDisplay.getLineWrap().setDown(topicXMLDisplay.getEditor().getUserWrapMode());
            topicXMLDisplay.getShowInvisibles().setDown(topicXMLDisplay.getEditor().getShowInvisibles());
            topicXMLDisplay.getEditor().redisplay();

            /* While editing the XML, we need to setup a refresh of the rendered view */
            if (display.getSplitType() != SplitType.NONE) {
                if (!isReadOnlyMode()) {
                    timer.scheduleRepeating(REFRESH_RATE);
                }
            }
        }

        /*
         * Here we add behaviours to additional buttons or views that don't use the Editor framework (like those that use
         * CellTables)
         */

        /*
         * if we just displayed a new selection of tags, link up all the tag delete buttons
         */
        else if (selectedView == this.topicTagsDisplay) {
            bindTagEditingButtons();
        }
    }

    /**
     * Displays a new topic view
     */
    private void changeDisplayedTopicView() {
        display.getTopicViewActionButtonsPanel().clear();
        display.getTopicViewPanel().clear();

        display.getTopicViewActionButtonsPanel().setWidget(selectedView.getTopActionPanel());
        display.getTopicViewPanel().setWidget(selectedView.getPanel());

        if (previousView != null) {
            previousView.setViewShown(false);
        }
        selectedView.setViewShown(true);

        previousView = selectedView;

        updateDisplayedTopicView();
    }

    /**
     * Sync any changes back to the underlying object
     */
    private void flushChanges() {
        if (selectedView == null || selectedView.getDriver() == null) {
            return;
        }

        /* These are read only views */
        if (selectedView == topicXMLErrorsDisplay || selectedView == topicTagsDisplay) {
            return;
        }

        selectedView.getDriver().flush();
    }

    @Override
    public void parseToken(final String historyToken) {
        queryString = historyToken.replace(HISTORY_TOKEN + ";", "");

        if (queryString.startsWith(SPLIT_TOKEN_HORIZONTAL)) {
            display.initialize(SplitType.HORIZONTAL, topicSplitPanelRenderedDisplay.getPanel());
        } else if (queryString.startsWith(SPLIT_TOKEN_VERTICAL)) {
            display.initialize(SplitType.VERTICAL, topicSplitPanelRenderedDisplay.getPanel());
        } else {
            display.initialize(SplitType.NONE, topicSplitPanelRenderedDisplay.getPanel());
        }

        queryString = queryString.replace(SPLIT_TOKEN_HORIZONTAL, "").replace(SPLIT_TOKEN_VERTICAL, "");

        if (!queryString.startsWith(Constants.QUERY_PATH_SEGMENT_PREFIX)) {
            queryString = Constants.QUERY_PATH_SEGMENT_PREFIX;
        }
    }

    /**
     * The currently displayed topic is topicRevisionsDisplay.getRevisionTopic() if it is not null, or
     * topicProviderData.getDisplayedItem() otherwise.
     * 
     * @return The currently displayed topic
     */
    private RESTTopicCollectionItemV1 getTopicOrRevisionTopic() {
        final RESTTopicCollectionItemV1 sourceTopic = topicRevisionsDisplay.getRevisionTopic() == null ? topicProviderData.getDisplayedItem()
                : topicRevisionsDisplay.getRevisionTopic();
        return sourceTopic;
    }

    /**
     * The UI is in a readonly mode if viewing a topic revision
     * 
     * @return true if the UI is in readonly mode, and false otherwise
     */
    private boolean isReadOnlyMode() {
        return topicRevisionsDisplay.getRevisionTopic() != null;
    }

    private void showRegularMenu() {
        display.getTopicViewActionButtonsPanel().clear();
        display.getTopicViewActionButtonsPanel().add(selectedView.getTopActionPanel());
    }

    private void showRenderedSplitPanelMenu() {
        display.getTopicViewActionButtonsPanel().clear();
        display.getTopicViewActionButtonsPanel().add(selectedView.getRenderedSplitViewMenu());
    }

    @Override
    public boolean checkForUnsavedChanges() {
        return true;
    }
}