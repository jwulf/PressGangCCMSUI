package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.propertytagcategory;

import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.HasData;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTPropertyTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTPropertyTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.join.RESTPropertyTagInPropertyCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.join.RESTPropertyTagInPropertyCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTPropertyCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.join.RESTPropertyTagInPropertyCategoryV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.children.BaseChildrenPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.children.BaseChildrenViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls.RESTCallback;
import org.jboss.pressgang.ccms.ui.client.local.sort.propertytag.RESTPropertyTagCollectionItemIDSort;
import org.jboss.pressgang.ccms.ui.client.local.sort.propertytag.RESTPropertyTagCollectionItemNameSort;
import org.jboss.pressgang.ccms.ui.client.local.sort.propertytag.RESTPropertyTagCollectionItemParentSort;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

/**
    The presenter used to display the property category tags.
 */
@Dependent
public class PropertyCategoryTagPresenter extends BaseChildrenPresenter<
        RESTPropertyCategoryV1,                                                                                                                         // The main REST types
        RESTPropertyTagCollectionItemV1,                                                                                                                // The possible children types
        RESTPropertyTagInPropertyCategoryV1, RESTPropertyTagInPropertyCategoryCollectionV1, RESTPropertyTagInPropertyCategoryCollectionItemV1>          // The existing children types
        implements BaseTemplatePresenterInterface {

    public static final String HISTORY_TOKEN = "PropertyCategoryView";
    @Nullable
    private Integer entityId;
    /**
     * The view that corresponds to this presenter.
     */
    @Inject
    private Display display;

    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        try {
            entityId = Integer.parseInt(GWTUtilities.removeHistoryToken(HISTORY_TOKEN, historyToken));
        } catch (@NotNull final NumberFormatException ex) {
            entityId = null;
        }
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN);
    }

    @Override
    public void bindChildrenExtended(final int topicId, @NotNull final String pageId) {
        super.bindChildren(topicId, pageId, display);
    }

    @Override
    public void displayChildrenExtended(@NotNull final RESTPropertyCategoryV1 parent, final boolean readOnly) {
        super.displayChildren(parent, readOnly);
        display.display(parent, readOnly);
    }

    @Override
    public void refreshPossibleChildrenDataFromRESTAndRedisplayList(@NotNull final RESTPropertyCategoryV1 parent) {

        @NotNull final RESTCallback<RESTPropertyTagCollectionV1> callback = new BaseRestCallback<RESTPropertyTagCollectionV1, PropertyCategoryTagPresenter.Display>(display,
                new BaseRestCallback.SuccessAction<RESTPropertyTagCollectionV1, PropertyCategoryTagPresenter.Display>() {
                    @Override
                    public void doSuccessAction(@NotNull final RESTPropertyTagCollectionV1 retValue, @NotNull final PropertyCategoryTagPresenter.Display display) {
                        checkArgument(retValue.getItems() != null, "Returned collection should have a valid items collection.");
                        checkArgument(retValue.getSize() != null, "Returned collection should have a valid size.");

                        /* Zero results can be a null list */
                        getPossibleChildrenProviderData().setStartRow(0);
                        getPossibleChildrenProviderData().setItems(retValue.getItems());
                        getPossibleChildrenProviderData().setSize(retValue.getItems().size());

                        /* Refresh the list */
                        redisplayPossibleChildList(parent);
                    }
                });

        getPossibleChildrenProviderData().reset();

        RESTCalls.getPropertyTags(callback);
    }

    @Override
    @NotNull
    public EnhancedAsyncDataProvider<RESTPropertyTagCollectionItemV1> generatePossibleChildrenProvider(@NotNull final RESTPropertyCategoryV1 parent) {

        @NotNull final EnhancedAsyncDataProvider<RESTPropertyTagCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTPropertyTagCollectionItemV1>() {
            @Override
            protected void onRangeChanged(@NotNull final HasData<RESTPropertyTagCollectionItemV1> data) {

                getPossibleChildrenProviderData().setStartRow(data.getVisibleRange().getStart());

                if (getPossibleChildrenProviderData().getItems() != null) {
                    /*
                        Implement sorting
                    */
                    final ColumnSortList sortList = display.getPossibleChildrenResults().getColumnSortList();
                    if (sortList.size() != 0) {
                        final Column<?, ?> column = sortList.get(0).getColumn();
                        final boolean ascending = sortList.get(0).isAscending();

                        /*
                            Sort the collection
                        */
                        if (column == display.getPossibleChildrenButtonColumn()) {
                            Collections.sort(getPossibleChildrenProviderData().getItems(), new RESTPropertyTagCollectionItemParentSort(parent, ascending));
                        } else if (column == display.getTagsIdColumn())  {
                            Collections.sort(getPossibleChildrenProviderData().getItems(), new RESTPropertyTagCollectionItemIDSort(ascending));
                        } else if (column == display.getTagsNameColumn())  {
                            Collections.sort(getPossibleChildrenProviderData().getItems(), new RESTPropertyTagCollectionItemNameSort(ascending));
                        }
                    }

                    displayNewFixedList(getPossibleChildrenProviderData().getItems());
                } else {
                    resetProvider();
                }

            }
        };
        return provider;
    }

    /**
     * The definition of the view that corresponds to this presenter.
     */
    public interface Display extends BaseChildrenViewInterface<
            RESTPropertyCategoryV1,                                                                                                                         // The main REST types
            RESTPropertyTagCollectionItemV1,                                                                                                                // The possible children types
            RESTPropertyTagInPropertyCategoryV1, RESTPropertyTagInPropertyCategoryCollectionV1, RESTPropertyTagInPropertyCategoryCollectionItemV1>          // The existing children types
    {
        /**
         *
         * @return The column that displays the id field.
         */
        TextColumn<RESTPropertyTagCollectionItemV1> getTagsIdColumn();

        /**
         *
         * @return The column that displays the name field.
         */
        TextColumn<RESTPropertyTagCollectionItemV1> getTagsNameColumn();
    }
}
