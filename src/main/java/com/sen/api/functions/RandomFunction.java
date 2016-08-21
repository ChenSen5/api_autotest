package com.sen.api.functions;

import com.sen.api.utils.RandomUtil;

public class RandomFunction implements Function {

	@Override
	public String execute(String[] args) {
		int len = args.length;
		int length = 6;// 默认为6
		boolean flag = false;// 默认为false
		if (len > 0) {// 第一个参数字符串长度
			length = Integer.valueOf(args[0]);
		}
		if (len > 1) {// 第二个参数是否纯字符串
			flag = Boolean.valueOf(args[1]);
		}
		return RandomUtil.getRandom(length, flag);
	}

	@Override
	public String getReferenceKey() {
		return "random";
	}

}
