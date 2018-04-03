/**
 * 
 */
package com.solace.spring_cloud_stream.binder;

import java.io.Serializable;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import com.solacesystems.jcsmp.BytesXMLMessage;

/**
 * Wrapper around a received Solace message which maps it to a Spring Message
 * @author michussey
 * @param <T>
 *
 */
public class SolaceMessage<T> implements Message<T>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8868969204325357275L;
	
	private BytesXMLMessage solaceMessage;
	
	private MessageHeaders springHeaders;
	
	private T payload;

	public SolaceMessage(BytesXMLMessage originalMessage) {
		this.solaceMessage = originalMessage;
	}

	public BytesXMLMessage getSolaceMessage() {
		return solaceMessage;
	}

	@Override
	public T getPayload() {
		return payload;
	}

	@Override
	public MessageHeaders getHeaders() {
		return springHeaders;
	}

}
