package com.hgaspar.rest.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;



public class Calculation implements Serializable{
	
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -8196424778909828691L;
	
	String calculationId;
	BigDecimal a, b;
	char sign;
	BigDecimal result;
	
	public Calculation() {}
	
	public Calculation(BigDecimal a, BigDecimal b, char sign,String calculationId) {
		this.calculationId = calculationId;
		this.a = a;
		this.b=b;
		this.sign=sign;
	}
	
	

	

	public String getCalculationId() {
		return calculationId;
	}

	public void setCalculationId(String calculationId) {
		this.calculationId = calculationId;
	}

	public BigDecimal getA() {
		return a;
	}

	public void setA(BigDecimal a) {
		this.a = a;
	}

	public BigDecimal getB() {
		return b;
	}

	public void setB(BigDecimal b) {
		this.b = b;
	}

	public char getSign() {
		return sign;
	}

	public void setSign(char sign) {
		this.sign = sign;
	}

	public BigDecimal getResult() {
		return result;
	}

	public void setResult(BigDecimal result) {
		this.result = result;
	}
	
	
	@Override
	public String toString() {
		String result =this.result== null? "":" ="+this.getResult().toPlainString();
		String calculation =  a+ ""+ sign+ "" + b;
		return calculation + result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
		result = prime * result + sign;
		return result;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Calculation other = (Calculation) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (result == null) {
			if (other.result != null)
				return false;
		} else if (!result.equals(other.result))
			return false;
		if (sign != other.sign)
			return false;
		return true;
	}

}
