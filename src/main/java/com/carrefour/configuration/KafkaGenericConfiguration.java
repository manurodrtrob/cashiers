package com.carrefour.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;


@Data
@Configuration
@ConfigurationProperties(prefix = "kafka")
public class KafkaGenericConfiguration {

	private String bootstrapServers;
	
	private String schemaRegistryUrl;

	private boolean missingTopicsFatal;

	private String headerEvent;

	private String ack;

	private boolean enableAutoCommit;

	private boolean specificAvroReader;

}
