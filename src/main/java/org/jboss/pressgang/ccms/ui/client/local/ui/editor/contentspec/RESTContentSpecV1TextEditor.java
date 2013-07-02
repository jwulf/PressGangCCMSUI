package org.jboss.pressgang.ccms.ui.client.local.ui.editor.contentspec;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.user.client.ui.SimplePanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import org.jboss.pressgang.ccms.rest.v1.entities.contentspec.RESTContentSpecV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;

public final class RESTContentSpecV1TextEditor extends SimplePanel implements Editor<RESTContentSpecV1> {
    /**
     * Ace builds from the 17th December 2012 and prior use absolute positioning, and require
     * that the AceEditor being constructed with true. After the 17th December 2012 the ACE
     * editor uses relative positioning, and the AceEditor needs to be constructed with false.
     */
    public final AceEditor text = new AceEditor(false);

    public RESTContentSpecV1TextEditor(final boolean readOnly) {
        this.addStyleName(CSSConstants.TopicView.TOPIC_XML_VIEW_ACE_PANEL);
        text.addStyleName(CSSConstants.TopicView.TOPIC_XML_VIEW_XML_FIELD);

        text.setReadOnly(readOnly);
        text.setMode(AceEditorMode.TEXT);
        text.setTheme(AceEditorTheme.ECLIPSE);

        this.setWidget(text);
    }
}
