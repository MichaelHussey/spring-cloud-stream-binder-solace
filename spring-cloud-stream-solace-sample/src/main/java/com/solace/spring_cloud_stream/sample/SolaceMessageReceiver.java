package com.solace.spring_cloud_stream.sample;

/**
 * Class to which the SolaceBinder will pass Messages received from VMR
 * 
 * Annotate methods with either @StreamListener or @ServiceActivator in order to receive
 * messages from the configured channels
 */
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import com.solace.spring_cloud_stream.binder.SolaceTopicSink;

@EnableBinding(ListenerDefinitionInterface.class)
public class SolaceMessageReceiver {  


	@StreamListener(SolaceTopicSink.INPUT)
	//@ServiceActivator(inputChannel = SolaceTopicSink.INPUT)
	public void testMessage(Message<?> message) {
		System.out.println("Received testMessage: "+message);
	}
	
	//@StreamListener(SolaceTopicSink.INPUT)
	@ServiceActivator(inputChannel = "input1")
	public void testMessage1(Message<?> message) {
		System.out.println("Received testMessage1: "+message);
	}
}
