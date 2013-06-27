package org.jboss.pressgang.ccms.ui.client.local.mvp.view.topic;


import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import hu.szaboaz.gwt.xslt.client.XsltProcessingException;
import hu.szaboaz.gwt.xslt.client.XsltProcessor;
import org.jboss.pressgang.ccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgang.ccms.rest.v1.entities.base.RESTBaseTopicV1;
import org.jboss.pressgang.ccms.rest.v1.entities.wrapper.IntegerWrapper;
import org.jboss.pressgang.ccms.ui.client.local.constants.CSSConstants;
import org.jboss.pressgang.ccms.ui.client.local.constants.Constants;
import org.jboss.pressgang.ccms.ui.client.local.mvp.events.dataevents.EntityListReceived;
import org.jboss.pressgang.ccms.ui.client.local.mvp.presenter.topic.TopicRenderedPresenter;
import org.jboss.pressgang.ccms.ui.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgang.ccms.ui.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgang.ccms.ui.client.local.resources.xsl.DocbookToHTML;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.BaseRestCallback;
import org.jboss.pressgang.ccms.ui.client.local.restcalls.RESTCalls;
import org.jboss.pressgang.ccms.ui.client.local.utilities.GWTUtilities;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * This view maintains a kind of double buffer. IFrames are loaded into hidden table cells, and then
 * "flipped" out after a period of time to give a seamless appearance of a panel being updated.
 */
public class TopicRenderedView extends BaseTemplateView implements TopicRenderedPresenter.Display {

    private static final Logger LOGGER = Logger.getLogger(TopicRenderedView.class.getName());
    private static final String LOADING_IFRAME = "LoadingIFrame";
    private static final String LOADED_IFRAME = "LoadedIFrame";
    private final FlexTable flexTable = new FlexTable();
    private int displayingRow = 0;

    private Frame loadingiframe;
    private Frame loadediframe;

    final Timer timer = new Timer() {
        @Override
        public void run() {
            final int next = displayingRow == 0 ? 1 : 0;

            /*
                Clear all styles
             */
            flexTable.getFlexCellFormatter().removeStyleName(displayingRow, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL);
            flexTable.getFlexCellFormatter().addStyleName(displayingRow, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL);

            flexTable.getFlexCellFormatter().removeStyleName(next, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_DISPLAYING_CELL);
            flexTable.getFlexCellFormatter().addStyleName(next, 0, CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME_TABLE_LOADING_CELL);

            /*
                Maintain the scroll position. This will fail if the iframe is not in the same domain.
             */

            setScroll(LOADING_IFRAME, getScrollX(LOADED_IFRAME), getScrollY(LOADED_IFRAME));

            loadediframe = loadingiframe;
            loadediframe.getElement().setId(LOADED_IFRAME);
            loadingiframe = null;
            displayingRow = next;
        }
    };

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
                iframe.contentWindow != null &&
                iframe.contentWindow.document != null &&
                iframe.contentWindow.document.defaultView != null) {
                    return iframe.contentWindow.document.defaultView.pageXOffset;
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
                iframe.contentWindow != null &&
                iframe.contentWindow.document != null &&
                iframe.contentWindow.document.defaultView != null) {
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
     * @param id  The iframe id
     * @param scrollLeft The horizontal scroll position
     * @param scrollTop The vertical scroll position
     */
    private native void setScroll(@NotNull final String id, final int scrollLeft, final int scrollTop) /*-{
        try{
            var iframe = $doc.getElementById(id);
            if (iframe != null &&
                iframe.contentWindow != null &&
                iframe.contentWindow.document != null &&
                iframe.contentWindow.document.defaultView != null) {
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


        ;

        LOGGER.info("ENTER TopicRenderedView()");
    }

    @Override
    public final void display(final RESTBaseTopicV1<?, ?, ?> topic, final boolean readOnly) {
        throw new UnsupportedOperationException("TopicRenderedView.display() is not supported. Use TopicRenderedView.displayTopicRendered() instead.");
    }

    @Override
    public final boolean displayTopicRendered(@NotNull Integer topicXMLHoldID, final boolean readOnly, final boolean showImages) {
        if (loadingiframe == null) {
            loadingiframe = new Frame();
            loadingiframe.getElement().setId(LOADING_IFRAME);
            loadingiframe.setUrl(Constants.REST_SERVER + Constants.ECHO_ENDPOINT + "?id=" + topicXMLHoldID);
            loadingiframe.addStyleName(CSSConstants.TopicView.TOPIC_RENDERED_VIEW_IFRAME);
            flexTable.setWidget(displayingRow, 0, loadingiframe);

            timer.schedule(Constants.REFRESH_RATE - (Constants.REFRESH_RATE / 10));

            return true;
        }

        return false;
    }
}