package com.solace.spring_cloud_stream.binder;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.solace.spring_cloud_stream.binder.properties.SolaceConfigurationProperties;

/**
 * 
 * @author michussey
 *
 */
@Configuration
@Import(SolaceBinderConfiguration.class)
@ConditionalOnMissingBean(SolaceBinder.class)
@EnableConfigurationProperties(SolaceConfigurationProperties.class)
public class SolaceBinderAutoConfiguration {

	//@Bean
	/*
	public BinderFactory binderFactory(final Binder<MessageChannel, ?, ?> binder) {
		return new BinderFactory() {
			@SuppressWarnings("unchecked")
			@Override
			public <T> Binder<T, ? extends ConsumerProperties, ? extends ProducerProperties> getBinder(
					String configurationName, Class<? extends T> bindableType) {
				return (Binder<T, ? extends ConsumerProperties, ? extends ProducerProperties>) binder;
			}
		};
	}*/
	
}
