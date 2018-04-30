package com.solace.spring_cloud_stream.binder;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.integration.channel.AbstractSubscribableChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.dispatcher.AbstractDispatcher;
import org.springframework.integration.dispatcher.BroadcastingDispatcher;
import org.springframework.integration.dispatcher.MessageDispatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.util.Assert;

import com.solace.spring_cloud_stream.binder.properties.JcsmpConsumerProperties;
import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.JCSMPSession;
import com.solacesystems.jcsmp.SessionEventArgs;
import com.solacesystems.jcsmp.SessionEventHandler;
import com.solacesystems.jcsmp.XMLMessage;
import com.solacesystems.jcsmp.XMLMessageConsumer;
import com.solacesystems.jcsmp.XMLMessageListener;

/**
 * Listen to Solace messages and forward to Spring Integration framework
 * @author michussey
 *
 */
public class InputMessageChannelAdapter extends AbstractSubscribableChannel implements XMLMessageListener, MessageProducer, SessionEventHandler {

	/**
	 * @return the session
	 */
	public JCSMPSession getSession() {
		return session;
	}

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
		logger.info("Channel ["+this.channelName+"] setOutputChannel to "+_outputChannel+":"+_outputChannel.getClass().getName());
		outputChannel = _outputChannel;
	}
	/**
		// see if this fixes issue with embedded headers
        DirectFieldAccessor dfa = new DirectFieldAccessor(outputChannel);
        AbstractDispatcher dispatcher = (AbstractDispatcher) dfa.getPropertyValue("dispatcher");
        dfa = new DirectFieldAccessor(dispatcher);
        @SuppressWarnings("unchecked")
        Set<MessageHandler> handlers = (Set<MessageHandler>) dfa.getPropertyValue("handlers");
        // there should be exactly one handler
        MessageHandler handler = handlers.iterator().next();
        dfa = new DirectFieldAccessor(handler);
        dfa.setPropertyValue("embedHeaders", false);
        logger.warn("Disabled embedHeaders");
 		
	}*/

	@Override
	public MessageChannel getOutputChannel() {
		logger.info("Channel ["+this.channelName+"] getOutputChannel: "+this.outputChannel);
		return this.outputChannel;
		/**
		if (this.outputChannelName != null) {
			synchronized (this) {
				if (this.outputChannelName != null) {
					this.outputChannel = getChannelResolver().resolveDestination(this.outputChannelName);
					logger.info("Channel ["+this.channelName+"] resolved OutputChannel: "+outputChannel);
					if (this.outputChannel != null)
					{
						this.outputChannelName = null;
					}
				}
			}
		}
		return this.outputChannel;
		*/
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
	@SuppressWarnings("rawtypes")
	@Override
	public void onReceive(BytesXMLMessage solaceMessage) {
		if (logger.isInfoEnabled())
			logger.info("Channel ["+this.channelName+"] received message on "+solaceMessage.getDestination().getName()
			+" payload size: "+solaceMessage.getContentLength()
			+"/"+solaceMessage.getAttachmentContentLength()
			+" type="+solaceMessage.getClass().getName());
		Message<?> springMessage = null;
		try {
			springMessage = new SolaceMessage(solaceMessage);
			outputChannel.send(springMessage);
		} catch (SolaceBinderException e) {
			logger.error(e.toString());
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
	 * from {@link AbstractSubscribableChannel}
	 *
	@Override
	protected boolean doSend(Message<?> message, long timeout) {
		logger.info("doSend:timeout="+timeout+" invoked for "+message);		
		return super.doSend(message, timeout);
	}
	**
	 * This method is called if 
	 *
	@Override
	public boolean send(Message<?> message, long timeout) {
		boolean retVal = true;
		logger.info("send:timeout="+timeout+" invoked for "+message);
		
		if (outputChannel instanceof OutputMessageChannelAdapter) {
			OutputMessageChannelAdapter solaceOutput = (OutputMessageChannelAdapter) outputChannel;
			@SuppressWarnings("unchecked")
			SolaceMessage<?> sMessage = new SolaceMessage(message);
			try {
				boolean hasOverrides = sMessage.toSolace();
				XMLMessage solaceMessage = sMessage.getSolaceMessage();
				
				solaceOutput.handleMessage(message);
				
			} catch (SolaceBinderException e) {
				retVal = false;
				logger.warn("Couldn't send reply:"+e.toString());
			}
		}
		else
		{
			if (outputChannel == null) {
				logger.error("Channel ["+this.channelName+"] Can't send reply as outputChannel is null");
			}
			else
			{
				logger.warn("Channel ["+this.channelName+"] outputChannel is a "+outputChannel.getClass()+" "+outputChannel);
			}
		}
		return retVal;
	}*/

}
