package com.solace.spring_cloud_stream.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableBinding(Source.class)
public class REST2Solace {

	@Autowired
    private Source channels;

	@RequestMapping(value="/publish", consumes = MediaType.ALL_VALUE, method = RequestMethod.POST)
	public void output(@RequestBody String payload, @RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType) {
		String msgPayload = payload != null ? payload:  "";
		System.err.println(payload);
		/**
		 * TODO: should be able to treat this as Message<String> but in 
		 * spring-cloud-stream.version 2.0.0.M4 it gives a java.lang.ClassCastException: java.lang.String cannot be cast to [B
		 */
		Message<?> message = MessageBuilder.withPayload(msgPayload.getBytes())
				.setHeader(MessageHeaders.CONTENT_TYPE, contentType)
				.build();
		channels.output().send(message);
		System.err.println("Published message: "+message.toString());
	}
}
