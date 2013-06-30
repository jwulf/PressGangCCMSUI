package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic;

import com.google.gwt.user.client.ui.HasWidgets;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.wrapper.IntegerWrapper;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.constants.ServiceConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.BaseTemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseCustomViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

@Dependent
public class TopicRenderedPresenter extends BaseTemplatePresenter {
    public static final String HISTORY_TOKEN = "TopicRenderedView";

    public interface Display extends BaseTemplateViewInterface, BaseCustomViewInterface<RESTBaseTopicV1<?, ?, ?>> {
        boolean displayTopicRendered(final Integer topicXMLHoldID, final boolean readOnly, final boolean showImages);

        void clear();

        void removeListener();
    }

    /**
     * The rendered topic view display
     */
    @Inject
    private Display display;

    @NotNull
    public Display getDisplay() {
        return display;
    }

    @Override
    public void go(@NotNull final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        bindExtended(ServiceConstants.DEFAULT_HELP_TOPIC, HISTORY_TOKEN);
    }

    @Override
    public void close() {
        display.removeListener();
    }

    public void bindExtended(final int topicId, @NotNull final String pageId) {
        super.bind(topicId, pageId, display);
    }

    public void displayTopicRendered(@Nullable final String topicXML, final boolean readOnly, final boolean showImages) {
        final BaseRestCallback<IntegerWrapper, Display> callback = new BaseRestCallback<IntegerWrapper, Display>(display,
                new BaseRestCallback.SuccessAction<IntegerWrapper, Display>() {
                    @Override
                    public void doSuccessAction(@NotNull final IntegerWrapper retValue, @NotNull final Display display) {
                        display.displayTopicRendered(retValue.value, readOnly, showImages);
                    }
                }, true);

        RESTCalls.holdXml(callback, (showImages ? Constants.DOCBOOK_XSL_REFERENCE : Constants.DOCBOOK_PLACEHOLDER_XSL_REFERENCE) + "\n" + topicXML);
    }

    @Override
    public void parseToken(@NotNull final String historyToken) {
        final String fixedToken = removeHistoryToken(historyToken, HISTORY_TOKEN);
        try {
            final Integer topicId = Integer.parseInt(fixedToken);

            final BaseRestCallback<RESTTopicV1, Display> callback = new BaseRestCallback<RESTTopicV1, Display>(display, new BaseRestCallback.SuccessAction<RESTTopicV1, Display>() {
                @Override
                public void doSuccessAction(@NotNull final RESTTopicV1 retValue, @NotNull final Display display) {
                    displayTopicRendered(retValue.getXml(), true, true);
                }
            });

            RESTCalls.getTopic(callback, topicId);

        } catch (@NotNull final NumberFormatException ex) {

        }
    }
}
