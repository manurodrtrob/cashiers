package com.carrefour.configuration;


import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Data
@Configuration
public class KafkaTopicConfiguration {

	private String topic;

	private String group;

	private String requestPerConnection;

	private String retries;

	private String retryTime;

	private String workers;
	
	private Boolean isEnabled;

}
