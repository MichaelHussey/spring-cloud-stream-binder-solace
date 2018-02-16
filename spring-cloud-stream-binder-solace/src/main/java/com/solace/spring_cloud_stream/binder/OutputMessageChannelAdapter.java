package com.solace.spring_cloud_stream.binder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.MessagingException;

import com.solace.spring_cloud_stream.binder.properties.JcsmpProducerProperties;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.Context;
import com.solacesystems.jcsmp.InvalidPropertiesException;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.SessionEventArgs;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.Topic;
import com.solacesystems.jcsmp.XMLMessage;
import com.solacesystems.jcsmp.XMLMessageProducer;
import org.springframework.beans.factory.DisposableBean;


public class OutputMessageChannelAdapter extends MessageProducerSupport implements MessageHandler, SessionEventHandler, JCSMPStreamingPublishEventHandler, DisposableBean {

	private static final Logger logger = LoggerFactory.getLogger(OutputMessageChannelAdapter.class);

	protected JCSMPSession session;

	//protected String channelName;
	
	protected XMLMessageProducer producer;
	
	protected SolaceProducerDestination destination;

	private String name;
	
	@Override
	protected void doStart() {
		super.doStart();
		logger.info("Starting OutputMessageChannelAdapter: "+name);
	}

	public void createPublisher(SolaceBinder binder, ProducerDestination _destination,
			ExtendedProducerProperties<JcsmpProducerProperties> producerProperties, MessageChannel errorChannel2) {

		try {
			session = JCSMPFactory.onlyInstance().createSession(binder.getProperties(), binder.getContext(), this);
			session.connect();		
			logger.info("Connection to Solace Message Router succeeded!");
			
			if (_destination instanceof SolaceProducerDestination)
			{
				destination = (SolaceProducerDestination) _destination;
			}

			producer = session.getMessageProducer(this);
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
		logger.info("Processing message: "+message);
		XMLMessage solaceMessage = JCSMPFactory.onlyInstance().createMessage(BytesXMLMessage.class);
		try {
			MessageHeaders headers = message.getHeaders();
			if (headers.containsKey(SolaceBinderConstants.FIELD_CORRELATION_ID)) {
				solaceMessage.setCorrelationId((String) headers.get(SolaceBinderConstants.FIELD_CORRELATION_ID));
			}
			Object payloadObject = message.getPayload();
			byte[] payloadBytes = null;
			if (payloadObject instanceof byte[])
			{
				payloadBytes = (byte[]) payloadObject;
			}
			else
			{
				logger.warn("Can't handle payload of type ["+payloadObject.getClass().getName()+"]");
			}
			solaceMessage.writeAttachment(payloadBytes);
			producer.send(solaceMessage , destination.getTopic());
			logger.info("Sent message to destination ["+destination.getName()+"]");
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	

	/**
	 * from {@link JCSMPStreamingPublishEventHandler}
	 */
	@Override
	public void handleError(String arg0, JCSMPException arg1, long arg2) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * from {@link JCSMPStreamingPublishEventHandler}
	 */
	@Override
	public void responseReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return "OutputMessageChannelAdapter{" +
				"name=" + name +
				", destination='" + this.destination.getName() + '\'' +
				'}';
	}

	public void setChannelName(String _name) {
		this.name = _name;

	}

}
