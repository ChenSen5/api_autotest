package com.sen.api.functions;

import java.math.BigDecimal;

public class SubFunction implements Function{

	@Override
	public String execute(String[] args) {
		BigDecimal value = new BigDecimal(args[0]);
		for(int index=1;index<args.length;index++){
			value =value.subtract(new BigDecimal(args[index]));
		}
		return String.valueOf(value);
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "sub";
	}

}
