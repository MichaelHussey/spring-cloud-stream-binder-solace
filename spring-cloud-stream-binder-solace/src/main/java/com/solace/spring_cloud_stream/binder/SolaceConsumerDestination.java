package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.Topic;

public class SolaceConsumerDestination implements ConsumerDestination {

	private String name;
	
	protected Topic topic;

	protected ExtendedConsumerProperties<JcsmpConsumerProperties> properties;

	public SolaceConsumerDestination(String _name, ExtendedConsumerProperties<JcsmpConsumerProperties> _properties) {
		name = _name;
		properties = _properties;
		// NB for now we only use Topics
		topic = JCSMPFactory.onlyInstance().createTopic(name);
	}
	@Override
	public String getName() {
		return name;
	}
	
	public Topic getTopic()
	{
		return topic;
	}

}
