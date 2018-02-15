package com.solace.spring_cloud_stream.binder;

import java.util.HashMap;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dispatcher.BroadcastingDispatcher;
import org.springframework.integration.dispatcher.MessageDispatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.Assert;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.Destination;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.Queue;
import com.solacesystems.jcsmp.SDTException;
import com.solacesystems.jcsmp.SDTMap;
import com.solacesystems.jcsmp.SessionEventArgs;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;

/**
 * Listen to Solace messages and forward to Spring Integration framework
 * @author michussey
 *
 */
public class InputMessageChannelAdapter extends AbstractSubscribableChannel implements XMLMessageListener, MessageProducer, SessionEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(InputMessageChannelAdapter.class);

	private volatile String outputChannelName;

	protected XMLMessageConsumer consumer;

	/** 
	 * Every MessageChannel instance needs its own Session so that the
	 * receive message callbacks are kept separate
	 */
	protected JCSMPSession session;


	protected MessageDispatcher dispatcher;

	private volatile MessageChannel outputChannel;

	protected String channelName;
	
	protected SolaceConsumerDestination destination;

	public InputMessageChannelAdapter(String name) {

		logger.info("Construct InputMessageChannelAdapter:"+name);
		channelName = name;
	}

	protected void doStop() {
		consumer.close();
		session.closeSession();
	}


	@Override
	public void onException(JCSMPException arg0) {
		logger.warn("Exception processing received message on topic "+destination.getName()+": "+Utils.jcsmpExceptionToString(arg0));
	}

	/**
	 * From {@link MessageProducer#getDispatcher() MessageProducer}
	 */
	@Override
	protected MessageDispatcher getDispatcher() {
		if (dispatcher == null)
		{
			dispatcher = new BroadcastingDispatcher(true);
		}
		return dispatcher;
	}

	/**
	 * From {@link MessageProducer#setOutputChannel(MessageChannel outputChannel) MessageProducer}
	 */
	@Override
	public void setOutputChannel(MessageChannel _outputChannel) {
		outputChannel = _outputChannel;
	}

	public void setOutputChannelName(String outputChannelName) {
		Assert.hasText(outputChannelName, "'outputChannelName' must not be null or empty");
		this.outputChannelName = outputChannelName;
	}

	@Override
	public MessageChannel getOutputChannel() {
		if (this.outputChannelName != null) {
			synchronized (this) {
				if (this.outputChannelName != null) {
					this.outputChannel = getChannelResolver().resolveDestination(this.outputChannelName);
					this.outputChannelName = null;
				}
			}
		}
		return this.outputChannel;
	}


	/**
	 * Subscribe to the topic (or queue)
	 * @param session2
	 * @param destination
	 * @param properties 
	 */
	public void doSubscribe(SolaceBinder binder, ConsumerDestination _destination, ExtendedConsumerProperties<JcsmpConsumerProperties> properties) {
		try {
			session = JCSMPFactory.onlyInstance().createSession(binder.getProperties(), binder.getContext(), this);
			session.connect();
			if (logger.isInfoEnabled())		
				logger.info("Channel ["+this.channelName+"] Connection to Solace Message Router succeeded!");

			if (_destination instanceof SolaceConsumerDestination) 
			{
				destination = (SolaceConsumerDestination) _destination;
			}

			consumer = session.getMessageConsumer(this);
			session.addSubscription(destination.getTopic());
			consumer.start();
			if (logger.isInfoEnabled())
				logger.info("Channel ["+this.channelName+"] subscribed successfully to "+destination.getName());
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * from {@link XMLMessageListener#onReceive(BytesXMLMessage arg0) XMLMessageListener}
	 */
	@Override
	public void onReceive(BytesXMLMessage solaceMessage) {
		if (logger.isInfoEnabled())
			logger.info("Channel ["+this.channelName+"] received message on "+destination.getName());
		Message<?> springMessage = null;
		HashMap<String, Object> headerMap = new HashMap<String, Object>();

		// TODO: set all the standard header properties

		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_ID, 
				solaceMessage.getApplicationMessageId());
		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_CORRELATION_ID, 
				solaceMessage.getCorrelationKey());
		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_TYPE, 
				solaceMessage.getApplicationMessageType());
		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_SENDERID, 
				solaceMessage.getSenderId());
		headerMap = Utils.putIfNotNull(headerMap, SolaceBinderConstants.FIELD_SENDER_TIMESTAMP, 
				solaceMessage.getSenderTimestamp());

		// Store the topic or queue name on which we received the message
		Destination dest = solaceMessage.getDestination();
		String prefix = "T/";
		if (dest instanceof Queue) {
			prefix = "Q/";
		}
		headerMap.put(SolaceBinderConstants.FIELD_DESTINATION_NAME, prefix+dest.getName());

		// Map any user properties
		SDTMap userProperties = solaceMessage.getProperties();
		if (userProperties != null)
		{
			Iterator<String> iter = userProperties.keySet().iterator();
			while (iter.hasNext())
			{
				String key = iter.next();
				try {
					headerMap.put(key, userProperties.get(key));
				} catch (SDTException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}


		MessageHeaders mh = new MessageHeaders(headerMap);
		if (solaceMessage instanceof TextMessage)
		{
			TextMessage solaceTextMessage = (TextMessage) solaceMessage;
			springMessage = MessageBuilder.createMessage(solaceTextMessage.getText(), mh);
		} else {
			springMessage = MessageBuilder.createMessage(solaceMessage.getBytes(), mh);
		}
		outputChannel.send(springMessage);
	}

	/**
	 * from {@link SessionEventHandler}
	 */
	@Override
	public void handleEvent(SessionEventArgs arg0) {
		if (arg0.getResponseCode() != 0)
		{
			if (logger.isWarnEnabled())
			{
				logger.warn("Solace session event received: "+Utils.sessionEventToString(arg0));
			}
		}
		else
		{
			if (logger.isInfoEnabled())
			{
				logger.info("Solace session event received: "+Utils.sessionEventToString(arg0));
			}
		}
	}
}
