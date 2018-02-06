package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.binder.Binding;
import org.springframework.messaging.MessageChannel;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;

public class SolaceConsumerBinding implements Binding<TopicMessageChannel> {

	private final TopicMessageChannel target;

	protected Topic topic;
	protected XMLMessageConsumer consumer;
	protected JCSMPSession session;

	protected SolaceConsumerBinding(TopicMessageChannel _target, String topicName, JCSMPSession session) throws JCSMPException {
		this.target = (TopicMessageChannel)_target;
		target.setTopicName(topicName);
		target.setSession(session);
		
		target.start();
	}

	@Override
	public void unbind() {
		if (target != null) {
			target.close();
		}
	}
}
