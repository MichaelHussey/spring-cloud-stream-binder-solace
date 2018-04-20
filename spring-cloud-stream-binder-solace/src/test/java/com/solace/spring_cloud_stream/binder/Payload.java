package com.solace.spring_cloud_stream.binder;

import java.io.Serializable;

public class Payload implements Serializable {
	private static final long serialVersionUID = -2226824804624450003L;
	
	protected String payload;

	public Payload(String payload) {
		this.payload = payload;
	}
	public boolean equals(Object test)
	{
		boolean retVal = false;
		if(test instanceof Payload && test != null) {
			return this.payload.equals(((Payload) test).payload);
		}
		return retVal;
	}
}