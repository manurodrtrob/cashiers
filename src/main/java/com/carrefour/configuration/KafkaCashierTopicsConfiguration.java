package com.carrefour.configuration;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.cashiers")
public class KafkaCashierTopicsConfiguration {

	@NestedConfigurationProperty
	private KafkaTopicConfiguration cashier;


	public KafkaCashierTopicsConfiguration() {

		super();
	}

}
