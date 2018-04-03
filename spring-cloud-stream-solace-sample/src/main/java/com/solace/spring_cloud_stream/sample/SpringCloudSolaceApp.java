package com.solace.spring_cloud_stream.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import com.solace.spring_cloud_stream.binder.SolaceBinderConfiguration;

@SpringBootApplication
@Import(SolaceBinderConfiguration.class)
public class SpringCloudSolaceApp {
	
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(SpringCloudSolaceApp.class, args);
	}

}
