package com.hgaspar.calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.hgaspar.calculator.model.Calculation;

@Service
@Component
public class CalculatorService {
	
	private BigDecimal add (Calculation calc) {
		return calc.getA().add(calc.getB());
	}
	
	
	private BigDecimal substract (Calculation calc) {
		return calc.getA().subtract(calc.getB());
	}
	
	
	private BigDecimal multiply (Calculation calc) {
		return calc.getA().multiply(calc.getB());
	}
	
	
	private BigDecimal divide (Calculation calc) throws Throwable {
		
		BigDecimal result =null;
		 try {
			 result = calc.getA().divide(calc.getB());
		} catch (ArithmeticException  e) {
			 result = calc.getA().divide(calc.getB(), RoundingMode.UP);

			
		}catch (Exception e) {
			throw new Exception( "Não é possível dividir por zero");
		}
		 
		 return result;
	}
	
	public Calculation calculate(Calculation calc) throws Throwable {
		
		BigDecimal result = null;
		switch (calc.getSign()) {
		case '+':
			result = this.add(calc);
			break;
		case '-':
			result = this.substract(calc);
			break;
		case '/':
			result = this.divide(calc);
			break;
		case '*':
			result = this.multiply(calc);
			break;

		default:
			break;
		}
		
		calc.setResult(result);
		return calc;
	}
}
