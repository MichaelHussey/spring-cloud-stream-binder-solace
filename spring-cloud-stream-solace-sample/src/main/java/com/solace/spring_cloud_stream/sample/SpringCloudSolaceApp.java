package com.solace.spring_cloud_stream.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Transformer;

import com.solace.spring_cloud_stream.binder.SolaceBinderConfiguration;

@SpringBootApplication
@Import(SolaceBinderConfiguration.class)
public class SpringCloudSolaceApp {
	
	/**
	@Transformer(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	public String transform(String in) {
		return in + " world";
	}*/

    static final String LISTEN_TOPIC = "test/>";

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringCloudSolaceApp.class, args);
	}

}
