package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.dispatcher.MessageDispatcher;


import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;

/**
 * MessageChannel which listens to a Solace topic
 * @author michussey
 *
 */
public class TopicMessageChannel extends AbstractSubscribableChannel implements SubscribableChannel, XMLMessageListener{

	private static final Logger log = LoggerFactory.getLogger(TopicMessageChannel.class);

	protected Topic topic;
	protected XMLMessageConsumer consumer;
	protected JCSMPSession session;
	
	/**
	 * TODO - assuming right now we have a single MessageHandler, should likely be a Map
	 */
	protected MessageHandler handler;
	
	/**
	 * TODO - figure out how to pass the topic name from the SpringBootApp
	 */
	protected String topicName = "test/>";
	
	protected String channelName;
	
	public TopicMessageChannel(String name) {
		channelName = name;
	}

	/**
	 * from {@link SubscribableChannel#send(Message<?> message) SubscribableChannel}
	 */
	@Override
	public boolean send(Message<?> message) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * from {@link SubscribableChannel#send(Message<?> message, long timeout) SubscribableChannel}
	 */
	@Override
	public boolean send(Message<?> message, long timeout) {
		// TODO Auto-generated method stub
		return false;
	}
	protected MessageDispatcher	getDispatcher() {
		// TODO 
		return null;
	}

	/**
	 * Create the Topic and Subscribe to it.
	 * from {@link SubscribableChannel#send(MessageHandler handler) SubscribableChannel}
	 */
	@Override
	public boolean subscribe(MessageHandler handler) {
		this.handler = handler;
		return true;
	}
	
	public void start() {

		// Create the topic	
		try {
			topic = JCSMPFactory.onlyInstance().createTopic(topicName);
			consumer = session.getMessageConsumer(this);
			session.addSubscription(topic);
			consumer.start();
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * from {@link SubscribableChannel#unsubscribe(MessageHandler handler) SubscribableChannel}
	 */
	@Override
	public boolean unsubscribe(MessageHandler handler) {
		if (consumer != null) {
			consumer.stop();
			consumer.close();
			return true;
		}
		return false;
	}

	/**
	 * from {@link XMLMessageListener#onException(JCSMPException arg0) XMLMessageListener}
	 */
	@Override
	public void onException(JCSMPException arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * from {@link XMLMessageListener#onReceive(BytesXMLMessage arg0) XMLMessageListener}
	 */
	@Override
	public void onReceive(BytesXMLMessage solaceMessage) {
		System.err.println("received message "+solaceMessage);
		Message<?> springMessage = null;
		MessageBuilder<?> springMB = null;
		if (solaceMessage instanceof TextMessage)
		{
			TextMessage solaceTextMessage = (TextMessage) solaceMessage;
			springMB = MessageBuilder.withPayload(solaceTextMessage.getText());
		} else {
			springMB = MessageBuilder.withPayload(solaceMessage.getBytes());
		}
		// TODO: set all the standard header properties
		springMB.setHeader(SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_ID, solaceMessage.getApplicationMessageId());
		
		springMessage = springMB.build();
		handler.handleMessage(springMessage);
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public void setSession(JCSMPSession session) {
		this.session = session;
		
	}

	public void close() {
		if (consumer != null) {
			consumer.stop();
			consumer.close();
		}
	}

}
