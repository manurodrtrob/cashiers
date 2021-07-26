package com.carrefour.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
 
@Data
@Configuration
@ConfigurationProperties(prefix = "cp.communication")
public class CommunicationConfiguration 
{
	private String storesUrl;
}

