package com.solace.spring_cloud_stream.binder.unused;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

import com.solace.spring_cloud_stream.binder.InputMessageChannelAdapter;

/**
 * Sample interface which defines which subscriptions our application will create
 */
public interface ListenerDefinitionInterface {

	public static String INPUT="input";
	public static String OUTPUT="output1";
	/**
	 * Explicitly use {@link InputMessageChannelAdapter} to indicate we're listening to a Solace Channel
	 * @return 
	 * @return
	 */
    @Input(INPUT)
    InputMessageChannelAdapter testMessage();
    
    @Output(OUTPUT)
    MessageChannel forwardMessage();
}
