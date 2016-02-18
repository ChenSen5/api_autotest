package com.sen.test.api;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

public class ApiTest {
	@Test
	public void test() throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
//		HttpGet getMethod = new HttpGet("http://www.pm25.in/api/querys/pm2_5.json?city=zhuhai&token=5j1znBVAsnSf5xQyNQyq");
//
//		HttpResponse response = client.execute(getMethod);
//		System.out.print("statu" + response.getStatusLine());
//		String responseEntity = EntityUtils.toString(response.getEntity());
//
//		System.out.print("response" + responseEntity);
		HttpPost postMethod = new HttpPost("http://apis.baidu.com/chazhao/shorturl/shorturl");
		postMethod.setHeader("apikey", "84ef029c7ba6178bf1e393e07895f8b8");
		HttpEntity entity = new StringEntity("{\"type\": 1, \"url\": [\"http://www.baidu.com\"]}","UTF-8");
		postMethod.setEntity(entity);
		HttpResponse response = client.execute(postMethod);
		System.out.println("data:"+EntityUtils.toString(response.getEntity()));
	}
}
