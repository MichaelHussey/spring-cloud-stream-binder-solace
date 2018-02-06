package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;

public class SolaceConsumerDestination implements ConsumerDestination {

	private String name;
	public SolaceConsumerDestination(String _name, ExtendedConsumerProperties<JcsmpConsumerProperties> properties) {
		name = _name;
		
		// NB for now we only use Topics, no provisioning necessary
	}
	@Override
	public String getName() {
		return name;
	}

}
