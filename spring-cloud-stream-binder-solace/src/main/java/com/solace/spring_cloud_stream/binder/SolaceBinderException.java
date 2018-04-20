package com.solace.spring_cloud_stream.binder;

import org.apache.commons.lang.exception.ExceptionUtils;

public class SolaceBinderException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1384976464234170430L;

	public SolaceBinderException() {
	}
	
	public SolaceBinderException(String message) {
		super(message);
	}

	public SolaceBinderException(String message, Throwable  e) {
		super(message, e);
	}
	
	public SolaceBinderException(Throwable  e) {
		super(e);
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder("{\"SolaceBinderException\": {\"message\":");
		sb.append(this.getMessage());
		sb.append(", \"stacktrace\":\"");
		sb.append(ExceptionUtils.getStackTrace(this));
		sb.append("}}");
		return sb.toString();
	}

}
