package com.carrefour.kafka.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

import com.carrefour.configuration.KafkaCashierTopicsConfiguration;
import com.carrefour.configuration.KafkaGenericConfiguration;
import com.carrefour.smartpos.cashier;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;


@Component
@Configuration
public class KafkaCashierJsonProducer {

	@Autowired
	KafkaGenericConfiguration genericConfiguration;
	
	@Autowired
	KafkaCashierTopicsConfiguration cashierTopicsConfiguration; 

	@Bean
	public ProducerFactory<String, cashier> cashierProducerFactory() {
	
		final Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, genericConfiguration.getBootstrapServers());
		configProps.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, genericConfiguration.getSchemaRegistryUrl());

		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
		configProps.put(ProducerConfig.ACKS_CONFIG, genericConfiguration.getAck());
	
		// Retry
		configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, cashierTopicsConfiguration.getCashier().getRequestPerConnection());
		configProps.put(ProducerConfig.RETRIES_CONFIG, cashierTopicsConfiguration.getCashier().getRetries());
		configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, cashierTopicsConfiguration.getCashier().getRetryTime());
	
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	@Bean
	public KafkaTemplate<String, cashier> posJsonKafkaTemplate() {
		return new KafkaTemplate<>(cashierProducerFactory());
	}
	
}
