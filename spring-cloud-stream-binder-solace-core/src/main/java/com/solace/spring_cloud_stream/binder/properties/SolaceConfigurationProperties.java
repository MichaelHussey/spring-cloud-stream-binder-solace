package com.solace.spring_cloud_stream.binder;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * Properties needed to connect to a Solace Message Router using the JCSMP API
 * Sets default values to the OOB settings for a VMR
 * 
 * @author michussey
 *
 */
@Component
@ConfigurationProperties("solace")
@Validated
public class SolaceConfigurationProperties {
	
	/**
	 * 
	 */
	@NotNull
	private String smfHost;
	public String getSmfHost() {
		return smfHost;
	}
	public void setSmfHost(String smfHost) {
		this.smfHost = smfHost;
	}
	
	/**
	 * 
	 */
	private String msgVpn = "default";
	public String getMsgVpn() {
		return msgVpn;
	}
	public void setMsgVpn(String msgVpn) {
		this.msgVpn = msgVpn;
	}
	
	/**
	 * 
	 */
	private String username = "default";
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * 
	 */
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	/**
	 * 
	 */
	@NotNull
	private String topicName;
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

}
