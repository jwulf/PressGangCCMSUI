package org.jboss.pressgang.ccms.ui.client.local.mvp.view;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.WelcomePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.resources.xsl.DocbookToHTML;
import org.jetbrains.annotations.NotNull;

public class WelcomeView extends BaseTemplateView implements WelcomePresenter.Display {

    private final Frame content = new Frame();

    public WelcomeView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.Welcome());

        this.getPanel().setWidget(content);

        content.addStyleName(CSSConstants.WelcomeView.WELCOME_VIEW_IFRAME);
    }

    @Override
    public void displayTopicRendered(@NotNull final Integer topicXMLHoldID) {
        content.setUrl(Constants.REST_SERVER + Constants.ECHO_ENDPOINT + "?id=" + topicXMLHoldID);
    }
}
