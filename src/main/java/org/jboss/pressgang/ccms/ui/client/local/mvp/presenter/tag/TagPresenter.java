package org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.tag;

import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.clearContainerAndAddTopLevelPanel;
import static org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities.removeHistoryToken;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.jboss.pressgang.ccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.collections.items.RESTTagCollectionItemV1;
import org.jboss.pressgang.ccms.rest.v1.entities.RESTTagV1;
import org.jboss.pressgang.ccms.ui.client.local.mvp.component.propertyview.BasePropertyViewComponentInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.base.TemplatePresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.editor.BaseEditorViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.tag.TagViewInterface;
import org.jboss.pressgang.ccms.ui.client.local.ui.editor.tagview.RESTTagV1BasicDetailsEditor;

import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.user.client.ui.HasWidgets;

@Dependent
public class TagPresenter implements TemplatePresenter {

    public static final String HISTORY_TOKEN = "TagView";

    // Empty interface declaration, similar to UiBinder
    public interface TagPresenterDriver extends SimpleBeanEditorDriver<RESTTagV1, RESTTagV1BasicDetailsEditor> {
    }

    public interface Display extends TagViewInterface, BaseEditorViewInterface<RESTTagV1, RESTTagCollectionV1, RESTTagCollectionItemV1> {

    }

    public interface LogicComponent extends BasePropertyViewComponentInterface<Display> {
        @Override
        void getEntity(final Integer tagId);
    }

    private Integer tagId;

    @Inject
    private Display display;

    @Inject
    private LogicComponent component;

    @Override
    public void parseToken(final String searchToken) {
        try {
            tagId = Integer.parseInt(removeHistoryToken(searchToken, HISTORY_TOKEN));
        } catch (final NumberFormatException ex) {
            tagId = null;
        }

    }

    @Override
    public void go(final HasWidgets container) {
        clearContainerAndAddTopLevelPanel(container, display);
        component.bind(display,  display);
        
        if (tagId != null)
            component.getEntity(tagId);
    }
}
