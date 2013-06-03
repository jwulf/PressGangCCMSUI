package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.search;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PushButton;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTFilterV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenterInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BasePopulatedEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.search.SearchFieldEditor;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.field.SearchUIFields;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

@Dependent
public class SearchFieldPresenter extends BaseTemplatePresenter implements BaseTemplatePresenterInterface {

    public static final String HISTORY_TOKEN = "SearchFieldView";

    public interface Display extends BaseTemplateViewInterface, BasePopulatedEditorViewInterface<RESTFilterV1, SearchUIFields, SearchFieldEditor> {
        interface SearchFieldPresenterDriver extends SimpleBeanEditorDriver<SearchUIFields, SearchFieldEditor> {
        }

        PushButton getSearchTopics();

        PushButton getTagsSearch();

        SearchUIFields getSearchUIFields();

        PushButton getFilters();

        PushButton getLocales();
    }

    @Inject
    private Display display;

    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        GWTUtilities.clearContainerAndAddTopLevelPanel(container, display);
        bindExtended(ServiceConstants.SEARCH_FIELDS_HELP_TOPIC, HISTORY_TOKEN);
    }

    @Override
    public void close() {

    }


    public void bindExtended(final int helpTopicId, @NotNull final String pageId) {
        bind(helpTopicId, pageId, display);
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {

    }

}
