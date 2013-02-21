package org.jboss.pressgang.ccms.ui.client.local.ui.editor.integerconstant;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.*;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTIntegerConstantV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;

/**
 * An editor used to bind the integer constant's details to ui elements
 */
public class RESTIntegerConstantV1DetailsEditor extends Grid implements Editor<RESTIntegerConstantV1> {
    private static final int ROWS = 3;
    private static final int COLS = 2;

    private final SimpleIntegerBox id = new SimpleIntegerBox();
    private final TextBox name = new TextBox();
    private final SimpleIntegerBox value = new SimpleIntegerBox();

    public SimpleIntegerBox idEditor() {
        return id;
    }

    public TextBox nameEditor() {
        return name;
    }

    public SimpleIntegerBox valueEditor() {
        return value;
    }

    public RESTIntegerConstantV1DetailsEditor(final boolean readOnly) {
        super(ROWS, COLS);

        this.addStyleName(CSSConstants.IntegerConstantView.INTEGER_CONSTANT_VIEW_PANEL);
        id.addStyleName(CSSConstants.IntegerConstantView.INTEGER_CONSTANT_VIEW_ID_FIELD);
        name.addStyleName(CSSConstants.IntegerConstantView.INTEGER_CONSTANT_VIEW_NAME_FIELD);
        value.addStyleName(CSSConstants.IntegerConstantView.INTEGER_CONSTANT_VIEW_VALUE_FIELD);

        id.setReadOnly(true);

        final Label idLabel = new Label(PressGangCCMSUI.INSTANCE.IntegerConstantId());
        this.setWidget(0, 0, idLabel);
        this.setWidget(0, 1, id);

        final Label nameLabel = new Label(PressGangCCMSUI.INSTANCE.IntegerConstantName());
        this.setWidget(1, 0, nameLabel);
        this.setWidget(1, 1, name);

        final Label valueLabel = new Label(PressGangCCMSUI.INSTANCE.IntegerConstantValue());
        this.setWidget(2, 0, valueLabel);
        this.setWidget(2, 1, value);

        for (int i = 0; i < ROWS; ++i) {
            this.getCellFormatter().addStyleName(i, 0, CSSConstants.TOPIC_VIEW_LABEL);
        }

        for (int i = 0; i < ROWS; ++i) {
            this.getCellFormatter().addStyleName(i, 1, CSSConstants.TOPIC_VIEW_DETAIL);
        }
    }

}