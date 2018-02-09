package com.solace.spring_cloud_stream.binder;

public class SolaceBinderConstants {

	public static final String BINDER_NAME = "spring-cloud-stream-binder-solace";

	public static final String BINDER_VERSION = "0.1";

	/**
	 * Solace message fields
	 */
	public static final String FIELD_APPLICATION_MESSAGE_ID = "ApplicationMessageId";
	public static final String FIELD_APPLICATION_MESSAGE_TYPE = "ApplicationMessageType";
	public static final String FIELD_APPLICATION_DELIVERY_MODE = "DeliveryMode";
	public static final String FIELD_USER_PROPERTIES = "UserProperties";
	public static final String FIELD_ATTACHMENT = "Attachment";
	public static final String FIELD_ATTACHMENT_CONTENT_LENGTH = "AttachmentContentLength";
	public static final String FIELD_CACHE_REQUEST_ID = "CacheRequestId";
	public static final String FIELD_CONSUMER_ID_LIST = "ConsumerIdList";
	public static final String FIELD_CONTENT_LENGTH = "ContentLength";
	public static final String FIELD_CORRELATION_ID = "CorrelationId";
	public static final String FIELD_CORRELATION_KEY = "CorrelationKey";
	public static final String FIELD_DESTINATION = "Destination";
	public static final String FIELD_EXPIRATION = "Expiration";
	public static final String FIELD_HTTP_CONTENT_ENCODING = "HttpContentEncoding";
	public static final String FIELD_HTTP_CONTENT_TYPE = "HttpContentType";
	public static final String FIELD_MESSAGE_ID = "MessageId";
	public static final String FIELD_MESSAGE_ID_LONG = "MessageIdLong";
	public static final String FIELD_RECEIVE_TIMESTAMP = "ReceiveTimestamp";
	public static final String FIELD_REDELIVERED = "IsRedelivered";
	public static final String FIELD_REPLYTO = "ReplyTo";
	public static final String FIELD_SENDERID = "SenderId";
	public static final String FIELD_SENDER_TIMESTAMP = "SenderTimestamp";
	public static final String FIELD_SEQUENCE_NUMBER = "SequenceNumber";

	public static final String FIELD_DESTINATION_NAME = "DestinationName";
}
