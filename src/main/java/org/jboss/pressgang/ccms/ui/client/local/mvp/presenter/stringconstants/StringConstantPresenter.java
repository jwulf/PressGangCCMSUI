package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.stringconstants;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BasePopulatedEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.stringconstant.RESTStringConstantV1DetailsEditor;
import org.jetbrains.annotations.NotNull;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;

/**
    The presenter used to display the string constants details.
 */
@Dependent
public class StringConstantPresenter extends BaseTemplatePresenter {

    public static final String HISTORY_TOKEN = "StringConstantView";

    /**
     * The view that corresponds to this presenter.
     */
    @Inject
    private Display display;

    /**
     * @return The view that corresponds to this presenter.
     */
    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {

    }

    @Override
    public void bindExtended(final int topicId, @NotNull final String pageId) {
        super.bind(topicId, pageId, display);
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN);
    }

    @Override
    public void close() {

    }

    // Empty interface declaration, similar to UiBinder
    public interface StringConstantPresenterDriver extends SimpleBeanEditorDriver<RESTStringConstantV1, RESTStringConstantV1DetailsEditor> {
    }

    /**
     * The definition of the view that corresponds to this presenter.
     */
    public interface Display extends BasePopulatedEditorViewInterface<RESTStringConstantV1, RESTStringConstantV1, RESTStringConstantV1DetailsEditor> {

    }
}
