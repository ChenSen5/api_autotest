package com.sen.test.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ApiTest {

	@DataProvider(name = "methods")
	public Object[][] getMethodData() {
		//[method][errorMsg]
		Object[][] methods = new Object[1][2];
		HttpPost postMethod = new HttpPost("http://apis.baidu.com/chazhao/shorturl/shorturl");
		postMethod.setHeader("apikey", "84ef029c7ba6178bf1e393e07895f8b8");
		try {
			HttpEntity entity;
			entity = new StringEntity("{\"type\": 1, \"url\": [\"http://www.baidu.com\"]}", "UTF-8");
			postMethod.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			methods[0][1]="参数转换失败:"+e.getMessage();
		}
		methods[0][0] = postMethod;
		return methods;
	}

	@Test(dataProvider = "methods")
	public void test(HttpRequestBase method, String errorMsg) throws ClientProtocolException, IOException {
		
		Assert.assertNull(errorMsg, errorMsg);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(method);
		System.out.println("data:" + EntityUtils.toString(response.getEntity()));
	}
}
