package com.sen.api.functions;

import com.sen.api.utils.RandomUtil;

public class RandomStrArrFucntion implements Function{

	@Override
	public String execute(String[] args) {
		// 第一个参数为数组长度 即生成参数个数
		// 第二个参数为参数长度
		// 第三个参数为是否只有数字标志
		int len = args.length;
		int arrLength = 1; // 默认数组长度为1
		int length = 6;// 默认参数长度为6
		boolean flag = false;// 默认为false
		if (len == 1) {
			arrLength = Integer.valueOf(args[0]);
		} else if (len == 2) {
			arrLength = Integer.valueOf(args[0]);
			length = Integer.valueOf(args[1]);
		} else if (len == 3) {
			arrLength = Integer.valueOf(args[0]);
			length = Integer.valueOf(args[1]);
			flag = Boolean.valueOf(args[1]);
		}
		return RandomUtil.getRandomArr(arrLength, length, flag);
	}

	@Override
	public String getReferenceKey() {
		// TODO Auto-generated method stub
		return "randomStrArr";
	}

}
