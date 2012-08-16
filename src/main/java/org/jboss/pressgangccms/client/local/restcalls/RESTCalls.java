package org.jboss.pressgangccms.client.local.restcalls;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.PathSegmentImpl;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.pressgangccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTopicV1;
import org.jboss.pressgangccms.rest.v1.jaxrsinterfaces.RESTInterfaceV1;

/**
 * This class provides a standard way to call the REST server and respond to the
 * various success and failure paths.
 * 
 * @author Matthew Casperson
 * 
 */
public final class RESTCalls
{
	abstract public interface RESTCallback<T>
	{
		void begin();

		void generalException(final Exception ex);

		void success(final T retValue);

		void failed();
	}

	static private <T> RemoteCallback<T> constructSuccessCallback(final RESTCallback<T> callback)
	{
		final RemoteCallback<T> successCallback = new RemoteCallback<T>()
		{
			@Override
			public void callback(final T retValue)
			{
				callback.success(retValue);
			}
		};

		return successCallback;
	}

	static private <T> ErrorCallback constructErrorCallback(final RESTCallback<T> callback)
	{
		final ErrorCallback errorCallback = new ErrorCallback()
		{
			@Override
			public boolean error(final Message message, final Throwable throwable)
			{
				callback.failed();
				return true;
			}
		};

		return errorCallback;

	}

	static public void getTopic(final RESTCallback<RESTTopicV1> callback, final Integer id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "";

		try
		{
			callback.begin();
			restMethod.getJSONTopic(id, expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}

	static public void getTopicsFromQuery(final RESTCallback<RESTTopicCollectionV1> callback, final String queryString, int start, int end)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[{\"trunk\":{\"start\": " + start + ", \"end\": " + end + ", \"showSize\":true,\"name\": \"topics\"}}]}";

		try
		{
			callback.begin();
			restMethod.getJSONTopicsWithQuery(new PathSegmentImpl(queryString), expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
}