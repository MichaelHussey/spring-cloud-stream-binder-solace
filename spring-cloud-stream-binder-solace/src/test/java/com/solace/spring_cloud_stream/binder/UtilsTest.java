package com.solace.spring_cloud_stream.binder;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.messaging.Message;

public class UtilsTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateReplyMessage() {
		String correlationId = "cor";
		String payload = "payload";
		HashMap<String, Object> headerMap = new HashMap<String, Object>();
		headerMap.put(SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_ID, correlationId);
		
		Message<String> aMessage = (Message<String>) Utils.buildSpringMessage(payload, headerMap);
		
		String replyPayload = "Reply: "+payload;
		Message<String> replyMessage = (Message<String>) Utils.createReplyMessage(aMessage, replyPayload, null);
		
		String replyCorrId = (String) replyMessage.getHeaders().get(SolaceBinderConstants.FIELD_CORRELATION_ID);
		
		assertTrue(replyCorrId.equals(correlationId));
	}

}
