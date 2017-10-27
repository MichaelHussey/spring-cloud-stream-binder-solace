package com.solace.spring_cloud_stream.binder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

/**
 * 
 * @author michussey
 *
 */
@ConfigurationProperties("spring.cloud.stream.solace")
public class JscmpExtendedBindingProperties implements ExtendedBindingProperties<JcsmpConsumerProperties, JcsmpProducerProperties>{

	private Map<String, JcsmpProducerProperties> producerBindings = new HashMap<>();
	private Map<String, JcsmpConsumerProperties> consumerBindings = new HashMap<>();
	
	public JcsmpConsumerProperties getExtendedConsumerProperties(String channelName) {
		if (consumerBindings.containsKey(channelName)) {
			return consumerBindings.get(channelName);
		}
		else {
			return new JcsmpConsumerProperties();
		}
	}

	public JcsmpProducerProperties getExtendedProducerProperties(String channelName) {
		if (producerBindings.containsKey(channelName) ) {
			return producerBindings.get(channelName);
		}
		else {
			return new JcsmpProducerProperties();
		}
	}

}
