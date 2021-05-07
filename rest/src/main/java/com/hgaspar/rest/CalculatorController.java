package com.hgaspar.rest;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<Calculation> add(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = generateCalculation(a, b, '+', correlation);
		Message message = prepareToSendRequest(correlation, calculation);
		String result = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				message, correlation);

		
	
		return createResponse(correlation, result, calculation);


	}

	@GetMapping("/sub/{a}/{b}")
	public ResponseEntity<Calculation> substract(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation;
	
			calculation = generateCalculation(a, b, '-', correlation);
		
		Message message = prepareToSendRequest(correlation, calculation);
		String result = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				message, correlation);

		return createResponse(correlation, result, calculation);

	}

	

	@GetMapping("/multiply/{a}/{b}")
	public ResponseEntity<Calculation> multiply(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = generateCalculation(a, b, '*', correlation);
		Message message = prepareToSendRequest(correlation, calculation);
		String result = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				message, correlation);

		return createResponse(correlation, result, calculation);

	}

	@GetMapping("/divide/{a}/{b}")
	public ResponseEntity<Calculation> divide(@PathVariable BigDecimal a, @PathVariable BigDecimal b) {

		CorrelationData correlation = ConfigTemplate();
		Calculation calculation = generateCalculation(a, b, '/', correlation);
		Message message = prepareToSendRequest(correlation, calculation);

		String result = (String) template.convertSendAndReceive(MessagingConfig.EXCHANGE, MessagingConfig.ROUTING_KEY,
				message, correlation);
		return createResponse(correlation, result, calculation);
	
	}

	private CorrelationData ConfigTemplate() {
		CorrelationData correlation = new CorrelationData();
		correlation.setId(UUID.randomUUID().toString());
		template.setUseDirectReplyToContainer(false);
		template.setCorrelationKey(correlation.getId());
		template.setMessageConverter(new Jackson2JsonMessageConverter());

		return correlation;
	}

	private ResponseEntity<Calculation> createResponse(CorrelationData correlation, String result, Calculation calc) {
		HttpHeaders header = new HttpHeaders();
		header.add("requestId", correlation.getId());
		header.add("Content-Type","application/json");
			
		try {
			calc.setResult(BigDecimal.valueOf(Double.valueOf(result)));
		} catch (NumberFormatException e) {
			//continue
		}
		
		ResponseEntity<Calculation> responseEntity = new ResponseEntity<Calculation>(calc, header, HttpStatus.OK);
		logger.info("[WEB]: Processando o resultado do cálculo {} ", calc.getResult()!=null? calc: result);
		return responseEntity;
	}

	private Calculation generateCalculation(BigDecimal a, BigDecimal b, char sign, CorrelationData correlation) {
		Calculation calculation = new Calculation(a, b, sign, correlation.getId());
		return calculation;
	}
	

	private Message prepareToSendRequest(CorrelationData correlation, Calculation calculation) {
		
		MDC.put("requestId", correlation.getId());
		logger.info("[WEB]: Enviando o calculo {} ", calculation);
		MessageProperties properties = new MessageProperties();
		properties.setCorrelationId(correlation.getId());
		properties.setHeader("CalculationId", correlation.getId());

		Message message = template.getMessageConverter().toMessage(calculation, properties);
		return message;
	}

}
