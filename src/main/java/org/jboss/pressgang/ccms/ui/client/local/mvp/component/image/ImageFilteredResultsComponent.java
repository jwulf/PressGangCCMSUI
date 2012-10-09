package org.jboss.pressgang.ccms.ui.client.local.mvp.component.image;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTImageCollectionItemV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.component.base.ComponentBase;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.image.ImageFilteredResultsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.ui.ProviderUpdateData;
import org.jboss.pressgang.ccms.ui.client.local.utilities.EnhancedAsyncDataProvider;

import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.HasData;

public class ImageFilteredResultsComponent extends ComponentBase<ImageFilteredResultsPresenter.Display> implements ImageFilteredResultsPresenter.LogicComponent{
   
    
    /**
     * Holds the data required to populate and refresh the categories list
     */
    private ProviderUpdateData<RESTImageCollectionItemV1> providerData = new ProviderUpdateData<RESTImageCollectionItemV1>();
    
    public ProviderUpdateData<RESTImageCollectionItemV1> getProviderData() {
        return providerData;
    }

    public void setProviderData(ProviderUpdateData<RESTImageCollectionItemV1> providerData) {
        this.providerData = providerData;
    }
    
    public void bind(final String queryString, final ImageFilteredResultsPresenter.Display display, final BaseTemplateViewInterface waitDisplay)
    {
        super.bind(display,  waitDisplay);
        display.setProvider(generateListProvider(queryString));
    }

    /**
     * @return A provider to be used for the image display list.
     */
    private EnhancedAsyncDataProvider<RESTImageCollectionItemV1> generateListProvider(final String queryString) {
        final EnhancedAsyncDataProvider<RESTImageCollectionItemV1> provider = new EnhancedAsyncDataProvider<RESTImageCollectionItemV1>() {
            @Override
            protected void onRangeChanged(final HasData<RESTImageCollectionItemV1> item) {
                providerData.setStartRow(item.getVisibleRange().getStart());
                final int length = item.getVisibleRange().getLength();
                final int end = providerData.getStartRow() + length;

                final RESTCalls.RESTCallback<RESTImageCollectionV1> callback = new RESTCalls.RESTCallback<RESTImageCollectionV1>() {
                    @Override
                    public void begin() {
                        display.addWaitOperation();
                    }

                    @Override
                    public void generalException(final Exception e) {
                        Window.alert(PressGangCCMSUI.INSTANCE.ErrorGettingTopics());
                        display.removeWaitOperation();
                    }

                    @Override
                    public void success(final RESTImageCollectionV1 retValue) {
                        try {
                            providerData.setItems(retValue.getItems());
                            displayAsynchronousList(providerData.getItems(), providerData.getStartRow(), retValue.getSize());
                        } finally {
                            display.removeWaitOperation();
                        }
                    }

                    @Override
                    public void failed() {
                        display.removeWaitOperation();
                        Window.alert(PressGangCCMSUI.INSTANCE.ConnectionError());
                    }
                };

                RESTCalls.getImagesFromQuery(callback, queryString, providerData.getStartRow(), end);
            }
        };
        return provider;
    }
}
