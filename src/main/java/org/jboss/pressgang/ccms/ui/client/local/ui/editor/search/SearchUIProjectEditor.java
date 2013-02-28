package org.jboss.pressgang.ccms.ui.client.local.ui.editor.search;

import com.google.gwt.dom.client.Style;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.adapters.EditorSource;
import com.google.gwt.editor.client.adapters.ListEditor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FourTextAndImageButtonSearchUIProjectEditor;
import com.google.gwt.user.client.ui.ScrollPanel;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.search.SearchPresenter.Display.SearchPresenterDriver;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUICategory;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUIProject;
import org.jboss.pressgang.ccms.ui.client.local.ui.search.tag.SearchUIProjects;
import org.jetbrains.annotations.NotNull;

public final class SearchUIProjectEditor extends DockLayoutPanel implements ValueAwareEditor<SearchUIProject> {
    private static final int BUTTON_COLUMN_WIDTH = 16;

    private final SearchPresenterDriver driver;
    private final SearchUIProjects searchUIProjects;
    private SearchUIProject value;

    final FourTextAndImageButtonSearchUIProjectEditor summary = new FourTextAndImageButtonSearchUIProjectEditor();
    final ListEditor<SearchUICategory, SearchUICategoryEditor> categories = ListEditor.of(new SearchUICategoryEditorSource());
    private final FlexTable categoriesButtonPanel = new FlexTable();
    private final ScrollPanel scroll = new ScrollPanel();

    @NotNull
    public SearchUIProject getValue() {
        return value;
    }

    /**
     * The EditorSource is used to create and orgainse the Editors that go into a ListEditor
     *
     * @author Matthew Casperson
     */
    private class SearchUICategoryEditorSource extends EditorSource<SearchUICategoryEditor> {
        @Override
        public SearchUICategoryEditor create(final int index) {
            final SearchUICategoryEditor subEditor = new SearchUICategoryEditor(SearchUIProjectEditor.this.driver, SearchUIProjectEditor.this);

            SearchUIProjectEditor.this.categoriesButtonPanel.setWidget(index, 0, subEditor.summary);

            subEditor.summary.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(final ClickEvent event) {
                    if (SearchUIProjectEditor.this.getCenter() != null) {
                        SearchUIProjectEditor.this.remove(SearchUIProjectEditor.this.getCenter());
                    }
                    SearchUIProjectEditor.this.add(subEditor);

                    /* Untoggle the other buttons */
                    for (final SearchUICategoryEditor editor : categories.getEditors()) {
                        if (editor.summary != subEditor.summary) {
                            editor.summary.removeStyleName(CSSConstants.Common.CUSTOM_BUTTON_DOWN);
                            editor.summary.removeStyleName(CSSConstants.Common.CUSTOM_BUTTON);

                            editor.summary.addStyleName(CSSConstants.Common.CUSTOM_BUTTON);
                        }
                    }
                }
            });

            return subEditor;
        }

        @Override
        public void dispose(final SearchUICategoryEditor subEditor) {
            subEditor.summary.removeFromParent();
            subEditor.removeFromParent();
        }

        @Override
        public void setIndex(final SearchUICategoryEditor subEditor, final int index) {
            SearchUIProjectEditor.this.categoriesButtonPanel.setWidget(index, 0, subEditor);
        }
    }

    public SearchUIProjectEditor(final SearchPresenterDriver driver, final SearchUIProjects searchUIProjects) {
        super(Style.Unit.EM);

        this.driver = driver;
        this.searchUIProjects = searchUIProjects;

        this.addStyleName(CSSConstants.TagListCategoryView.CATEGORIES_LAYOUT);
        summary.addStyleName(CSSConstants.Common.CUSTOM_BUTTON);

        categoriesButtonPanel.addStyleName(CSSConstants.TagListCategoryView.CATEGORIES_BUTTONS_LAYOUT);
        scroll.addStyleName(CSSConstants.TagListCategoryView.CATEGORIES_SCROLL_PANEL);

        scroll.setWidget(categoriesButtonPanel);
        this.addWest(scroll, BUTTON_COLUMN_WIDTH);

        summary.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(final ClickEvent event) {
                summary.removeStyleName(CSSConstants.Common.CUSTOM_BUTTON);
                summary.addStyleName(CSSConstants.Common.CUSTOM_BUTTON_DOWN);
            }
        });
    }

    @Override
    public void setDelegate(final EditorDelegate<SearchUIProject> delegate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        this.summary.asEditor().setValue(value.getSummary());
    }

    @Override
    public void onPropertyChange(final String... paths) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setValue(final SearchUIProject value) {
        this.value = value;
    }
}
