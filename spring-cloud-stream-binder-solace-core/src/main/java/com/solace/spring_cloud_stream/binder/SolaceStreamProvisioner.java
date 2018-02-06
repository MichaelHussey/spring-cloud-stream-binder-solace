package com.solace.spring_cloud_stream.binder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.context.annotation.Bean;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;
import com.solace.spring_cloud_stream.binder.properties.JcsmpProducerProperties;
import com.solace.spring_cloud_stream.binder.properties.SolaceConfigurationProperties;

public class SolaceStreamProvisioner implements
ProvisioningProvider<ExtendedConsumerProperties<JcsmpConsumerProperties>, ExtendedProducerProperties<JcsmpProducerProperties>> {

	private static final Log logger = LogFactory.getLog(SolaceStreamProvisioner.class);

	protected SolaceConfigurationProperties properties;
	/**
	 * Constructor is required for AutoWiring to work
	 */
	public SolaceStreamProvisioner(SolaceConfigurationProperties solaceConfigProperties)
	{
		Assert.notNull(solaceConfigProperties,"'solaceConfigProperties' must not be null");
		this.properties = solaceConfigProperties;
	}
	
	@Override
	public ProducerDestination provisionProducerDestination(String name,
			ExtendedProducerProperties<JcsmpProducerProperties> properties) throws ProvisioningException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ConsumerDestination provisionConsumerDestination(String name, String group,
			ExtendedConsumerProperties<JcsmpConsumerProperties> properties) throws ProvisioningException {
		
		if (logger.isInfoEnabled()) {
			logger.info("Using Solace topic for inbound: " + name + " group:"+group);
		}
		return new SolaceConsumerDestination(name, properties);
	}

}
