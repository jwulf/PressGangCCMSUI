package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.contentspec;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.view.client.HasData;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTopicCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseCustomViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkArgument;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

@Dependent
public class ContentSpecRevisionsPresenter extends BaseTemplatePresenter {

    public interface Display extends BaseTemplateViewInterface, BaseCustomViewInterface<RESTContentSpecV1> {

        @NotNull EnhancedAsyncDataProvider<RESTContentSpecCollectionItemV1> getProvider();

        void setProvider( @NotNull final EnhancedAsyncDataProvider<RESTContentSpecCollectionItemV1> provider);

        @NotNull CellTable<RESTContentSpecCollectionItemV1> getResults();

        @NotNull SimplePager getPager();

        @NotNull  Column<RESTContentSpecCollectionItemV1, String> getViewButton();

        @NotNull Column<RESTContentSpecCollectionItemV1, String> getDiffButton();

        /**
         * @return The currently selected revision content spec.
         */
        @Nullable RESTContentSpecV1 getRevisionContentSpec();

        /**
         * @param revisionTopic The currently selected revision content spec.
         */
        void setRevisionContentSpec(@Nullable RESTContentSpecV1 revisionTopic);
    }

    /**
     * History token
     */
    public static final String HISTORY_TOKEN = "ContentSpecHistoryView";

    private String contentSpec;

    @Inject
    private Display display;

    /**
     * Holds the data required to populate and refresh the tags list
     */
    private final ProviderUpdateData<RESTContentSpecCollectionItemV1> providerData = new ProviderUpdateData<RESTContentSpecCollectionItemV1>();

    @NotNull
    public final ProviderUpdateData<RESTContentSpecCollectionItemV1> getProviderData() {
        return providerData;
    }


    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN);
    }

    public void bindExtended(final int topicId, @NotNull final String pageId) {
        super.bind(topicId, pageId, display);
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {

    }

    public EnhancedAsyncDataProvider<RESTContentSpecCollectionItemV1> generateListProvider(@NotNull final Integer id, @NotNull final BaseTemplateViewInterface waitDisplay) {

        getProviderData().reset();

        final EnhancedAsyncDataProvider<RESTContentSpecCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTContentSpecCollectionItemV1>() {
            @Override
            protected void onRangeChanged(@NotNull final HasData<RESTContentSpecCollectionItemV1> list) {

                final BaseRestCallback<RESTContentSpecV1, Display> callback = new BaseRestCallback<RESTContentSpecV1, Display>(
                        display,
                        new BaseRestCallback.SuccessAction<RESTContentSpecV1, Display>() {
                            @Override
                            public void doSuccessAction(@NotNull final RESTContentSpecV1 retValue, @NotNull final Display display) {
                                checkArgument(retValue.getRevisions().getItems() != null, "Returned collection should have a valid items collection.");
                                checkArgument(retValue.getRevisions().getSize() != null, "Returned collection should have a valid size.");

                                if (retValue.getRevisions().getItems().size() != 0) {
                                    checkArgument(retValue.getRevisions().getItems().get(0).getItem().getProperties() != null, "Returned collection should include items with a valid properties collection.");
                                }

                                getProviderData().setItems(retValue.getRevisions().getItems());
                                getProviderData().setSize(retValue.getRevisions().getSize());
                                displayAsynchronousList(getProviderData().getItems(), getProviderData().getSize(), getProviderData().getStartRow());
                            }
                    });

                final int start = list.getVisibleRange().getStart();
                getProviderData().setStartRow(start);
                final int length = list.getVisibleRange().getLength();
                final int end = start + length;

                RESTCalls.getContentSpecWithRevisions(callback, id, start, end);
            }
        };
        return provider;
    }
}