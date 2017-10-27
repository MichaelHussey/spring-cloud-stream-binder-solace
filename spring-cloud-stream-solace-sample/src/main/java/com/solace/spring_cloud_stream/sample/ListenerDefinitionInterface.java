package com.solace.spring_cloud_stream.sample;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;

import com.solace.spring_cloud_stream.binder.TopicMessageChannel;

/**
 * Sample interface which defines which subscriptions our application will create
 */
public interface ListenerDefinitionInterface {

	/**
	 * Explicitly use {@link TopicMessageChannel} to indicate we're listening to a Solace Topic
	 * @return
	 */
    @Input(Sink.INPUT)
    TopicMessageChannel testMessage();

}
