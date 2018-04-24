/**
 * 
 */
package com.solace.spring_cloud_stream.binder;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.test.mock.MockIntegration;
import org.springframework.integration.test.mock.MockMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author michussey
 *
 */
public class OutputMessageChannelAdapterTest {

	
	@Bean
	public MockMessageHandler getOutChannel()
	{
		MockMessageHandler handler = MockIntegration.mockMessageHandler();
		return handler;
	}
	@Test
	public void testHandleMessage() {
		HashMap<String, Object> headerMap = new HashMap<String, Object>();
		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_ID, 
				"appId");
		MessageHeaders mh = new MessageHeaders(headerMap);
		Message<?> springMessage = MessageBuilder.createMessage("Just some data", mh);
		
		SolaceProducerDestination destination = new SolaceProducerDestination("test", null);
		OutputMessageChannelAdapter ad = new OutputMessageChannelAdapter();
		ad.setDestination(destination);
		ad.handleMessage(springMessage);
	}

}
