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
public class SolaceChannelFactory extends AbstractBindingTargetFactory<InputMessageChannelAdapter>  {

	public SolaceChannelFactory(MessageChannelConfigurer messageChannelConfigurer) {
		super(InputMessageChannelAdapter.class);
		this.messageChannelConfigurer = messageChannelConfigurer;
	}

	private final MessageChannelConfigurer messageChannelConfigurer;

	/**
	 * Called when a interface is discovered which is annotated with "@Input(Sink.INPUT)"
	 */
	@Override
	public InputMessageChannelAdapter createInput(String name) {
		InputMessageChannelAdapter messageChannel = new InputMessageChannelAdapter(name);
		messageChannelConfigurer.configureInputChannel(messageChannel, name);
		return messageChannel;
	}

	@Override
	public InputMessageChannelAdapter createOutput(String name) {
		InputMessageChannelAdapter messageChannel = new InputMessageChannelAdapter(name);
		return messageChannel;
	}

}
