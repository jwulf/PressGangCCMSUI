package org.jboss.pressgang.ccms.ui.client.local.mvp.view.contentspec;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTCategoryCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.RESTContentSpecCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.contentspec.items.RESTContentSpecCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTCategoryCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTCategoryV1;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.contentspec.ContentSpecFilteredResultsAndDetailsPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.searchandedit.BaseSearchAndEditView;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.LogMessageView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.ui.UIUtilities;
import org.jetbrains.annotations.NotNull;

/**

 */
public class ContentSpecFilteredResultsAndDetailsView extends
        BaseSearchAndEditView<RESTContentSpecV1, RESTContentSpecCollectionV1, RESTContentSpecCollectionItemV1> implements
        ContentSpecFilteredResultsAndDetailsPresenter.Display {

    private final PushButton save = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Save());
    private final PushButton details = UIUtilities.createTopTabPushButton(PressGangCCMSUI.INSTANCE.CategoryDetails());
    private final PushButton text = UIUtilities.createTopTabPushButton(PressGangCCMSUI.INSTANCE.CategoryTags());

    private final Label detailsDown = UIUtilities.createTopTabDownLabel(PressGangCCMSUI.INSTANCE.CategoryDetails());
    private final Label textDown = UIUtilities.createTopTabDownLabel(PressGangCCMSUI.INSTANCE.CategoryTags());

    /**
     * An instance of the message log dialog box
     */
    private final LogMessageView messageLogDialog = new LogMessageView();

    @NotNull
    @Override
    public PushButton getText() {
        return text;
    }

    @NotNull
    @Override
    public PushButton getDetails() {
        return details;
    }

    @NotNull
    @Override
    public PushButton getSave() {
        return save;
    }

    @NotNull
    @Override
    public Label getTextDown() {
        return textDown;
    }

    @NotNull
    @Override
    public Label getDetailsDown() {
        return detailsDown;
    }

    @NotNull
    @Override
    public LogMessageView getMessageLogDialog() {
        return messageLogDialog;
    }

    public ContentSpecFilteredResultsAndDetailsView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.ContentSpecifications());
        populateTopActionBar();
        addSpacerToShortcutPanels();
        super.initialize(true);
    }


    private void populateTopActionBar() {
        this.addActionButton(this.getText());
        this.addActionButton(this.getDetails());
        this.addActionButton(this.getSave());
    }
}
