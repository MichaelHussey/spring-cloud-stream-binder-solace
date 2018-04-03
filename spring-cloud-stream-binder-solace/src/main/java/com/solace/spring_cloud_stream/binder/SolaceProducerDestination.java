package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ProducerDestination;

import com.solace.spring_cloud_stream.binder.properties.JcsmpProducerProperties;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.Topic;

public class SolaceProducerDestination implements ProducerDestination {

	private String name;
	
	protected Topic topic;
		
	protected ExtendedProducerProperties<JcsmpProducerProperties> properties;
	
	public SolaceProducerDestination(String _name, ExtendedProducerProperties<JcsmpProducerProperties> _properties) {
		name = _name;
		properties = _properties;
		// NB for now we only use Topics
		topic = JCSMPFactory.onlyInstance().createTopic(name);
	}
	
	public Topic getTopic()
	{
		return topic;
	}

	@Override
	public String getName() {
		return name;
	}
	@Override
	public String getNameForPartition(int partition)
	{
		// Solace topics have no concept of partitions.
		// TODO: maybe we should append "/partition" to topic name?
		return name;
	}

}
