package com.sen.api.utils;

import org.testng.Assert;

public class AssertUtil {

	public static void contains(String source, String search) {
		Assert.assertTrue(source.contains(search),
				String.format("期待'%s'包含'%s'，实际为不包含.", source, search));
	}
	
	public static void notContains(String source, String search) {
		Assert.assertFalse(source.contains(search),
				String.format("期待'%s'不包含'%s'，实际为包含.", source, search));
	}
}
