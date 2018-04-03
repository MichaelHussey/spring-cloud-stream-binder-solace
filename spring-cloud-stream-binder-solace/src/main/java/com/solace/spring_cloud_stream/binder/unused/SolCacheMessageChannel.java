package com.solace.spring_cloud_stream.binder.unused;

import org.springframework.messaging.MessageHandler;

import com.solace.spring_cloud_stream.binder.InputMessageChannelAdapter;
import com.solacesystems.jcsmp.CacheLiveDataAction;
import com.solacesystems.jcsmp.CacheSession;
import com.solacesystems.jcsmp.CacheSessionProperties;
import com.solacesystems.jcsmp.JCSMPException;
import com.solacesystems.jcsmp.JCSMPStreamingPublishEventHandler;
import com.solacesystems.jcsmp.XMLMessageProducer;

public class SolCacheMessageChannel extends InputMessageChannelAdapter implements JCSMPStreamingPublishEventHandler {

	protected CacheSession cacheSession;
	
	protected String cacheName;

	private XMLMessageProducer cacheRequestProducer;

	private Long requestId;
	
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	public SolCacheMessageChannel(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean subscribe(MessageHandler handler) {
		super.subscribe(handler);
		
		// Requests the most recent message on the topic, using default timeout 10sec
		CacheSessionProperties props = new CacheSessionProperties(cacheName);
		try {
			cacheSession = session.createCacheSession(props);
			
			cacheRequestProducer = session.getMessageProducer(this);
			
			cacheSession.sendCacheRequest(requestId, destination.getTopic(), false, CacheLiveDataAction.FULFILL);
			
		} catch (JCSMPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}

	@Override
	public void handleError(String arg0, JCSMPException arg1, long arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void responseReceived(String arg0) {
		// TODO Auto-generated method stub
		
	}	

}
