package org.jboss.pressgangccms.client.local.mvp.presenter.topicsearch;

import java.util.ArrayList;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgangccms.client.local.constants.Constants;
import org.jboss.pressgangccms.client.local.mvp.events.SearchResultsAndTopicViewEvent;
import org.jboss.pressgangccms.client.local.mvp.presenter.base.EditableView;
import org.jboss.pressgangccms.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicBugsPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicRenderedPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicRevisionsPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicTagsPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicXMLErrorsPresenter;
import org.jboss.pressgangccms.client.local.mvp.presenter.topic.TopicXMLPresenter;
import org.jboss.pressgangccms.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgangccms.client.local.mvp.view.topic.TopicViewInterface;
import org.jboss.pressgangccms.client.local.mvp.view.topicsearch.SearchResultsAndTopicView;
import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.restcalls.RESTCalls;
import org.jboss.pressgangccms.client.local.ui.ProviderUpdateData;
import org.jboss.pressgangccms.client.local.ui.SplitType;
import org.jboss.pressgangccms.client.local.ui.editor.topicview.assignedtags.TopicTagViewCategoryEditor;
import org.jboss.pressgangccms.client.local.ui.editor.topicview.assignedtags.TopicTagViewProjectEditor;
import org.jboss.pressgangccms.client.local.ui.editor.topicview.assignedtags.TopicTagViewTagEditor;
import org.jboss.pressgangccms.client.local.ui.search.SearchUICategory;
import org.jboss.pressgangccms.client.local.ui.search.SearchUIProject;
import org.jboss.pressgangccms.client.local.ui.search.SearchUIProjects;
import org.jboss.pressgangccms.rest.v1.collections.RESTBugzillaBugCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTBugzillaBugV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicV1;

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
import com.google.gwt.view.client.AsyncDataProvider;
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
                topicSplitPanelRenderedDisplay.initialize(getTopicOrRevisionTopic(), isReadOnlyMode(), display.getSplitType());
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
            final RESTTagV1 selectedTag = topicTagsDisplay.getMyTags().getValue().getTag();

            /* Need to deal with re-adding removed tags */
            for (final RESTTagV1 tag : topicProviderData.getDisplayedItem().getTags().getItems()) {
                if (tag.getId().equals(selectedTag.getId())) {
                    if (tag.getRemoveItem()) {
                        tag.setRemoveItem(false);

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
            /*
             * Set the add item property to true, to indicate that we need at add this tag to the topic
             */
            selectedTagClone.setAddItem(true);
            /* Add the tag to the topic */
            topicProviderData.getDisplayedItem().getTags().addItem(selectedTagClone);
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
        private final RESTTagV1 tag;

        public DeleteTagClickHandler(final RESTTagV1 tag) {
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

            if (tag.getAddItem()) {
                /* Tag was added and then removed, so we just delete the tag */
                topicProviderData.getDisplayedItem().getTags().getItems().remove(tag);
            } else {
                /* Otherwise we set the tag as removed */
                tag.setRemoveItem(true);
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
    private ProviderUpdateData<RESTTopicV1> topicProviderData = new ProviderUpdateData<RESTTopicV1>();

    /**
     * A copy of all the tags in the system, broken down into project->category->tag. Used when adding new tags to a topic.
     */
    private final SearchUIProjects searchUIProjects = new SearchUIProjects();

    /** Keeps a reference to the start row */
    private Integer tableStartRow;

    @Override
    public void go(final HasWidgets container) {
        searchResultsDisplay.setViewShown(true);

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

        final AsyncDataProvider<RESTTopicV1> provider = generateTopicListProvider();
        searchResultsDisplay.setProvider(provider);

        /* set the provider, which will update the list */
        final AsyncDataProvider<RESTTopicV1> revisionsPropvider = generateTopicRevisionsListProvider();
        topicRevisionsDisplay.setProvider(revisionsPropvider);

        /* setup the bugs provider */
        final AsyncDataProvider<RESTBugzillaBugV1> bugzillaProvider = generateTopicBugListProvider();
        topicBugsDisplay.setProvider(bugzillaProvider);

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
        topicRevisionsDisplay.getDiffButton().setFieldUpdater(new FieldUpdater<RESTTopicV1, String>() {
            @Override
            public void update(final int index, final RESTTopicV1 revisionTopic, final String value) {
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
                            final RESTTopicV1 sourceTopic = getTopicOrRevisionTopic();
                            final String retValueLabel = PressGangCCMSUI.INSTANCE.TopicID() + ": " + retValue.getId() + " "
                                    + PressGangCCMSUI.INSTANCE.TopicRevision() + ": " + retValue.getRevision().toString() + " "
                                    + PressGangCCMSUI.INSTANCE.RevisionDate() + ": "
                                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL).format(retValue.getLastModified());
                            final String sourceTopicLabel = PressGangCCMSUI.INSTANCE.TopicID()
                                    + ": "
                                    + sourceTopic.getId()
                                    + " "
                                    + PressGangCCMSUI.INSTANCE.TopicRevision()
                                    + ": "
                                    + sourceTopic.getRevision().toString()
                                    + " "
                                    + PressGangCCMSUI.INSTANCE.RevisionDate()
                                    + ": "
                                    + DateTimeFormat.getFormat(PredefinedFormat.DATE_FULL)
                                            .format(sourceTopic.getLastModified());
                            displayDiff(retValue.getXml(), retValueLabel, sourceTopic.getXml(), sourceTopicLabel);
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

                RESTCalls.getTopicRevision(callback, revisionTopic.getId(), revisionTopic.getRevision());

            }
        });

        topicRevisionsDisplay.getViewButton().setFieldUpdater(new FieldUpdater<RESTTopicV1, String>() {
            @Override
            public void update(final int index, final RESTTopicV1 revisionTopic, final String value) {
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
                            topicRevisionsDisplay.setRevisionTopic(retValue);

                            /* default to the rendered view */
                            if (selectedView == null) {
                                selectedView = topicRevisionsDisplay;
                                changeDisplayedTopicView();
                            } else {
                                updateDisplayedTopicView();
                            }
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

                /* Reset the reference to the revision topic */
                topicRevisionsDisplay.setRevisionTopic(null);

                if (revisionTopic.getRevision().equals(topicProviderData.getDisplayedItem().getRevision())) {
                    /*
                     * The latest revision is actually the same as the main topic, so if that is clicked, we want to edit the
                     * main topic
                     */
                    updateDisplayedTopicView();
                } else {
                    RESTCalls.getTopicRevision(callback, revisionTopic.getId(), revisionTopic.getRevision());
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
    private AsyncDataProvider<RESTTopicV1> generateTopicRevisionsListProvider() {
        final AsyncDataProvider<RESTTopicV1> provider = new AsyncDataProvider<RESTTopicV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTTopicV1> display) {
                if (topicProviderData.getDisplayedItem() != null && topicProviderData.getDisplayedItem().getRevisions() != null
                        && topicProviderData.getDisplayedItem().getRevisions().getItems() != null) {

                    final int count = topicProviderData.getDisplayedItem().getRevisions().getItems().size();
                    final int tableStartRow = display.getVisibleRange().getStart();
                    final int length = display.getVisibleRange().getLength();
                    final int end = tableStartRow + length;
                    final int fixedEnd = end > count ? count : end;

                    updateRowData(tableStartRow,
                            topicProviderData.getDisplayedItem().getRevisions().getItems().subList(tableStartRow, fixedEnd));
                    updateRowCount(count, true);
                } else {
                    updateRowCount(0, false);
                }
            }
        };
        return provider;
    }

    /**
     * @return A provider to be used for the topic display list
     */
    private AsyncDataProvider<RESTBugzillaBugV1> generateTopicBugListProvider() {
        final AsyncDataProvider<RESTBugzillaBugV1> provider = new AsyncDataProvider<RESTBugzillaBugV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTBugzillaBugV1> display) {
                if (topicProviderData.getDisplayedItem() != null
                        && topicProviderData.getDisplayedItem().getBugzillaBugs_OTM() != null
                        && topicProviderData.getDisplayedItem().getBugzillaBugs_OTM().getItems() != null) {

                    final int bugzillaCount = topicProviderData.getDisplayedItem().getBugzillaBugs_OTM().getItems().size();
                    final int tableStartRow = display.getVisibleRange().getStart();
                    final int length = display.getVisibleRange().getLength();
                    final int end = tableStartRow + length;
                    final int fixedEnd = end > bugzillaCount ? bugzillaCount : end;

                    updateRowData(
                            tableStartRow,
                            topicProviderData.getDisplayedItem().getBugzillaBugs_OTM().getItems()
                                    .subList(tableStartRow, fixedEnd));
                    updateRowCount(bugzillaCount, true);
                } else {
                    updateRowCount(0, false);
                }
            }
        };
        return provider;
    }

    /**
     * @return A provider to be used for the topic display list
     */
    private AsyncDataProvider<RESTTopicV1> generateTopicListProvider() {
        final AsyncDataProvider<RESTTopicV1> provider = new AsyncDataProvider<RESTTopicV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTTopicV1> display) {
                tableStartRow = display.getVisibleRange().getStart();
                final int length = display.getVisibleRange().getLength();
                final int end = tableStartRow + length;

                final RESTCalls.RESTCallback<RESTTopicCollectionV1> callback = new RESTCalls.RESTCallback<RESTTopicCollectionV1>() {
                    @Override
                    public void begin() {
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
                            updateRowData(tableStartRow, topicProviderData.getItems());
                            updateRowCount(retValue.getSize(), true);
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

                RESTCalls.getTopicsFromQuery(callback, queryString, tableStartRow, end);
            }
        };
        return provider;
    }

    /**
     * Bind the button click events for the topic editor screens
     */
    private void bindTopicListRowClicks() {
        searchResultsDisplay.getResults().addCellPreviewHandler(new Handler<RESTTopicV1>() {
            @Override
            public void onCellPreview(final CellPreviewEvent<RESTTopicV1> event) {
                /* Check to see if this was a click event */
                final boolean isClick = Constants.JAVASCRIPT_CLICK_EVENT.equals(event.getNativeEvent().getType());

                if (isClick) {
                    topicProviderData.setSelectedItem(event.getValue());
                    topicProviderData.setDisplayedItem(event.getValue().clone(true));
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
                        topicRevisionsDisplay.getProvider().updateRowCount(0, false);

                    /* set the bugs to show the loading widget */
                    if (topicBugsDisplay.getProvider() != null)
                        topicBugsDisplay.getProvider().updateRowCount(0, false);

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
                                topicProviderData.getDisplayedItem().setRevisions(retValue.getRevisions());

                                /* refresh the list */
                                topicRevisionsDisplay.getProvider().updateRowData(0, retValue.getRevisions().getItems());
                                topicRevisionsDisplay.getProvider().updateRowCount(retValue.getRevisions().getItems().size(),
                                        true);

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
                                topicProviderData.getDisplayedItem().setTags(retValue.getTags());

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
                                topicProviderData.getDisplayedItem().setBugzillaBugs_OTM(collection);

                                /* refresh the celltable */
                                topicBugsDisplay.getProvider().updateRowData(0, collection.getItems());
                                topicBugsDisplay.getProvider().updateRowCount(collection.getItems().size(), true);
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
                    RESTCalls.getTopicWithTags(topicWithTagsCallback, topicProviderData.getSelectedItem().getId());
                    RESTCalls.getTopicWithRevisions(topicWithRevisionsCallback, topicProviderData.getSelectedItem().getId());
                    RESTCalls.getTopicWithBugs(topicWithBugsCallback, topicProviderData.getSelectedItem().getId());
                }
            }
        });
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
    private void bindTopicEditButtons(final AsyncDataProvider<RESTTopicV1> provider) {
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
                                /* Update the local collection of topics */
                                retValue.cloneInto(topicProviderData.getDisplayedItem(), true);

                                /* The title may have been updated */
                                if (!topicProviderData.getSelectedItem().getTitle()
                                        .equals(topicProviderData.getDisplayedItem().getTitle())) {
                                    /* Update the title */
                                    topicProviderData.getSelectedItem().setTitle(
                                            topicProviderData.getDisplayedItem().getTitle());
                                    /* Update the list of topics */
                                    provider.updateRowData(tableStartRow, topicProviderData.getItems());
                                }

                                /* Update the edit window */
                                selectedView.initialize(getTopicOrRevisionTopic(), isReadOnlyMode(), display.getSplitType());

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
                    final RESTTopicV1 updateTopic = topicProviderData.getDisplayedItem().clone(true);
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
            title.append(": " + topicProviderData.getDisplayedItem().getTitle());
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
        topicSplitPanelRenderedDisplay.initialize(getTopicOrRevisionTopic(), isReadOnlyMode(), display.getSplitType());
        /* By default, stop the automatic updating of the rendered view panel */
        timer.cancel();

        if (selectedView == this.topicRevisionsDisplay) {
            /*
             * The revisions always come from the parent topic (this saves us expanding the revisions when loading a revision
             */
            selectedView.initialize(topicProviderData.getDisplayedItem(), isReadOnlyMode(), display.getSplitType());
        } else {
            /* All other details come from the revision topic */
            selectedView.initialize(getTopicOrRevisionTopic(), isReadOnlyMode(), display.getSplitType());
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
        queryString = historyToken.replace(SearchResultsAndTopicView.HISTORY_TOKEN + ";", "");

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
    private RESTTopicV1 getTopicOrRevisionTopic() {
        final RESTTopicV1 sourceTopic = topicRevisionsDisplay.getRevisionTopic() == null ? topicProviderData.getDisplayedItem()
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
