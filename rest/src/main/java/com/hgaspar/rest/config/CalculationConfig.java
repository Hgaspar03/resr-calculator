package com.hgaspar.rest.config;


import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hgaspar.rest.CalculatorController;


@Profile({"calculation",MessagingConfig.ROUTING_KEY})
@Configuration
public class CalculationConfig {

	
	@Bean
	public Queue queue() {
		return new Queue(MessagingConfig.QUEUE);
	}

		@Bean
		public DirectExchange exchange() {
			return new DirectExchange(MessagingConfig.EXCHANGE);
		}

	
	@Profile("client")
		@Bean
		public CalculatorController client() {
	 	 	return new CalculatorController();
		}

	

	

	}

