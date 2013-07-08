package org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic;


import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseTopicV1;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * This view maintains a kind of double buffer. IFrames are loaded into hidden table cells, and then
 * "flipped" out after a period of time to give a seamless appearance of a panel being updated.
 */
public class TopicRenderedView extends BaseTemplateView implements TopicRenderedPresenter.Display {

    private static final Logger LOGGER = Logger.getLogger(TopicRenderedView.class.getName());
    private static final String LOADING_IFRAME = "LoadingIFrame";
    private static final String LOADED_IFRAME = "LoadedIFrame";
    private static final String HIDDEN_IFRAME = "HiddenIFrame";
    private final FlexTable flexTable = new FlexTable();
    private int displayingRow = 0;
    private JavaScriptObject listener;

    private Frame loadingiframe;
    private Frame loadediframe;

    /**
     * The GWT scrolling functions don't work in Firefox in a window that contains
     * XSL transformed into HTML. So we use native code to get access to the scroll
     * position of the default view. Tested in Chrome and Firefox.
     *
     * @param id The iframe id
     * @return The current horizontal scroll position
     */
    private native int getScrollX(@NotNull final String id)  /*-{
		try {
			var iframe = $doc.getElementById(id);
			if (iframe != null &&
				iframe.contentWindow != null) {
				return iframe.contentWindow.pageXOffset;
			}
		} catch (exception) {
			// probably a cross domain violation
		}
		return 0;
	}-*/;

    /**
     * The GWT scrolling functions don't work in Firefox in a window that contains
     * XSL transformed into HTML. So we use native code to get access to the scroll
     * position of the default view. Tested in Chrome and Firefox.
     *
     * @param id The iframe id
     * @return The current vertical scroll position
     */
    private native int getScrollY(@NotNull final String id)  /*-{
		try {
			var iframe = $doc.getElementById(id);
			if (iframe != null &&
				iframe.contentWindow != null) {
				return iframe.contentWindow.document.defaultView.pageYOffset;
			}
		} catch (exception) {
			// probably a cross domain violation
		}
		return 0;
	}-*/;

    /**
     * The GWT scrolling functions don't work in Firefox in a window that contains
     * XSL transformed into HTML. So we use native code to get access to the scroll
     * position of the default view. Tested in Chrome and Firefox.
     *
     * @param id         The iframe id
     * @param scrollLeft The horizontal scroll position
     * @param scrollTop  The vertical scroll position
     */
    private native void setScroll(@NotNull final String id, final int scrollLeft, final int scrollTop) /*-{
		try {
			var iframe = $doc.getElementById(id);
			if (iframe != null &&
				iframe.contentWindow != null) {
				iframe.contentWindow.document.defaultView.scrollTo(scrollLeft, scrollTop);
			}
		} catch (exception) {
			// probably a cross domain violation
		}
	}-*/;

    public TopicRenderedView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.SearchResults() + " - "
                + PressGangCCMSUI.INSTANCE.RenderedView());
        this.getPanel().setWidget(flexTable);
        flexTable.addStyleName(CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE);
        flexTable.getFlexCellFormatter().addStyleName(0, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL);
        flexTable.getFlexCellFormatter().addStyleName(1, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL);

        createEventListener();
        addEventListener();

        LOGGER.info("ENTER TopicRenderedView()");
    }

    /**
     * The listener hold a reference to this, which will prevent it from being reclaimed by the GC.
     * So here we remove the listener.
     */
    public native void removeListener() /*-{
		if (this.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::listener != null) {
			$wnd.removeEventListener('message', this.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::listener);
			this.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::listener = null;
		}
	}-*/;

    private native void createEventListener() /*-{
		this.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::listener =
			function (me) {
				return function displayAfterLoaded(event) {
					if (event.data == 'loaded') {
						me.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::displayRendered()();
					}
				};
			}(this);
	}-*/;

    private native void addEventListener() /*-{
		$wnd.addEventListener('message', this.@org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic.TopicRenderedView::listener);
	}-*/;

    private void displayRendered() {
        if (loadingiframe != null) {
            final int next = displayingRow == 0 ? 1 : 0;

            /*
                Maintain the scroll position. This will fail if the iframe is not in the same domain.
                We have to get these values before the iframe is hidden, otherwise Firefox will
                lose the scroll position.
             */
            final int scrollX = getScrollX(LOADED_IFRAME);
            final int scrollY = getScrollY(LOADED_IFRAME);

            /*
                Hide the outgoing iframe, and display the incoming one.
            */
            if (Arrays.asList(flexTable.getFlexCellFormatter().getStyleName(displayingRow, 0).split(" ")).indexOf(CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL) != -1) {
                flexTable.getFlexCellFormatter().removeStyleName(displayingRow, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL);
                flexTable.getFlexCellFormatter().addStyleName(displayingRow, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL);
            }

            if (Arrays.asList(flexTable.getFlexCellFormatter().getStyleName(next, 0).split(" ")).indexOf(CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL)  != -1) {
                flexTable.getFlexCellFormatter().removeStyleName(next, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL);
                flexTable.getFlexCellFormatter().addStyleName(next, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL);
            }

            setScroll(LOADING_IFRAME, scrollX, scrollY);

            if (loadediframe != null) {
                loadediframe.getElement().setId(HIDDEN_IFRAME);
            }

            loadediframe = loadingiframe;
            loadediframe.getElement().setId(LOADED_IFRAME);
            loadingiframe = null;
            displayingRow = next;
        }
    }

    @Override
    public void display(final RESTBaseTopicV1<?, ?, ?> topic, final boolean readOnly) {
        throw new UnsupportedOperationException("TopicRenderedView.display() is not supported. Use TopicRenderedView.displayTopicRendered() instead.");
    }

    @Override
    public void clear() {
        final int next = displayingRow == 0 ? 1 : 0;
        flexTable.setWidget(next, 0, null);
    }

    @Override
    public boolean displayTopicRendered(@NotNull final Integer topicXMLHoldID, final boolean readOnly, final boolean showImages) {

        /*
            There is the potential for a race condition here. If a XML file takes longer to render than the next refresh
            (maybe a large XML file was rendered, followed by a small one) then when the large one finished it will
            trigger the small one to be displayed prematurely.

            All this means in this situation is that the user will see blank screen or a half rendered HTML page.
         */

        loadingiframe = new Frame();
        loadingiframe.getElement().setId(LOADING_IFRAME);
        loadingiframe.setUrl(Constants.REST_SERVER + Constants.ECHO_ENDPOINT + "?id=" + topicXMLHoldID);
        loadingiframe.addStyleName(CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME);
        flexTable.setWidget(displayingRow, 0, loadingiframe);

        return true;
    }


}