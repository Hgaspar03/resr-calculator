package com.hgaspar.calculator;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.hgaspar.calculator.consumer.CalculatorConsumer;



@SpringBootApplication
@EnableScheduling
public class CalculatorMicroservice {
	
	
	 @Profile("usage_message")
	    @Bean
	    public CommandLineRunner usage() {
	        return args -> {
	        	
	            System.out.println(
	            	"This app uses Spring Profiles to control its behavior");
	            System.out.println("Sample usage: java -jar"
	            		+ "  calculator.0.0.1.SNAPSHOT.jar"
	            		+ " --spring.profiles.active=calculator_exchange,server");
	        };
	    }
	 
	   @Bean
	    public Jackson2JsonMessageConverter converter() {
	        return new Jackson2JsonMessageConverter();
	    }


	    @Profile("!usage_message")
	    @Bean
	    public CommandLineRunner tutorial() {
	        return new CalculatorConsumer();
	    }



	public static void main(String[] args) {
		SpringApplication.run(CalculatorMicroservice.class, args);
		
		System.out.println("Iniciando o processador de calculo...");
	

	}

}
