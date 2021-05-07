package com.hgaspar.calculator.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfigOut {
	
	
    public static final String QUEUE = "calculator.queue.out";
    public static final String EXCHANGE = "calculator.exchange.out";
    public static final String ROUTING_KEY = "calculator.routingKey.out";

    @Bean
    public Queue queueOut() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchangeOut() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding bindingOut(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converterOut() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate templateOut(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converterOut());
        return rabbitTemplate;
    }

}

