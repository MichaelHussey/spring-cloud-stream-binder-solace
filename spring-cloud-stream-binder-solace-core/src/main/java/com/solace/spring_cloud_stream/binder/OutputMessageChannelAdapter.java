package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import com.solace.spring_cloud_stream.binder.properties.JcsmpProducerProperties;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.SessionEventArgs;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.Topic;

public class OutputMessageChannelAdapter extends MessageProducerSupport implements MessageHandler, SessionEventHandler {

	private static final Logger logger = LoggerFactory.getLogger(OutputMessageChannelAdapter.class);

	protected Topic topic;
	protected JCSMPSession session;

	protected String topicName;

	protected String channelName;

	public OutputMessageChannelAdapter(String name) {

		logger.info("Construct OutputMessageChannelAdapter:"+name);
		channelName = name;
	}

	public void createPublisher(SolaceBinder binder, ProducerDestination destination,
			ExtendedProducerProperties<JcsmpProducerProperties> producerProperties, MessageChannel errorChannel2) {

		try {
			session = JCSMPFactory.onlyInstance().createSession(binder.getProperties(),binder.getContext(), this);
			session.connect();		
			logger.info("Connection to Solace Message Router succeeded!");

			topicName = destination.getName();

			topic = JCSMPFactory.onlyInstance().createTopic(topicName);
		} catch (InvalidPropertiesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//TODO: complete impl
	}

	/**
	 * from {@link MessageHandler} 
	 * @param message
	 * @throws MessagingException
	 */
	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		// TODO Auto-generated method stub

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
