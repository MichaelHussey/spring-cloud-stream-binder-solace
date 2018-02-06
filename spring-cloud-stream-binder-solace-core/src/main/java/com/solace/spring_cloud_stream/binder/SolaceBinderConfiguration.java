package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binding.CompositeMessageChannelConfigurer;
import org.springframework.cloud.stream.binding.SubscribableChannelBindingTargetFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.solace.spring_cloud_stream.binder.properties.JcsmpExtendedBindingProperties;
import com.solace.spring_cloud_stream.binder.properties.SolaceConfigurationProperties;

/**
 * Binder {@link org.springframework.context.annotation.Configuration} for the
 * {@link @SolaceBinder}.
 * 
 * Creates an instance of the SolaceBinder Bean and connects to the Solace Message Router
 * @author michussey
 *
 */
@Configuration
@EnableConfigurationProperties({SolaceConfigurationProperties.class, JcsmpExtendedBindingProperties.class})
public class SolaceBinderConfiguration {

	private static final Logger log = LoggerFactory.getLogger(SolaceBinderConfiguration.class);

	@Autowired
	private SolaceConfigurationProperties solaceProperties;
	
	@Bean
	public SolaceStreamProvisioner getSolaceStreamProvisioner(SolaceConfigurationProperties solaceProperties) {
		return new SolaceStreamProvisioner(solaceProperties);
	}

	@Bean
	public SolaceBinder getSolaceBinder(SolaceStreamProvisioner provisioningProvider) {
		SolaceBinder solaceBinder = new SolaceBinder(null, provisioningProvider);
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
