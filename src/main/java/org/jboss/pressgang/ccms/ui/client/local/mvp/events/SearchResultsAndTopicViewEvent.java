package org.jboss.pressgang.ccms.ui.client.local.mvp.events;

public class SearchResultsAndTopicViewEvent extends ViewOpenWithQueryEvent<ViewOpenWithQueryEventHandler> {
    public static final Type<ViewOpenWithQueryEventHandler> TYPE = new Type<ViewOpenWithQueryEventHandler>();

    public SearchResultsAndTopicViewEvent(final String query, final boolean newWindow) {
        super(query, newWindow);
    }

    @Override
    public Type<ViewOpenWithQueryEventHandler> getAssociatedType() {
        return TYPE;
    }
}
