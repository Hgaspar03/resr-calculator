package com.hgaspar.calculator.consumer;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hgaspar.calculator.CalculatorService;
import com.hgaspar.calculator.config.MessagingConfig;
import com.hgaspar.calculator.model.Calculation;

@Component
public class CalculatorConsumer implements CommandLineRunner {

	Logger logger  = LoggerFactory.getLogger(CalculatorConsumer.class);

@Autowired
RabbitTemplate tamplete;


	private CalculatorService calculatorService = new CalculatorService();

	@RabbitListener(queues = MessagingConfig.QUEUE_IN,
			messageConverter = "converter", replyContentType = "application/json")
	public String consumeMessageFromQueue(Calculation calc){
			
        logger.info("SERVER: Executando o calculo {} com ID: {} ", calc, calc.getCalculationId());
        
	    Calculation calcWithResult;
		try {
			calcWithResult = calculatorService.calculate(calc);
		} catch (Throwable e) {
			logger.error("calculo {}, teve o erro {}, com ID: {}", calc, e.getMessage(), calc.getCalculationId());
			return "Erro: Não é possívrl dividir por zero";
			
		}

		
		logger.info("SERVER: calculo {}, teve o resultado {} com ID: {}", calc, calc.getResult(), calc.getCalculationId());


		return  String.valueOf( calcWithResult.getResult());
		

	}

	@Override
	public void run(String... args) throws Exception {
        logger.info("Executando o motor de calculo ...");
	}

}
