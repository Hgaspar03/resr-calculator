package com.hgaspar.calculator.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.hgaspar.calculator.consumer.CalculatorConsumer;
import com.hgaspar.calculator.model.Calculation;


@Profile({"calculation",MessagingConfig.ROUTING_KEY})
@Configuration
public class CalculationConfig {

	

		@Bean
		public Queue queue() {
			return new Queue(MessagingConfig.QUEUE_IN);
		}


	 @Profile("server")
	    @Bean
	    public CalculatorConsumer consumer() {
	        return new CalculatorConsumer();
	    }
	
	

}





   