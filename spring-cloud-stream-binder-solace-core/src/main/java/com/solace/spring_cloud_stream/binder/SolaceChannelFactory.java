package com.solace.spring_cloud_stream.binder;

import org.springframework.cloud.stream.binding.AbstractBindingTargetFactory;
import org.springframework.cloud.stream.binding.MessageChannelConfigurer;

/**
 * The default {@link(org.springframework.cloud.stream.binding.SubscribableChannelBindingTargetFactory) SubscribableChannelBindingTargetFactory} 
 * always returns {@link(org.springframework.integration.channel.DirectChannel) DirectChannel} so we need to provide our own
 * implementation
 * @author michussey
 *
 */
public class SolaceChannelFactory extends AbstractBindingTargetFactory<TopicMessageChannel>  {


	public SolaceChannelFactory(MessageChannelConfigurer messageChannelConfigurer) {
		super(TopicMessageChannel.class);
		this.messageChannelConfigurer = messageChannelConfigurer;
	}

	private final MessageChannelConfigurer messageChannelConfigurer;

	@Override
	public TopicMessageChannel createInput(String name) {
		TopicMessageChannel subscribableChannel = new TopicMessageChannel(name);
		this.messageChannelConfigurer.configureInputChannel(subscribableChannel, name);
		return subscribableChannel;
	}

	@Override
	public TopicMessageChannel createOutput(String name) {
		TopicMessageChannel subscribableChannel = new TopicMessageChannel(name);
		this.messageChannelConfigurer.configureInputChannel(subscribableChannel, name);
		return subscribableChannel;
	}

}
