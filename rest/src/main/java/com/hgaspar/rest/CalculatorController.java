package com.hgaspar.rest;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.hgaspar.rest.config.MessagingConfig;
import com.hgaspar.rest.model.Calculation;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CalculatorController {

	@Autowired
	private RabbitTemplate template;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@GetMapping("/add/{a}/{b}")
	public ResponseEntity<String> add(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = new Calculation(a, b, '+', correlation.getId());
		logger.info("WEB: Enviando o calculo {} com Id: {} ", calculation, correlation.getId());
		String value = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				calculation);
		ResponseEntity<String> responseEntity = createResponse(correlation, value);
		return responseEntity;

	}

	@GetMapping("/sub/{a}/{b}")
	public ResponseEntity<String> substract(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = generateCalculation(a, b, '-', correlation);

		MessageProperties properties = new MessageProperties();
		properties.setCorrelationId(correlation.getId());
		properties.setHeader("CalculationId", correlation.getId());
		properties.setHeader("__TypeId__", "com.hgaspar.calculator.model.Calculation");

		Message message = template.getMessageConverter().toMessage(calculation, properties);
		String value = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				message, correlation);

		return createResponse(correlation, value);

	}

	@GetMapping("/multiply/{a}/{b}")
	public ResponseEntity<String> multiply(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = new Calculation(a, b, '*', correlation.getId());

		String value = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				calculation, correlation);

		ResponseEntity<String> responseEntity = createResponse(correlation, value);
		return responseEntity;
	}

	@GetMapping("/divide/{a}/{b}")
	public ResponseEntity<String> divide(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = new Calculation(a, b, '/', correlation.getId());

		String value = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				calculation, correlation);
		ResponseEntity<String> responseEntity = createResponse(correlation, value);
		return responseEntity;
	}

	private CorrelationData ConfigTemplate() {
		CorrelationData correlation = new CorrelationData();
		correlation.setId(UUID.randomUUID().toString());
		template.setUseDirectReplyToContainer(false);
		template.setCorrelationKey(correlation.getId());
		template.setMessageConverter(new Jackson2JsonMessageConverter());

		return correlation;
	}

	private ResponseEntity<String> createResponse(CorrelationData correlation, String value) {
		HttpHeaders header = new HttpHeaders();
		header.add("correlationId", correlation.getId());
		ResponseEntity<String> responseEntity = new ResponseEntity<String>("Result: " + value, header, HttpStatus.OK);
		logger.info("WEB: processando o resultadoo do calculo {} com Id: {} ", value, correlation.getId());
		return responseEntity;
	}

	private Calculation generateCalculation(BigDecimal a, BigDecimal b, char sign, CorrelationData correlation) {
		Calculation calculation = new Calculation(a, b, sign, correlation.getId());
		return calculation;
	}

}
