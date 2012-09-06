package org.jboss.pressgangccms.client.local.restcalls;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.enterprise.client.jaxrs.api.PathSegmentImpl;
import org.jboss.errai.enterprise.client.jaxrs.api.RestClient;
import org.jboss.pressgangccms.rest.v1.collections.RESTImageCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTProjectCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTagCollectionV1;
import org.jboss.pressgangccms.rest.v1.collections.RESTTopicCollectionV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTImageV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTLanguageImageV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTProjectV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTStringConstantV1;
import org.jboss.pressgangccms.rest.v1.entities.RESTTagV1;
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
	/** The required expansion details for the topics */
	private static final String TOPIC_EXPANSION = "{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTopicV1.BUGZILLABUGS_NAME + "\"}}, {\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTopicV1.REVISIONS_NAME + "\"}}, {\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTopicV1.TAGS_NAME + "\"},\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTagV1.PROJECTS_NAME + "\"}},{\"trunk\":{\"showSize\":true,\"name\":\"" + RESTTagV1.CATEGORIES_NAME + "\"}}]}";
	/** The required expansion details for the tags */
	private static final String TAG_EXPANSION = "{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTagV1.PROJECTS_NAME + "\"}}, {\"trunk\":{\"showSize\":true,\"name\": \"" + RESTTagV1.CATEGORIES_NAME + "\"}}";
	
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
	
	static public void saveTag(final RESTCallback<RESTTagV1> callback, final RESTTagV1 tag)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[" + TAG_EXPANSION + "]}";

		try
		{
			callback.begin();
			restMethod.updateJSONTag(expand, tag);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void saveTopic(final RESTCallback<RESTTopicV1> callback, final RESTTopicV1 topic)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[" + TOPIC_EXPANSION + "]}";

		try
		{
			callback.begin();
			restMethod.updateJSONTopic(expand, topic);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getStringConstant(final RESTCalls.RESTCallback<RESTStringConstantV1> callback, final int id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		final String expand = "";
		
		try
		{
			callback.begin();
			restMethod.getJSONStringConstant(id, expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getTags(final RESTCalls.RESTCallback<RESTTagCollectionV1> callback)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[{\"branches\":[" + TAG_EXPANSION + "],\"trunk\":{\"showSize\":true,\"name\":\"tags\"}}]}";
		
		try
		{
			callback.begin();
			restMethod.getJSONTags(expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getTag(final RESTCallback<RESTTagV1> callback, final Integer id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[" + TAG_EXPANSION + "]}";

		try
		{
			callback.begin();
			restMethod.getJSONTag(id, expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}

	static public void getTopic(final RESTCallback<RESTTopicV1> callback, final Integer id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[" + TOPIC_EXPANSION + "]}";

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
	
	static public void getImage(final RESTCallback<RESTImageV1> callback, final Integer id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the language images */
		final String expand = "{\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTImageV1.LANGUAGEIMAGES_NAME + "\"},\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTLanguageImageV1.IMAGEDATABASE64_NAME + "\"}}]}]}";

		try
		{
			callback.begin();
			restMethod.getJSONImage(id, expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getImageWithoutLanguageImages(final RESTCallback<RESTImageV1> callback, final Integer id)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the language images */
		final String expand = "{\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTImageV1.LANGUAGEIMAGES_NAME + "\"}}]}";

		try
		{
			callback.begin();
			restMethod.getJSONImage(id, expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void saveImage(final RESTCallback<RESTImageV1> callback, final RESTImageV1 image)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the language images */
		final String expand = "{\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTImageV1.LANGUAGEIMAGES_NAME + "\"},\"branches\":[{\"trunk\":{\"showSize\":true,\"name\": \"" + RESTLanguageImageV1.IMAGEDATABASE64_NAME + "\"}}]}]}";

		try
		{
			callback.begin();
			restMethod.updateJSONImage(expand, image);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getTopicRevision(final RESTCallback<RESTTopicV1> callback, final Integer id, final Integer revision)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[" + TOPIC_EXPANSION + "]}";

		try
		{
			callback.begin();
			restMethod.getJSONTopicRevision(id, revision, expand);
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
	
	static public void getTagsFromQuery(final RESTCallback<RESTTagCollectionV1> callback, final String queryString, int start, int end)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[{\"trunk\":{\"start\": " + start + ", \"end\": " + end + ", \"showSize\":true,\"name\": \"tags\"}}]}";

		try
		{
			callback.begin();
			restMethod.getJSONTagsWithQuery(new PathSegmentImpl(queryString), expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getProjectsFromQuery(final RESTCallback<RESTProjectCollectionV1> callback, final String queryString, int start, int end)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String tagsExpand = "\"branches\":[{\"trunk\":{\"start\": " + start + ", \"end\": " + end + ", \"showSize\":true,\"name\": \"" + RESTProjectV1.TAGS_NAME + "\"}}]";
		final String expand = "{\"branches\":[{\"trunk\":{\"start\": " + start + ", \"end\": " + end + ", \"showSize\":true,\"name\": \"projects\"}, " + tagsExpand + "}]}";

		try
		{
			callback.begin();
			restMethod.getJSONProjectsWithQuery(new PathSegmentImpl(queryString), expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
	
	static public void getImagesFromQuery(final RESTCallback<RESTImageCollectionV1> callback, final String queryString, int start, int end)
	{
		final RESTInterfaceV1 restMethod = RestClient.create(RESTInterfaceV1.class, constructSuccessCallback(callback), constructErrorCallback(callback));
		/* Expand the categories and projects in the tags */
		final String expand = "{\"branches\":[{\"trunk\":{\"start\": " + start + ", \"end\": " + end + ", \"showSize\":true,\"name\": \"images\"}}]}";

		try
		{
			callback.begin();
			restMethod.getJSONImagesWithQuery(new PathSegmentImpl(queryString), expand);
		}
		catch (final Exception ex)
		{
			callback.generalException(ex);
		}
	}
}
