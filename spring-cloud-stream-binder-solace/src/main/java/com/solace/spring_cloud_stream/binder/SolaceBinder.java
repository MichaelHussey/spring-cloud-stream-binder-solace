package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.binder.HeaderMode;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.Assert;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;
import com.solace.spring_cloud_stream.binder.properties.JcsmpExtendedBindingProperties;
import com.solace.spring_cloud_stream.binder.properties.JcsmpProducerProperties;
import com.solace.spring_cloud_stream.binder.properties.SolaceConfigurationProperties;
import com.solacesystems.jcsmp.Context;
import com.solacesystems.jcsmp.ContextProperties;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPChannelProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPProperties;

/**
 * A Stream {@link org.springframework.cloud.stream.binder.Binder} which uses the Solace JCSMP messaging API.
 * @author Mic Hussey
 *
 */
public class SolaceBinder extends AbstractMessageChannelBinder<
ExtendedConsumerProperties<JcsmpConsumerProperties>,
ExtendedProducerProperties<JcsmpProducerProperties>,
SolaceStreamProvisioner> implements ExtendedPropertiesBinder<MessageChannel, JcsmpConsumerProperties, JcsmpProducerProperties> {

	public SolaceBinder(String[] headersToEmbed, SolaceStreamProvisioner provisioningProvider) {
		super(headersToEmbed, provisioningProvider);
	}

	/**
	 * TODO: figure out how to insist supportsHeadersNatively is true properly
	 * @param supportsHeadersNatively
	 * @param headersToEmbed
	 * @param provisioningProvider
	 *
	public SolaceBinder(boolean supportsHeadersNatively, String[] headersToEmbed,
			SolaceStreamProvisioner provisioningProvider) {
		super(supportsHeadersNatively, headersToEmbed, provisioningProvider);
		Assert.isTrue(supportsHeadersNatively, "Solace messaging supports headers natively");
	}
	*/

	private static final Logger log = LoggerFactory.getLogger(SolaceBinder.class);

	/**
	 * Configuration config
	 */
	protected SolaceConfigurationProperties solaceProperties;

	InputMessageChannelAdapter messageChannelAdapter;

	protected OutputMessageChannelAdapter replyChannelAdapter;
	
	protected volatile OutputMessageChannelAdapter lastCreatedOutputChannelAdapter;

	protected OutputMessageChannelAdapter getReplyChannelAdapter() {
		if (replyChannelAdapter == null)
		{
			String replyChannelName = "null";
			if (messageChannelAdapter != null)
			{
				replyChannelName = messageChannelAdapter.channelName;
			}
			replyChannelName = replyChannelName + ".reply";
			replyChannelAdapter = new OutputMessageChannelAdapter();
			replyChannelAdapter.setChannelName(replyChannelName);
		}
		return replyChannelAdapter;
	}

	@Autowired
	public void setSolaceProps(SolaceConfigurationProperties solaceProps) {
		this.solaceProperties = solaceProps;
	}

	private JcsmpExtendedBindingProperties extendedBindingProperties = new JcsmpExtendedBindingProperties();

	protected JCSMPProperties properties;

	/**
	 * @return the properties
	 */
	public final JCSMPProperties getProperties() {
		return properties;
	}
	
	protected Context context;
	/**
	 * @return the properties
	 */
	public final Context getContext() {
		return context;
	}

	/**
	 * Called when Spring Boot starts.
	 * @return
	 * @throws JCSMPException 
	 * @throws InvalidPropertiesException 
	 */
	@Override
	public void onInit() throws InvalidPropertiesException, JCSMPException {
		log.info("SolaceBinder.onInit()");
		properties = new JCSMPProperties();
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

		ContextProperties contextProperties = new ContextProperties();

		context = JCSMPFactory.onlyInstance().createContext(contextProperties);
		log.info("Solace context created.");
	}

	@Override
	public JcsmpConsumerProperties getExtendedConsumerProperties(String channelName) {
		return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
	}

	@Override
	public JcsmpProducerProperties getExtendedProducerProperties(String channelName) {
		return this.extendedBindingProperties.getExtendedProducerProperties(channelName);
	}
	
	@Override
	protected MessageHandler createProducerMessageHandler(ProducerDestination destination,
			ExtendedProducerProperties<JcsmpProducerProperties> producerProperties, MessageChannel errorChannel)
					throws Exception {

		Assert.state(!HeaderMode.embeddedHeaders.equals(producerProperties.getHeaderMode()),
				"the Solace binder does not support embedded headers since Solace supports headers natively");
		OutputMessageChannelAdapter outputChannelAdapter = null;
		if (destination != null) {
			outputChannelAdapter = new OutputMessageChannelAdapter();
			outputChannelAdapter.createPublisher(this, destination, producerProperties, errorChannel);
			
			lastCreatedOutputChannelAdapter = outputChannelAdapter;
			log.info("Created outputChannelAdapter for ["+destination.getName()+"]");
		}
		return outputChannelAdapter;
	}
	@Override
	protected void postProcessOutputChannel(MessageChannel outputChannel, ExtendedProducerProperties<JcsmpProducerProperties> producerProperties) {
		lastCreatedOutputChannelAdapter.setOutputChannel(outputChannel);
		lastCreatedOutputChannelAdapter.setChannelName(outputChannel.toString());
		log.info("postProcessOutputChannel: "+lastCreatedOutputChannelAdapter.toString());
	}

	/**
	 * 
	 */
	@Override
	protected MessageProducer createConsumerEndpoint(ConsumerDestination destination, String group,
			ExtendedConsumerProperties<JcsmpConsumerProperties> properties) throws Exception {

		Assert.state(!HeaderMode.embeddedHeaders.equals(properties.getHeaderMode()),
				"the Solace binder does not support embedded headers since Solace supports headers natively");

		messageChannelAdapter = new InputMessageChannelAdapter(group);
		messageChannelAdapter.doSubscribe(this, destination, properties);
		return messageChannelAdapter;
	}
}
