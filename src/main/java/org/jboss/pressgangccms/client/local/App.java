package org.jboss.pressgangccms.client.local;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.pressgangccms.client.local.constants.Constants;
import org.jboss.pressgangccms.client.local.resources.css.CSSResources;
import org.jboss.pressgangccms.client.local.view.SearchView;
import org.jboss.pressgangccms.client.local.view.WelcomeView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

@EntryPoint
public class App
{
	private HandlerManager eventBus = new HandlerManager(null);

	@Inject
	private AppController appController;

	@AfterInitialization
	public void startApp()
	{
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler()
		{
			@Override
			public void onUncaughtException(final Throwable ex)
			{
				ex.printStackTrace();
				Window.alert("Uncaught exception was detected. Redirecting you to the home page.\nException: " + ex.getMessage());
				History.newItem(WelcomeView.HISTORY_TOKEN);
			}
		});
		
		/* Setup the REST client */
		RestClient.setApplicationRoot(Constants.REST_SERVER);
		RestClient.setJacksonMarshallingActive(true);
		
		final RootPanel root = RootPanel.get();
		
		/* Inject the CSS file */		
		CSSResources.INSTANCE.App().ensureInjected();
		
		appController.go(root);
	}

	@Produces
	private HandlerManager produceEventBus()
	{
		return eventBus;
	}	
}