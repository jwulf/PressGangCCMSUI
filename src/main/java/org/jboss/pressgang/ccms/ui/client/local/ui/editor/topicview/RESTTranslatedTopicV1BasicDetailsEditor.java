package org.jboss.pressgang.ccms.ui.client.local.ui.editor.topicview;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.*;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTranslatedTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;

/**
 * An editor to bind the details in a RESTTranslatedTopicV1 to UI elements. This is similar but not the same
 * as RESTTopicV1BasicDetailsEditor, which has some slightly different properties being displayed.
 */
public final class RESTTranslatedTopicV1BasicDetailsEditor extends Grid implements Editor<RESTTranslatedTopicV1> {

    private static final int ROWS = 4;
    private static final int COLS = 2;

    private final SimpleIntegerBox id = new SimpleIntegerBox();
    private final SimpleIntegerBox topicRevision = new SimpleIntegerBox();
    private final SimpleIntegerBox topicId = new SimpleIntegerBox();
    private final TextBox topicTitle = new TextBox();
    private final TextBox locale = new TextBox();

    public TextBox localeEditor() {
        return locale;
    }

    public SimpleIntegerBox topicIdEditor() {
        return topicId;
    }

    public SimpleIntegerBox topicRevisionEditor() {
        return topicRevision;
    }

    public TextBox topicTitleEditor() {
        return topicTitle;
    }

    public SimpleIntegerBox idEditor() {
        return id;
    }

    public RESTTranslatedTopicV1BasicDetailsEditor(final boolean readOnly) {
        super(ROWS, COLS);

        this.addStyleName(CSSConstants.TOPIC_VIEW_PANEL);

        topicId.setReadOnly(readOnly);
        topicRevision.setReadOnly(readOnly);
        locale.setReadOnly(readOnly);
        id.setReadOnly(true);


        int row = 0;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TranslatedTopicID()));
        this.setWidget(row, 1, id);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicID()));
        this.setWidget(row, 1, topicId);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicRevision()));
        this.setWidget(row, 1, topicRevision);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicTitle()));
        this.setWidget(row, 1, topicTitle);

        ++row;
        this.setWidget(row, 0, new Label(PressGangCCMSUI.INSTANCE.TopicLocale()));
        this.setWidget(row, 1, locale);

        for (int i = 0; i < ROWS; ++i) {
            this.getCellFormatter().addStyleName(i, 0, CSSConstants.TOPIC_VIEW_LABEL);
        }

        for (int i = 0; i < ROWS; ++i) {
            this.getCellFormatter().addStyleName(i, 1, CSSConstants.TOPIC_VIEW_DETAIL);
        }

    }
}