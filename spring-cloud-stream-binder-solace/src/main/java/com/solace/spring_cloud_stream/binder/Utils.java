package com.solace.spring_cloud_stream.binder;

import java.util.HashMap;
import org.apache.commons.lang.exception.ExceptionUtils;

import com.solacesystems.jcsmp.JCSMPErrorResponseException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.SessionEvent;
import com.solacesystems.jcsmp.SessionEventArgs;

public class Utils {
	/**
	 * Translate the sessionEvent into a JSON string for logging
	 * @param sessionEvent
	 * @return
	 */
	public static String sessionEventToString(SessionEventArgs sessionEvent)
	{
		StringBuilder sb = new StringBuilder("{\"code\":");
		sb.append(sessionEvent.getResponseCode());
		sb.append(", \"event\":{\"type\":\"");
		SessionEvent se = sessionEvent.getEvent();
		switch (se) {
		case SUBSCRIPTION_ERROR:
			sb.append("\"SUBSCRIPTION_ERROR\", \"description\": \"The appliance rejected a subscription (add or remove).\"");
			break;
		case VIRTUAL_ROUTER_NAME_CHANGED:
			sb.append("\"VIRTUAL_ROUTER_NAME_CHANGED\", \"description\": \"The appliance's Virtual Router Name changed during a reconnect operation. This could render existing queues or temporary topics invalid.\"");
			break;
		case INCOMPLETE_LARGE_MESSAGE_RECVD:
			sb.append("\"INCOMPLETE_LARGE_MESSAGE_RECVD\", \"description\": \"Incomplete large message is received by the consumer due to not receiving all the segments in time\"");
			break;
		case UNKNOWN_TRANSACTED_SESSION_NAME:
			sb.append("\"UNKNOWN_TRANSACTED_SESSION_NAME\", \"description\": \"An attempt to re-establish a transacted session failed.\"");
			break;
		case RECONNECTING:
			sb.append("\"RECONNECTING\", \"description\": \"The session is reconnecting.\"");
			break;
		case RECONNECTED:
			sb.append("\"RECONNECTED\", \"description\": \"The session successfully reconnected.\"");
			break;
		case DOWN_ERROR:
			sb.append("\"DOWN_ERROR\", \"description\": \"The session has failed.\"");
			break;
		}
		sb.append("}");
		if (sessionEvent.getInfo() != null)
		{
			sb.append(", \"info\":\"");
			sb.append(sessionEvent.getInfo());
			sb.append("\"");
		}
		if (sessionEvent.getException() != null)
		{
			if (sessionEvent.getException() instanceof JCSMPErrorResponseException )
			{
				JCSMPErrorResponseException je = (JCSMPErrorResponseException) sessionEvent.getException();
				sb.append(", \"exception\":{\"subcode\":");
				sb.append(je.getSubcodeEx());
				sb.append("}");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	/**
	 * Translate the sessionEvent into a JSON string for logging
	 * @param sessionEvent
	 * @return
	 */
	public static String jcsmpExceptionToString(JCSMPException exception)
	{
		StringBuilder sb = new StringBuilder("{\"info\":");
		sb.append(exception.getExtraInfo());
		sb.append(", \"event\":\"");
		sb.append(exception.getMessage());
		sb.append(", \"stacktrace\":\"");
		sb.append(ExceptionUtils.getStackTrace(exception));
		sb.append("}");
		return sb.toString();
	}
	/**
	 * Helper method when translating Solace messages to Spring messages
	 * Only add a particular message property if value isn't null
	 * @param inMap
	 * @param key
	 * @param value
	 * @return
	 */
	public static HashMap<String, Object> putIfNotNull(HashMap<String, Object> inMap, String key, Object value)
	{
		if (value != null)
		{
			inMap.put(key, value);
		}
		return inMap;
	}
}
