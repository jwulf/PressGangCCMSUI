package org.jboss.pressgangccms.client.local.mvp.view.base;

import org.jboss.pressgangccms.client.local.constants.CSSConstants;
import org.jboss.pressgangccms.client.local.constants.Constants;
import org.jboss.pressgangccms.client.local.resources.css.CSSResources;
import org.jboss.pressgangccms.client.local.resources.images.ImageResources;
import org.jboss.pressgangccms.client.local.resources.strings.PressGangCCMSUI;
import org.jboss.pressgangccms.client.local.ui.UIUtilities;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class is used to build the standard page template
 * 
 * @author Matthew Casperson
 */
public abstract class BaseTemplateView implements BaseTemplateViewInterface
{
	/** The name of the application */
	private final String applicationName;
	/** The name of the current page */
	private final String pageName;

	/** Defines the top level layout that holds the header and the other content */
	private final DockLayoutPanel topLevelLayoutPanel = new DockLayoutPanel(Unit.PX);

	/** Defines the panel that holds the page title and the other content */
	private final DockLayoutPanel secondLevelLayoutPanel = new DockLayoutPanel(Unit.EM);

	/** Defines the panel that holds the shortcut bar, content and footer */
	private final DockLayoutPanel thirdLevelLayoutPanel = new DockLayoutPanel(Unit.PX);

	private final SimplePanel headingBanner = new SimplePanel();
	private final VerticalPanel pageTitleParentLayoutPanel = new VerticalPanel();
	private final Label pageTitle = new Label();
	
	private final SimplePanel shortCutPanelParent = new SimplePanel();
	private final FlexTable shortcutPanel = new FlexTable();
	private final FlexTable advancedShortcutPanel = new FlexTable();
	
	private SimpleLayoutPanel panel = new SimpleLayoutPanel();

	/** This panel holds the buttons currently displayed in the top action bar */
	private final SimplePanel topActionParentPanel = new SimplePanel();
	/** This is the default collection of top action bar items */
	private final FlexTable topActionPanel = new FlexTable();
	private final HorizontalPanel footerPanel = new HorizontalPanel();
	private final Image spinner = new Image(ImageResources.INSTANCE.spinner());
	private final DialogBox waiting = new DialogBox();

	private final PushButton home;
	private final PushButton createTopic;
	private final PushButton search;
	private final PushButton searchTranslations;
	private final PushButton images;
	private final PushButton tags;
	private final PushButton categories;
	private final PushButton projects;
	private final PushButton stringConstants;
	private final PushButton blobConstants;
	private final PushButton integerConstants;
	private final PushButton users;
	private final PushButton roles;
	private final PushButton propertyTags;
	private final PushButton propertyTagCategories;
	private final PushButton bug;
	private final PushButton reports;
	private final PushButton advanced;
	private final PushButton advancedOpen;
	private final PushButton close;

	public PushButton getClose()
	{
		return close;
	}

	public PushButton getAdvancedOpen()
	{
		return advancedOpen;
	}

	public SimplePanel getShortCutPanelParent()
	{
		return shortCutPanelParent;
	}

	public FlexTable getAdvancedShortcutPanel()
	{
		return advancedShortcutPanel;
	}

	public PushButton getAdvanced()
	{
		return advanced;
	}

	public PushButton getPropertyTagCategories()
	{
		return propertyTagCategories;
	}

	public PushButton getPropertyTags()
	{
		return propertyTags;
	}

	public PushButton getRoles()
	{
		return roles;
	}

	public PushButton getUsers()
	{
		return users;
	}

	public PushButton getIntegerConstants()
	{
		return integerConstants;
	}

	public PushButton getBlobConstants()
	{
		return blobConstants;
	}

	public PushButton getStringConstants()
	{
		return stringConstants;
	}

	public PushButton getProjects()
	{
		return projects;
	}

	public PushButton getCategories()
	{
		return categories;
	}

	public PushButton getTags()
	{
		return tags;
	}

	public PushButton getImages()
	{
		return images;
	}

	public Label getPageTitle()
	{
		return pageTitle;
	}

	public String getPageName()
	{
		return pageName;
	}

	public String getApplicationName()
	{
		return applicationName;
	}

	public SimplePanel getTopActionParentPanel()
	{
		return topActionParentPanel;
	}

	public FlexTable getShortcutPanel()
	{
		return shortcutPanel;
	}

	@Override
	public DockLayoutPanel getTopLevelPanel()
	{
		return topLevelLayoutPanel;
	}

	@Override
	public SimpleLayoutPanel getPanel()
	{
		return panel;
	}

	public PushButton getReports()
	{
		return reports;
	}

	public PushButton getSearchTranslations()
	{
		return searchTranslations;
	}

	@Override
	public FlexTable getTopActionPanel()
	{
		return topActionPanel;
	}

	@Override
	public PushButton getBug()
	{
		return bug;
	}

	@Override
	public PushButton getSearch()
	{
		return search;
	}

	@Override
	public void setSpinnerVisible(final boolean enabled)
	{
		if (enabled)
		{
			waiting.center();
			waiting.show();
		}
		else
		{
			waiting.hide();
		}
	}

	public BaseTemplateView(final String applicationName, final String pageName)
	{
		this.applicationName = applicationName;
		this.pageName = pageName;

		/* Iinitialize the loading spinner */
		waiting.setGlassEnabled(true);
		waiting.setText(PressGangCCMSUI.INSTANCE.PleaseWait());
		waiting.setWidget(spinner);

		/* Set the heading */
		headingBanner.addStyleName(CSSResources.INSTANCE.App().ApplicationHeadingPanel());
		headingBanner.add(new Image(ImageResources.INSTANCE.headingBanner()));

		topLevelLayoutPanel.addStyleName(CSSConstants.TOPLEVELLAYOUTPANEL);
		topLevelLayoutPanel.addNorth(headingBanner, 110);

		/* Set the second level layout */
		secondLevelLayoutPanel.addStyleName(CSSConstants.SECONDLEVELLAYOUTPANEL);
		topLevelLayoutPanel.add(secondLevelLayoutPanel);

		/* Set the page title */
		pageTitle.setText(pageName);
		pageTitle.addStyleName(CSSConstants.PAGETITLE);
		pageTitleParentLayoutPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		pageTitleParentLayoutPanel.add(pageTitle);

		pageTitleParentLayoutPanel.addStyleName(CSSConstants.PAGETITLEPARENTLAYOUTPANEL);
		secondLevelLayoutPanel.addNorth(pageTitleParentLayoutPanel, 3);

		/* Set the remaining content */
		thirdLevelLayoutPanel.addStyleName(CSSConstants.THIRDLEVELLAYOUTPANEL);
		secondLevelLayoutPanel.add(thirdLevelLayoutPanel);

		/* Set the action bar panel */
		topActionPanel.addStyleName(CSSConstants.TOPACTIONPANEL);

		topActionParentPanel.add(topActionPanel);

		thirdLevelLayoutPanel.addNorth(topActionParentPanel, Constants.ACTION_BAR_HEIGHT);

		/* Set the shortcut bar */
		shortCutPanelParent.setWidget(getShortcutPanel());
		shortCutPanelParent.addStyleName(CSSConstants.SHORTCUTPANELPARENT);
		getShortcutPanel().addStyleName(CSSConstants.SHORTCUTPANEL);

		thirdLevelLayoutPanel.addWest(shortCutPanelParent, Constants.SHORTCUT_BAR_WIDTH);

		/* Set the footer panel */
		footerPanel.addStyleName(CSSConstants.FOOTERPANEL);

		thirdLevelLayoutPanel.addSouth(footerPanel, 0);

		/* Add the content panel */
		panel.addStyleName(CSSConstants.CONTENTLAYOUTPANEL);

		thirdLevelLayoutPanel.add(panel);

		/* Build the shortcut panel */

		home = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Home());
		addShortcutButton(home);

		createTopic = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.CreateTopic());
		createTopic.setEnabled(false);
		addShortcutButton(createTopic);
		
		search = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Search());
		addShortcutButton(search);

		searchTranslations = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.SearchTranslations());
		searchTranslations.setEnabled(false);
		addShortcutButton(searchTranslations);

		images = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Images());
		addShortcutButton(images);

		tags = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Tags());
		addShortcutButton(tags);

		categories = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Categories());
		categories.setEnabled(false);
		addShortcutButton(categories);

		projects = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Projects());
		projects.setEnabled(false);
		addShortcutButton(projects);
		
		reports = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Reports());
		reports.setEnabled(false);
		addShortcutButton(reports);

		bug = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.CreateBug());
		addShortcutButton(bug);

		advanced = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Advanced(), true);
		addShortcutButton(advanced);
		
		advancedOpen = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Advanced(), true);
		addShortcutButton(advancedOpen, advancedShortcutPanel);
	

		users = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Users());
		users.setEnabled(false);
		addShortcutButton(users, advancedShortcutPanel);

		roles = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.Roles());
		roles.setEnabled(false);
		addShortcutButton(roles, advancedShortcutPanel);

		stringConstants = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.StringConstants());
		stringConstants.setEnabled(false);
		addShortcutButton(stringConstants, advancedShortcutPanel);

		blobConstants = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.BlobConstants());
		blobConstants.setEnabled(false);
		addShortcutButton(blobConstants, advancedShortcutPanel);

		integerConstants = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.IntegerConstants());
		integerConstants.setEnabled(false);
		addShortcutButton(integerConstants, advancedShortcutPanel);

		propertyTags = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.PropertyTags());
		propertyTags.setEnabled(false);
		addShortcutButton(propertyTags, advancedShortcutPanel);

		propertyTagCategories = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.PropertyTagCategories());
		propertyTagCategories.setEnabled(false);
		addShortcutButton(propertyTagCategories, advancedShortcutPanel);
		
		close = UIUtilities.createPushButton(PressGangCCMSUI.INSTANCE.CloseSubMenu());
		addShortcutButton(close, advancedShortcutPanel);
	}

	public void showRegularMenu()
	{
		topActionParentPanel.clear();
		topActionParentPanel.add(topActionPanel);
	}

	protected void addRightAlignedActionButtonPaddingPanel()
	{
		addRightAlignedActionButtonPaddingPanel(this.getTopActionPanel());
	}

	protected void addRightAlignedActionButtonPaddingPanel(final FlexTable table)
	{
		final int rows = table.getRowCount();
		int columns = 0;
		if (rows != 0)
		{
			columns = table.getCellCount(0);
		}

		table.setWidget(0, columns, new SimplePanel());
		table.getCellFormatter().addStyleName(0, columns, CSSConstants.RIGHTALIGNEDACTIONBUTTONS);
	}

	protected void addActionButton(final Widget widget)
	{
		final int rows = this.getTopActionPanel().getRowCount();
		int columns = 0;
		if (rows != 0)
		{
			columns = this.getTopActionPanel().getCellCount(0);
		}

		this.getTopActionPanel().setWidget(0, columns, widget);
	}

	private void addShortcutButton(final Widget widget)
	{
		addShortcutButton(widget, this.getShortcutPanel());
	}
	
	private void addShortcutButton(final Widget widget, final FlexTable table)
	{
		final int rows = table.getRowCount();
		table.setWidget(rows, 0, widget);
	}
}