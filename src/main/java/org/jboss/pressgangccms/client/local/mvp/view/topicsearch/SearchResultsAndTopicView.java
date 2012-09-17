package org.jboss.pressgangccms.client.local.mvp.view.topicsearch;

import org.jboss.pressgangccms.client.local.constants.CSSConstants;
import org.jboss.pressgangccms.client.local.constants.Constants;
import org.jboss.pressgangccms.client.local.mvp.presenter.topicsearch.SearchResultsAndTopicPresenter;
import org.jboss.pressgangccms.client.local.mvp.view.base.BaseTemplateView;
import org.jboss.pressgangccms.client.local.resources.images.ImageResources;
import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.ui.SplitType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HanldedSplitLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public class SearchResultsAndTopicView extends BaseTemplateView implements SearchResultsAndTopicPresenter.Display {
    public static final String HISTORY_TOKEN = "SearchResultsAndTopicView";
    private final HanldedSplitLayoutPanel splitPanel = new HanldedSplitLayoutPanel(Constants.SPLIT_PANEL_DIVIDER_SIZE);
    private final DockLayoutPanel resultsViewLayoutPanel = new DockLayoutPanel(Unit.PX);
    private final DockLayoutPanel topicViewLayoutPanel = new DockLayoutPanel(Unit.PX);
    private final SimpleLayoutPanel topicResultsPanel = new SimpleLayoutPanel();
    private final SimpleLayoutPanel topicViewPanel = new SimpleLayoutPanel();
    private final SimpleLayoutPanel topicResultsActionButtonsPanel = new SimpleLayoutPanel();
    private final SimpleLayoutPanel topicViewActionButtonsPanel = new SimpleLayoutPanel();
    
    /** The image to display in the waiting dialog. */
    private final Image spinner = new Image(ImageResources.INSTANCE.spinner());
    /** The dialog that is presented when the view is unavailable. */
    private final DialogBox waiting = new DialogBox();

    private SplitType splitType = SplitType.NONE;

    @Override
    public SplitType getSplitType() {
        return splitType;
    }

    @Override
    public DockLayoutPanel getTopicViewLayoutPanel() {
        return topicViewLayoutPanel;
    }

    @Override
    public HanldedSplitLayoutPanel getSplitPanel() {
        return splitPanel;
    }

    @Override
    public SimpleLayoutPanel getTopicResultsActionButtonsPanel() {
        return topicResultsActionButtonsPanel;
    }

    @Override
    public SimpleLayoutPanel getTopicResultsPanel() {
        return topicResultsPanel;
    }

    @Override
    public SimpleLayoutPanel getTopicViewPanel() {
        return topicViewPanel;
    }

    @Override
    public SimpleLayoutPanel getTopicViewActionButtonsPanel() {
        return topicViewActionButtonsPanel;
    }

    public SearchResultsAndTopicView() {
        super(PressGangCCMSUI.INSTANCE.PressGangCCMS(), PressGangCCMSUI.INSTANCE.SearchResults());

        waiting.setWidget(spinner);
        waiting.setText(PressGangCCMSUI.INSTANCE.PleaseWait());
        
        /* We have own own top action panels */
        this.getTopActionParentPanel().removeFromParent();

        addSpacerToShortcutPanels();

        resultsViewLayoutPanel.addStyleName(CSSConstants.RESULTSVIEWLAYOUTPANEL);
        topicViewLayoutPanel.addStyleName(CSSConstants.TOPICVIEWLAYOUTPANEL);

        resultsViewLayoutPanel.addNorth(topicResultsActionButtonsPanel, Constants.ACTION_BAR_HEIGHT);
        topicViewLayoutPanel.addNorth(topicViewActionButtonsPanel, Constants.ACTION_BAR_HEIGHT);

        resultsViewLayoutPanel.add(topicResultsPanel);
        topicViewLayoutPanel.add(topicViewPanel);

        splitPanel.addWest(resultsViewLayoutPanel, Constants.SPLIT_PANEL_SIZE);

        topicViewActionButtonsPanel.addStyleName(CSSConstants.TOPICSEARCHTOPICVIEWBUTTONSPANEL);
        topicViewPanel.addStyleName(CSSConstants.TOPICSEARCHTOPICVIEWDETAILSPANEL);

        splitPanel.addStyleName(CSSConstants.TOPICSEARCHRESULTSANDVIEWPARENTPANEL);

        this.getPanel().add(splitPanel);
    }

    /**
     * The split panel needs to have the center widget added last, which we need to do after optionally added a east or south
     * widget for the rendered view.
     * 
     * @param splitType How the parent panel should be split
     * @param panel The rendered view panel itself
     */
    @Override
    public void initialize(final SplitType splitType, final Panel panel) {
        this.splitType = splitType;

        final SimplePanel renderedPanelParent = new SimplePanel();
        renderedPanelParent.addStyleName(CSSConstants.TOPICVIEWLAYOUTPANEL);
        renderedPanelParent.add(panel);

        if (splitType == SplitType.HORIZONTAL) {
            splitPanel.addSouth(renderedPanelParent, Constants.SPLIT_PANEL_SIZE);
        } else if (splitType == SplitType.VERTICAL) {
            splitPanel.addEast(renderedPanelParent, Constants.SPLIT_PANEL_SIZE);
        }

        splitPanel.add(topicViewLayoutPanel);
    }

    @Override
    protected void showWaiting() {
        waiting.center();
        waiting.show();
        
    }

    @Override
    protected void hideWaiting() {
        waiting.hide();        
    }
}
