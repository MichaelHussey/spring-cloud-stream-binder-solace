package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binder.Binding;
import org.springframework.cloud.stream.binder.ConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.solace.spring_cloud_stream.binder.unused.SolaceChannelAdapter;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;
import com.solacesystems.jcsmp.JCSMPSession;

/**
 * Stream Binder which uses Solace JCSMP messaging API.
 * @author michussey
 *
 */
public class SolaceBinder implements Binder<TopicMessageChannel, ConsumerProperties, ProducerProperties> {

	private static final Logger log = LoggerFactory.getLogger(SolaceBinder.class);

	/**
	 * Configuration config
	 */
     protected SolaceConfigurationProperties solaceProperties;
     
    @Autowired
	public void setSolaceProps(SolaceConfigurationProperties solaceProps) {
		this.solaceProperties = solaceProps;
	}

	private JCSMPSession session;

	/**
	 * Default constructor
	 */
	public SolaceBinder() {
		System.err.println("Constructor: SolaceBinder");
		
	}

	/**
	 * Called when Spring Boot starts.
	 * @return
	 * @throws JCSMPException 
	 * @throws InvalidPropertiesException 
	 */
	@Bean
	public Binder<TopicMessageChannel, ?, ?> connectToMessageRouter() throws InvalidPropertiesException, JCSMPException {
		final JCSMPProperties properties = new JCSMPProperties();
		properties.setProperty(JCSMPProperties.HOST, solaceProperties.getSmfHost());
		properties.setProperty(JCSMPProperties.VPN_NAME, solaceProperties.getMsgVpn());
		properties.setProperty(JCSMPProperties.USERNAME, solaceProperties.getUsername());
		if (solaceProperties.getPassword() != null)
		{
			properties.setProperty(JCSMPProperties.PASSWORD, solaceProperties.getPassword());
		}
		properties.setProperty(JCSMPProperties.APPLICATION_DESCRIPTION, 
				SolaceBinderConstants.BINDER_NAME+" Version "+SolaceBinderConstants.BINDER_VERSION);

		// Settings for automatic reconnection to Solace Router
		JCSMPChannelProperties channelProps = (JCSMPChannelProperties) properties.getProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES);
//		channelProps.setReconnectRetries(reconnectRetries);
		//		channelProps.setReconnectRetryWaitInMillis(reconnectRetryWaitInMillis);
		//		channelProps.setConnectTimeoutInMillis(connectTimeoutInMillis);
		//		channelProps.setConnectRetriesPerHost(connectRetriesPerHost);
		//		channelProps.setKeepAliveIntervalInMillis(keepAliveIntervalInMillis);

		properties.setProperty(JCSMPProperties.CLIENT_CHANNEL_PROPERTIES, channelProps);


		log.info("Connecting to Solace Message Router...");
		session = JCSMPFactory.onlyInstance().createSession(properties);
		session.connect();		
		log.info("Connection to Solace Message Router succeeded!");
		
		return this;
	}

	/**
	 * Method required by org.springframework.cloud.stream.binder.Binder interface
	 */
	@Override
	public Binding<TopicMessageChannel> bindConsumer(String name, String group, TopicMessageChannel inboundBindTarget,
			ConsumerProperties consumerProperties) {
		Binding<TopicMessageChannel> retval = null;
		String topicName = "test/>";
		try {
			if (session == null) {
				connectToMessageRouter();
			}
			retval = new SolaceConsumerBinding(inboundBindTarget, topicName, session);
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retval;
	}

	/**
	 * Method required by org.springframework.cloud.stream.binder.Binder interface
	 */
	@Override
	public Binding<TopicMessageChannel> bindProducer(String name, TopicMessageChannel outboundBindTarget,
			ProducerProperties producerProperties) {
		// TODO Auto-generated method stub
		return null;
	}

}
