package com.sen.api.functions;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sen.api.utils.StringUtil;

public class DateFunction  implements Function{

	@Override
	public String execute(String[] args) {
		if (args.length == 0 ||StringUtil.isEmpty(args[0])) {
			return String.format("%s", new Date().getTime());
		} else {
			return getCurrentDate("yyyy-MM-dd");
		}
	}

	private String getCurrentDate(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		String str = format.format(new Date());
		return str;
	}
	
	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "date";
	}

}
