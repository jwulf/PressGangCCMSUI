package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.filteredresults;

import com.google.gwt.event.shared.HandlerManager;
import org.jboss.pressgang.ccms.rest.v1.collections.base.RESTBaseCollectionItemV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.dataevents.EntityListReceived;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.dataevents.EntityListReceivedHandler;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.filteredresults.BaseFilteredResultsViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkState;

/**
 * @see BaseFilteredResultsPresenterInterface
 */
abstract public class BaseFilteredResultsPresenter<V extends RESTBaseCollectionItemV1<?, ?, ?>>
        extends BaseTemplatePresenter implements BaseFilteredResultsPresenterInterface<V> {

    /**
     * A Logger
     */
    private static final Logger LOGGER = Logger.getLogger(BaseFilteredResultsPresenter.class.getName());

    /**
     * Holds the data required to populate and refresh the tags list
     */
    private final ProviderUpdateData<V> providerData = new ProviderUpdateData<V>();

    /**
     * Used to distribute events, such as when the list of entities has been loaded.
     */
    final private HandlerManager handlerManager = new HandlerManager(this);

    /**
     * Manages event registration and notification.
     */
    @NotNull
    public HandlerManager getHandlerManager() {
        return handlerManager;
    }

    @Override
    @NotNull
    public ProviderUpdateData<V> getProviderData() {
        return providerData;
    }

    /**
     * Adds an event handler to listen for the entity list loaded event.
     *
     * @param handler The event handler
     */
    public void addTopicListReceivedHandler(@NotNull final EntityListReceivedHandler handler) {
        try {
            LOGGER.log(Level.INFO, "ENTER BaseFilteredResultsPresenter.addTopicListReceivedHandler()");
            handlerManager.addHandler(EntityListReceived.getType(), handler);
        } finally {
            LOGGER.log(Level.INFO, "EXIT BaseFilteredResultsPresenter.addTopicListReceivedHandler()");
        }
    }

    /**
     * @param topicId     The ID of the help topic associated with this view
     * @param pageId      The history token associated with this view
     * @param queryString The query that defines the results to be displayed
     * @param display     The filtered results view
     */
    protected void bindFilteredResults(final int topicId, @NotNull final String pageId, @NotNull final String queryString, @NotNull final BaseFilteredResultsViewInterface display) {
        super.bind(topicId, pageId, display);
        displayQueryElements(queryString);
    }

    /**
     * An empty implementation. Extending classes should use bindExtendedFilteredResults.
     */
    public void bindExtended(final int topicId, @NotNull final String pageId) {
        throw new UnsupportedOperationException("bindExtended() is not supported. Use bindFilteredResults() instead.");
    }

    /**
     * When a new entity is created, the filtered results are reloaded. This process breaks the link between the selected item
     * and the collection being displayed by the filtered results. This methods will go through and set the selected item to the
     * item in the filtered results list (if it exists).
     */
    protected void relinkSelectedItem() {
        checkState(providerData != null, "The providerData variable should have been set.");

        if (this.providerData.getSelectedItem() != null && this.providerData.getItems() != null) {
            for (@NotNull final V filteredResultEntity : this.providerData.getItems()) {

                checkState(this.providerData.getSelectedItem() != null, "There has to be a selected item");
                checkState(this.providerData.getSelectedItem().getItem() != null, "The selected item needs to have reference a valid entity");
                checkState(this.providerData.getSelectedItem().getItem().getId() != null, "The entity collection item needs to have a valid entity with a valid id");

                if (filteredResultEntity.getItem().getId().equals(this.providerData.getSelectedItem().getItem().getId())) {
                    this.providerData.setSelectedItem(filteredResultEntity);
                    break;
                }
            }
        }
    }

    /**
     * Display the current filter options.
     *
     * @param queryString The string that contains the filter options
     */
    abstract protected void displayQueryElements(@NotNull final String queryString);

    /**
     * @param queryString The query string passed to the rest interface
     * @param waitDisplay The main view used to notify the user that an ongoing operation is in progress
     * @return A provider to be used for the category display list
     */
    @Nullable
    abstract protected EnhancedAsyncDataProvider<V> generateListProvider(@NotNull final String queryString, @NotNull final BaseTemplateViewInterface waitDisplay);
}
