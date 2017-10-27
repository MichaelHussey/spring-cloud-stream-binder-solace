package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;

public interface SolaceTopicSink {

	String INPUT = "input";

	@Input(Sink.INPUT)
	TopicMessageChannel input();
}
