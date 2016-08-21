package com.sen.api.functions;

import java.math.BigDecimal;

public class MultiFunction implements Function{

	@Override
	public String execute(String[] args) {
		BigDecimal value=new BigDecimal(1);
		for(String numerial :args){
			value=value.multiply(new BigDecimal(numerial));
		}
		return String.valueOf(value);
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "multi";
	}

}
