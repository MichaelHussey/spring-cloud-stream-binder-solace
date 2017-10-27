package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binding.CompositeMessageChannelConfigurer;
import org.springframework.cloud.stream.binding.SubscribableChannelBindingTargetFactory;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * Binder {@link org.springframework.context.annotation.Configuration} for the
 * {@link @SolaceBinder}.
 * 
 * Creates an instance of the SolaceBinder Bean and connects to the Solace Message Router
 * @author michussey
 *
 */
@Configuration
@EnableConfigurationProperties(SolaceConfigurationProperties.class)
public class SolaceBinderConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SolaceBinderConfiguration.class);

	private SolaceConfigurationProperties solaceProperties;
	@Autowired
	public void setSolaceProps(SolaceConfigurationProperties solaceProps) {
		this.solaceProperties = solaceProps;
	}

	private Binder<TopicMessageChannel, ?, ?> solaceBinder = new SolaceBinder();
	
	@Bean
	public Binder<TopicMessageChannel, ?, ?> getSolaceBinder() {
		return solaceBinder;
	}
	
	/**
	 * With a bit of luck this will allow us to have TopicMessageChannel used in the Application interface defn
	 * @param compositeMessageChannelConfigurer
	 * @return
	 */
    @Bean(name="channelFactory")
	public SolaceChannelFactory solaceChannelFactory(
			CompositeMessageChannelConfigurer compositeMessageChannelConfigurer) {
		return new SolaceChannelFactory(compositeMessageChannelConfigurer);
	}
}
