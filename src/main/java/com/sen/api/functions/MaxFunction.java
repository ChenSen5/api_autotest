package com.sen.api.functions;

import java.math.BigDecimal;

public class MaxFunction implements Function{

	@Override
	public String execute(String[] args) {
		BigDecimal maxValue=new BigDecimal(args[0]);
		for(String numerial :args){
			maxValue = maxValue.max(new BigDecimal(numerial));
		}
		return String.valueOf(maxValue);
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "max";
	}

}
