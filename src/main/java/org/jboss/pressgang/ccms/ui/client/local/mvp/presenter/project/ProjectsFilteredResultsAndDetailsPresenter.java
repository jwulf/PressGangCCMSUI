package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.project;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTProjectCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.viewevents.ProjectsFilteredResultsAndProjectViewEvent;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.children.AddPossibleChildCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.children.GetExistingCollectionCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.children.UpdateAfterChildModifiedCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.BaseSearchAndEditPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.DisplayNewEntityCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.searchandedit.GetNewEntityCallback;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseCustomViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.searchandedit.BaseSearchAndEditViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.preferences.Preferences;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls.RESTCallback;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.projectview.RESTProjectV1BasicDetailsEditor;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.*;

@Dependent
public class ProjectsFilteredResultsAndDetailsPresenter
        extends
        BaseSearchAndEditPresenter<
                        RESTProjectV1,
                        RESTProjectCollectionV1,
                        RESTProjectCollectionItemV1,
                        RESTProjectV1BasicDetailsEditor>
        implements BaseTemplatePresenterInterface {


    /**
     * This interface describes the required UI elements for the parent view (i.e. the view that holds the two views
     * CategoryFilteredResults view to provide a list of categories and the CategoryView.
     *
     * @author Matthew Casperson
     */
    public interface Display extends BaseSearchAndEditViewInterface<RESTProjectV1, RESTProjectCollectionV1, RESTProjectCollectionItemV1> {
        PushButton getChildren();

        PushButton getDetails();

        PushButton getSave();

        Label getChildrenDown();

        Label getDetailsDown();
    }

    /**
     * The history token used to identify this view
     */
    public static final String HISTORY_TOKEN = "ProjectsFilteredResultsAndProjectView";

    /**
     * A logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ProjectsFilteredResultsAndDetailsPresenter.class.getName());

    @Inject
    private HandlerManager eventBus;

    /**
     * An Errai injected instance of a class that implements Display. This is the view that holds all other views
     */
    @Inject
    private Display display;

    /**
     * An Errai injected instance of a class that implements ProjectFilteredResultsPresenter
     */
    @Inject
    private ProjectFilteredResultsPresenter filteredResultsComponent;

    /**
     * An Errai injected instance of a class that implements ProjectPresenter
     */
    @Inject
    private ProjectPresenter resultComponent;

    @Inject
    private ProjectTagPresenter tagComponent;

    /**
     * The category query string extracted from the history token
     */
    private String queryString;

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindSearchAndEditExtended(ServiceConstants.PROJECT_HELP_TOPIC, HISTORY_TOKEN, queryString);
    }

    @Override
    public void bindSearchAndEditExtended(final int topicId, @NotNull final String pageId, @NotNull final String queryString) {
        /* A call back used to get a fresh copy of the entity that was selected */
        @NotNull final GetNewEntityCallback<RESTProjectV1> getNewEntityCallback = new GetNewEntityCallback<RESTProjectV1>() {

            @Override
            public void getNewEntity(@NotNull final RESTProjectV1 selectedEntity, @NotNull final DisplayNewEntityCallback<RESTProjectV1> displayCallback) {
                @NotNull final RESTCallback<RESTProjectV1> callback = new BaseRestCallback<RESTProjectV1, BaseTemplateViewInterface>(display,
                        new BaseRestCallback.SuccessAction<RESTProjectV1, BaseTemplateViewInterface>() {
                            @Override
                            public void doSuccessAction(@NotNull final RESTProjectV1 retValue, @NotNull final BaseTemplateViewInterface display) {
                                checkArgument(retValue.getTags() != null, "The initially retrieved entity should have an expanded tags collection");
                                displayCallback.displayNewEntity(retValue);
                            }
                        });
                RESTCalls.getProject(callback, selectedEntity.getId());
            }
        };


        display.setFeedbackLink(Constants.KEY_SURVEY_LINK + HISTORY_TOKEN);

        filteredResultsComponent.bindExtendedFilteredResults(ServiceConstants.SEARCH_VIEW_HELP_TOPIC, pageId, queryString);
        resultComponent.bindExtended(ServiceConstants.PROJECT_DETAILS_HELP_TOPIC, pageId);
        tagComponent.bindChildrenExtended(ServiceConstants.PROJECT_TAGS_HELP_TOPIC, pageId);
        super.bindSearchAndEdit(topicId, pageId, Preferences.PROJECT_VIEW_MAIN_SPLIT_WIDTH, resultComponent.getDisplay(), resultComponent.getDisplay(),
                filteredResultsComponent.getDisplay(), filteredResultsComponent, display, display, getNewEntityCallback);

        /* Bind the logic to add and remove possible children */
        tagComponent.bindPossibleChildrenListButtonClicks(
                new GetExistingCollectionCallback<RESTTagV1, RESTTagCollectionV1, RESTTagCollectionItemV1>() {

                    @NotNull
                    @Override
                    public RESTTagCollectionV1 getExistingCollection() {
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags() != null, "The displayed collection item to reference a valid entity and have a valid tags collection.");

                        return filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags();
                    }

                }, new AddPossibleChildCallback<RESTTagCollectionItemV1>() {

                    @Override
                    public void createAndAddChild(@NotNull final RESTTagCollectionItemV1 copy) {
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags() != null, "The displayed collection item to reference a valid entity and have a valid tags collection.");

                        @NotNull final RESTTagV1 newChild = new RESTTagV1();
                        newChild.setId(copy.getItem().getId());
                        newChild.setName(copy.getItem().getName());
                        filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags().addNewItem(newChild);
                    }

                }, new UpdateAfterChildModifiedCallback() {

                    @Override
                    public void updateAfterChildModified() {
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
                        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");

                        /*
                         * refresh the list of possible tags
                         */
                        tagComponent.redisplayPossibleChildList(filteredResultsComponent.getProviderData().getDisplayedItem().getItem());
                    }

                }
        );
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        queryString = removeHistoryToken(historyToken, HISTORY_TOKEN);
        if (!queryString.startsWith(Constants.QUERY_PATH_SEGMENT_PREFIX)) {
            queryString = Constants.QUERY_PATH_SEGMENT_PREFIX;
        }
    }

    @Override
    protected void loadAdditionalDisplayedItemData() {
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");

        /* Get a new collection of tags */
        tagComponent.refreshPossibleChildrenDataFromRESTAndRedisplayList(filteredResultsComponent.getProviderData().getDisplayedItem().getItem());
    }

    @Override
    protected void bindActionButtons() {
        /**
         * A click handler used to display the project fields view
         */
        @NotNull final ClickHandler projectDetailsClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                switchView(resultComponent.getDisplay());
            }

        };

        /**
         * A click handler used to display the project tags view
         */
        @NotNull final ClickHandler projectTagsClickHandler = new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                switchView(tagComponent.getDisplay());
            }

        };

        /**
         * A click handler used to save any changes to the project
         */
        @NotNull final ClickHandler saveClickHandler = new ClickHandler() {
            @Override
            public void onClick(@NotNull final ClickEvent event) {

                checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
                checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");
                checkState(filteredResultsComponent.getProviderData().getSelectedItem() != null, "There should be a selected collection item.");
                checkState(filteredResultsComponent.getProviderData().getSelectedItem().getItem() != null, "The selected collection item to reference a valid entity.");

                /* Was the tag we just saved a new tag? */
                final boolean wasNewEntity = filteredResultsComponent.getProviderData().getDisplayedItem().returnIsAddItem();

                /* Sync the UI to the underlying object */
                resultComponent.getDisplay().getDriver().flush();

                @NotNull final RESTCallback<RESTProjectV1> callback = new BaseRestCallback<RESTProjectV1, Display>(display,
                        new BaseRestCallback.SuccessAction<RESTProjectV1, Display>() {
                            @Override
                            public void doSuccessAction(@NotNull final RESTProjectV1 retValue, @NotNull final Display display) {
                                checkState(filteredResultsComponent.getProviderData().isValid(), "The filtered results provider data should be valid.");

                                retValue.cloneInto(filteredResultsComponent.getProviderData().getSelectedItem().getItem(), true);
                                retValue.cloneInto(filteredResultsComponent.getProviderData().getDisplayedItem().getItem(),
                                        true);

                                /* This project is no longer a new project */
                                filteredResultsComponent.getProviderData().getDisplayedItem().setState(RESTBaseCollectionItemV1.UNCHANGED_STATE);
                                filteredResultsComponent.getDisplay().getProvider().updateRowData(
                                        filteredResultsComponent.getProviderData().getStartRow(),
                                        filteredResultsComponent.getProviderData().getItems());

                                tagComponent.getDisplay().display(filteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false);
                                tagComponent.refreshPossibleChildrenDataFromRESTAndRedisplayList(filteredResultsComponent.getProviderData().getDisplayedItem().getItem());

                                updateDisplayWithNewEntityData(wasNewEntity);

                                Window.alert(PressGangCCMSUI.INSTANCE.SaveSuccess());
                            }
                        });

                if (filteredResultsComponent.getProviderData().getDisplayedItem() != null) {

                    if (hasUnsavedChanges()) {

                        final RESTProjectV1 project = new RESTProjectV1();
                        project.setId(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getId());
                        project.explicitSetName(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getName());
                        project.explicitSetDescription(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getDescription());
                        project.explicitSetTags(filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags());

                        if (wasNewEntity) {
                            RESTCalls.createProject(callback, project);
                        } else {
                            RESTCalls.saveProject(callback, project);
                        }
                    } else {
                        Window.alert(PressGangCCMSUI.INSTANCE.NoUnsavedChanges());
                    }
                }
            }
        };

        display.getDetails().addClickHandler(projectDetailsClickHandler);
        display.getChildren().addClickHandler(projectTagsClickHandler);
        display.getSave().addClickHandler(saveClickHandler);
    }

    private void doSearch(final boolean newWindow) {
        if (isOKToProceed()) {
            eventBus.fireEvent(new ProjectsFilteredResultsAndProjectViewEvent(filteredResultsComponent.getQuery(), newWindow));
        }
    }

    /**
     * Binds behaviour to the tag search and list view
     */
    @Override
    protected void bindFilteredResultsButtons() {
        filteredResultsComponent.getDisplay().getEntitySearch().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(@NotNull final ClickEvent event) {
               doSearch(GWTUtilities.isEventToOpenNewWindow(event));
            }
        });

        final KeyPressHandler searchKeyPressHandler = new KeyPressHandler() {
            @Override
            public void onKeyPress(@NotNull final KeyPressEvent event) {
                if (GWTUtilities.enterKeyWasPressed(event)) {
                    doSearch(false);
                }
            }
        };

        filteredResultsComponent.getDisplay().getDescriptionFilter().addKeyPressHandler(searchKeyPressHandler);
        filteredResultsComponent.getDisplay().getIdFilter().addKeyPressHandler(searchKeyPressHandler);
        filteredResultsComponent.getDisplay().getNameFilter().addKeyPressHandler(searchKeyPressHandler);

        filteredResultsComponent.getDisplay().getCreate().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(@NotNull final ClickEvent event) {

                /* The 'selected' tag will be blank. This gives us something to compare to when checking for unsaved changes */
                @NotNull final RESTProjectV1 selectedEntity = new RESTProjectV1();
                selectedEntity.setId(Constants.NULL_ID);
                @NotNull final RESTProjectCollectionItemV1 selectedTagWrapper = new RESTProjectCollectionItemV1(selectedEntity);

                /* The displayed tag will also be blank. This is the object that our data will be saved into */
                @NotNull final RESTProjectV1 displayedEntity = new RESTProjectV1();
                displayedEntity.setId(Constants.NULL_ID);
                displayedEntity.setTags(new RESTTagCollectionV1());
                @NotNull final RESTProjectCollectionItemV1 displayedTagWrapper = new RESTProjectCollectionItemV1(displayedEntity, RESTBaseCollectionItemV1.ADD_STATE);

                filteredResultsComponent.getProviderData().setSelectedItem(selectedTagWrapper);
                filteredResultsComponent.getProviderData().setDisplayedItem(displayedTagWrapper);

                initializeViews();

                switchView(lastDisplayedView == null ? resultComponent.getDisplay() : lastDisplayedView);

                tagComponent.refreshPossibleChildrenDataFromRESTAndRedisplayList(filteredResultsComponent.getProviderData().getDisplayedItem().getItem());
            }
        });
    }

    @Override
    public boolean hasUnsavedChanges() {
        /* sync the UI with the underlying tag */
        if (filteredResultsComponent.getProviderData().getDisplayedItem() != null) {
            resultComponent.getDisplay().getDriver().flush();

            return (unsavedProjectChanges() || unsavedTagChanges());
        }
        return false;
    }

    /**
     * Compare the selected and displayed project, and see if any of the fields have changed
     *
     * @return true if there are unsaved changes, false otherwise
     */
    private boolean unsavedProjectChanges() {
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");
        checkState(filteredResultsComponent.getProviderData().getSelectedItem() != null, "There should be a selected collection item.");
        checkState(filteredResultsComponent.getProviderData().getSelectedItem().getItem() != null, "The selected collection item to reference a valid entity.");

        return !(stringEqualsEquatingNullWithEmptyString(filteredResultsComponent.getProviderData().getSelectedItem().getItem()
                .getName(), filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getName()) && stringEqualsEquatingNullWithEmptyString(
                filteredResultsComponent.getProviderData().getSelectedItem().getItem().getDescription(),
                filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getDescription()));
    }

    /**
     * Check to see if there are any added, removed or modified tags in the project
     *
     * @return true if there are modified tags, false otherwise
     */
    private boolean unsavedTagChanges() {
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");

        /*
            We might be saving the entity before the tags have been loaded, so filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags()
            could be null.
         */
        return filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags() == null ||
                !filteredResultsComponent.getProviderData().getDisplayedItem().getItem().getTags().returnDeletedAddedAndUpdatedCollectionItems().isEmpty();
    }

    /**
     * Called when the selected tag is changed, or the selected view is changed.
     */
    @Override
    protected void afterSwitchView(@NotNull final BaseTemplateViewInterface displayedView) {

        this.enableAndDisableActionButtons(displayedView);
        setHelpTopicForView(displayedView);
    }

    private void enableAndDisableActionButtons(@NotNull final BaseTemplateViewInterface displayedView) {
        this.display.replaceTopActionButton(this.display.getChildrenDown(), this.display.getChildren());
        this.display.replaceTopActionButton(this.display.getDetailsDown(), this.display.getDetails());

        if (displayedView == this.resultComponent.getDisplay()) {
            this.display.replaceTopActionButton(this.display.getDetails(), this.display.getDetailsDown());
        } else if (displayedView == this.tagComponent.getDisplay()) {
            this.display.replaceTopActionButton(this.display.getChildren(), this.display.getChildrenDown());
        }
    }

    @Override
    protected void initializeViews(@Nullable final List<BaseTemplateViewInterface> filter) {

        checkState(filteredResultsComponent.getProviderData().getDisplayedItem() != null, "There should be a displayed collection item.");
        checkState(filteredResultsComponent.getProviderData().getDisplayedItem().getItem() != null, "The displayed collection item to reference a valid entity.");

        @NotNull final List<BaseCustomViewInterface<RESTProjectV1>> displayableViews = new ArrayList<BaseCustomViewInterface<RESTProjectV1>>();
        displayableViews.add(resultComponent.getDisplay());
        displayableViews.add(tagComponent.getDisplay());

        for (@NotNull final BaseCustomViewInterface<RESTProjectV1> view : displayableViews) {
            if (viewIsInFilter(filter, view)) {
                view.display(filteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false);
            }
        }

        if (viewIsInFilter(filter, tagComponent.getDisplay())) {
            tagComponent.displayChildrenExtended(filteredResultsComponent.getProviderData().getDisplayedItem().getItem(), false);
        }

    }

    private void setHelpTopicForView(@NotNull final BaseTemplateViewInterface view) {
        if (view == tagComponent.getDisplay()) {
            setHelpTopicId(tagComponent.getHelpTopicId());
        } else if (view == resultComponent.getDisplay()) {
            setHelpTopicId(resultComponent.getHelpTopicId());
        }
    }
}