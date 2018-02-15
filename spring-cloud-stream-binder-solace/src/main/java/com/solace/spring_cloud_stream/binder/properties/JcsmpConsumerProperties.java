package com.solace.spring_cloud_stream.binder.properties;

import javax.validation.constraints.NotNull;

public class JcsmpConsumerProperties {

	public JcsmpConsumerProperties() {
	}
	
	@NotNull
	private String topicName;
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
