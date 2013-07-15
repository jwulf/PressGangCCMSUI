package org.jboss.pressgang.ccms.ui.client.local.ui.editor.search;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TriStatePushButton;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.search.SearchTagPresenter.Display.SearchPresenterDriver;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUITag;
import org.jetbrains.annotations.NotNull;

/**
 * A GWT Editor to provide a visual representation of SearchUITag
 *
 * @author Matthew Casperson
 */
public final class SearchUITagEditor implements Editor<SearchUITag> {
    private final SearchUICategoryEditor searchUICategory;

    private final Label name = new Label();
    private final TriStatePushButton state = new TriStatePushButton();
    private final TriStatePushButton bulkTagState = new TriStatePushButton();

    public SearchUITagEditor(@NotNull final SearchPresenterDriver driver, @NotNull final SearchUICategoryEditor searchUICategory) {
        this.searchUICategory = searchUICategory;

        name.addStyleName(CSSConstants.TagListTagView.TAG_LABEL);

        state.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(@NotNull final ClickEvent event) {
                driver.flush();
            }
        });
    }

    public TriStatePushButton stateEditor() {
        return state;
    }

    public TriStatePushButton bulkTagStateEditor() {
        return bulkTagState;
    }

    public Label nameEditor() {
        return name;
    }
}
