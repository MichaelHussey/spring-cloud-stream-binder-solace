package com.solace.spring_cloud_stream.sample;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.messaging.Sink;

import com.solace.spring_cloud_stream.binder.InputMessageChannelAdapter;

/**
 * Sample interface which defines which subscriptions our application will create
 */
public interface ListenerDefinitionInterface {

	/**
	 * Explicitly use {@link InputMessageChannelAdapter} to indicate we're listening to a Solace Channel
	 * @return 
	 * @return
	 */
    @Input("input")
    InputMessageChannelAdapter testMessage();

    @Input("input1")
    InputMessageChannelAdapter testMessage1();

}
