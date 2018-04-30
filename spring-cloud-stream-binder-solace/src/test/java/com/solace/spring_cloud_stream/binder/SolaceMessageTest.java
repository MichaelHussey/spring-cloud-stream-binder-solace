/**
 * 
 */
package com.solace.spring_cloud_stream.binder;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;

import com.solacesystems.jcsmp.BytesXMLMessage;
import com.solacesystems.jcsmp.JCSMPFactory;
import com.solacesystems.jcsmp.MapMessage;
import com.solacesystems.jcsmp.SDTException;
import com.solacesystems.jcsmp.SDTMap;
import com.solacesystems.jcsmp.TextMessage;
import com.solacesystems.jcsmp.XMLMessage;

/**
 * @author michussey
 *
 */
public class SolaceMessageTest {
	
	/**
	 * Handling of String payloads when mapping Spring -> Solace
	 * @throws SolaceBinderException
	 */
	@Test
	public void testSolace2Spring_String() throws SolaceBinderException {
		String payload = "Just some string data";
		TextMessage solaceMessage = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
		solaceMessage.setText(payload);
		String retrievePayload = solaceMessage.getText();
		assertTrue(retrievePayload.equals(payload));
		
		// Now convert to SolaceMessage
		SolaceMessage<String> sm = new SolaceMessage<String>(solaceMessage);
		
		String convertedPayload = sm.getPayload();
		System.err.println("Converted payload ["+convertedPayload+"] length="+convertedPayload.length());
		System.err.println("Original  payload ["+payload+"] length="+payload.length());
		assertTrue(convertedPayload.equals(payload));
	}
	/**
	 * Handling of Byte payloads when mapping Byte -> Solace
	 * @throws SolaceBinderException
	 */
	@Test
	public void testSolace2Spring_Byte() throws SolaceBinderException {
		String appId = "AppId";
		String payload = "Just some string data";
		BytesXMLMessage solaceMessage = JCSMPFactory.onlyInstance().createMessage(BytesXMLMessage.class);
		solaceMessage.setApplicationMessageId(appId);

		// Handle empty payloads
		// Now convert to SolaceMessage
		SolaceMessage<Byte[]> sm = new SolaceMessage<Byte[]>(solaceMessage);
		String newAppId = (String) sm.getHeaders().get(SolaceBinderConstants.FIELD_APPLICATION_MESSAGE_ID);
		assertTrue(newAppId.equals(appId));
		
		solaceMessage.writeAttachment(payload.getBytes());
		
		SolaceMessage<byte[]> sm1 = new SolaceMessage<byte[]>(solaceMessage);
		byte[] convertedPayload = sm1.getPayload();
		System.err.println("Converted payload ["+convertedPayload+"] length="+convertedPayload.length);
		System.err.println("Original  payload ["+payload+"] length="+payload.length());
		assertEquals(convertedPayload.length, payload.length());
		String convertedString = new String(convertedPayload);
		assertTrue(convertedString.equals(payload));
	}
	/**
	 * Handling of String payloads when mapping Spring -> Solace
	 * @throws SolaceBinderException
	 */
	@Test
	public void testSpring2Solace_String() throws SolaceBinderException {
		String payload = "This is a String payload";
		Message<?> springMessage = Utils.buildSpringMessage(payload, null);
		
		SolaceMessage<String> sm = new SolaceMessage<String>(springMessage);
		
		// we haven't overriden the destination, so toSolace() returns false
		assertFalse(sm.toSolace());
		
		assertNotNull(sm.getPayload());
		assertTrue(payload.equals(sm.getPayload()));
		
		String newDestName = "override/the/topic/name";
		HashMap<String, Object> headerMap = new HashMap<String, Object> ();
		headerMap.put(SolaceBinderConstants.FIELD_DYNAMICDESTINATION_NAME, newDestName);
		springMessage = Utils.buildSpringMessage(payload, headerMap);
		sm = new SolaceMessage<String>(springMessage);
		assertTrue(sm.toSolace());
		
		// Now check that the payload is encoded correctly in the Solace message
		XMLMessage sMessage = sm.getSolaceMessage();		
		assertTrue(sMessage instanceof TextMessage);
		TextMessage tMessage = (TextMessage) sMessage;
		assertTrue(tMessage.getText().equals(payload));
	}
	/**
	 * Handling of Map payloads when mapping Spring -> Solace
	 * @throws SolaceBinderException
	 */
	@Test
	public void testSpring2Solace_Map() throws SolaceBinderException, SDTException {
		String payload = "This is a String payload";		
		HashMap<String, Object> payloadMap = new HashMap<String, Object> ();
		payloadMap.put("payload", payload);
		Message<?> springMessage = Utils.buildSpringMessage(payloadMap, null);
		
		SolaceMessage<String> sm = new SolaceMessage<String>(springMessage);
		
		// we haven't overriden the destination, so toSolace() returns false
		assertFalse(sm.toSolace());
		
		String newDestName = "override/the/topic/name";
		HashMap<String, Object> headerMap = new HashMap<String, Object> ();
		headerMap.put(SolaceBinderConstants.FIELD_DYNAMICDESTINATION_NAME, newDestName);
		springMessage = Utils.buildSpringMessage(payloadMap, headerMap);
		sm = new SolaceMessage<String>(springMessage);

		// we have overriden the destination, so toSolace() returns true
		assertTrue(sm.toSolace());
		
		XMLMessage sMessage = sm.getSolaceMessage();
		assertTrue(sMessage instanceof MapMessage);
		MapMessage mMessage = (MapMessage) sMessage;
		SDTMap sdtMap = mMessage.getMap();
		assertNotNull(sdtMap);
		assertTrue(sdtMap.get("payload").equals(payload));
	}

	/**
	 * Handling of Object payloads when mapping Spring -> Solace
	 * @throws SolaceBinderException
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	@Test
	public void testSpring2Solace_Object() throws SolaceBinderException, SDTException, IOException, ClassNotFoundException {
		Payload payload = new Payload("This is a String payload");		
		Message<?> springMessage = Utils.buildSpringMessage(payload, null);
		
		SolaceMessage<String> sm = new SolaceMessage<String>(springMessage);
		
		// we haven't overriden the destination, so toSolace() returns false
		assertFalse(sm.toSolace());
		
		String newDestName = "override/the/topic/name";
		HashMap<String, Object> headerMap = new HashMap<String, Object> ();
		headerMap.put(SolaceBinderConstants.FIELD_DYNAMICDESTINATION_NAME, newDestName);
		springMessage = Utils.buildSpringMessage(payload, headerMap);
		sm = new SolaceMessage<String>(springMessage);

		// we have overriden the destination, so toSolace() returns true
		assertTrue(sm.toSolace());
		
		XMLMessage sMessage = sm.getSolaceMessage();
		assertTrue(sMessage instanceof BytesXMLMessage);
		BytesXMLMessage bMessage = (BytesXMLMessage) sMessage;
		byte[] payloadBytes = new byte[bMessage.getContentLength()];
		bMessage.readContentBytes(payloadBytes);
		assertTrue(payloadBytes.length > 0);
		ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(payloadBytes));
		Object payloadObject = objectInputStream.readObject();
		assertNotNull(payloadObject);
		assertTrue(payloadObject instanceof Payload);
		Payload deserializedPayload = (Payload) payloadObject;
		assertTrue(deserializedPayload.equals(payload));
	}
	
	@Test
	public void testToString() {
		String payload = "Just a payload";
		SolaceMessage<String> sm = new SolaceMessage<String>(payload, null);
		
		String sm2string = sm.toString();
		assertNotNull(sm2string);
	}
}
