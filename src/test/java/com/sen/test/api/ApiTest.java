package com.sen.test.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sen.test.utils.DecodeUtil;

public class ApiTest {

	private String rootUrl;

	private boolean rooUrlEndWithSlash = false;

	private Header[] publicHeaders;

	@BeforeSuite
	public void init() throws DocumentException {
		SAXReader reader = new SAXReader();
		String path = System.getProperty("user.dir") + "/api-config.xml";
		Document document = reader.read(path);
		Element rootElement = document.getRootElement();
		rootUrl = rootElement.element("rootUrl").getTextTrim();
		@SuppressWarnings("unchecked")
		List<Element> headerElements = rootElement.element("headers").elements(
				"header");
		List<Header> headers = new ArrayList<Header>();
		rooUrlEndWithSlash = rootUrl.endsWith("/");
		for (Element element : headerElements) {
			Header header = new BasicHeader(element.attributeValue("name"),
					element.attributeValue("value"));
			headers.add(header);
		}
		publicHeaders = headers.toArray(new Header[headers.size()]);
	}

	@DataProvider(name = "methods")
	public Object[][] getMethodData() {
		// ues to test
		String method_name = "get";
		// [method][errorMsg]
		Object[][] methods = new Object[1][2];

		if ("post".equalsIgnoreCase(method_name)) {

			HttpPost postMethod = new HttpPost(
					parseUrl("/chazhao/shorturl/shorturl"));
			postMethod.setHeaders(publicHeaders);
			try {
				HttpEntity entity;
				entity = new StringEntity(
						"{\"type\": 1, \"url\": [\"http://www.baidu.com\"]}",
						"UTF-8");
				postMethod.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				methods[0][1] = "参数转换失败:" + e.getMessage();
			}
			methods[0][0] = postMethod;
		} else {
			HttpGet getMethod = new HttpGet(
					parseUrl("apistore/aqiservice/citylist"));
			// http://www.pm25.in/api/querys/pm2_5.json?city=zhuhai&token=5j1znBVAsnSf5xQyNQyq
			// "apistore/aqiservice/citylist"
			getMethod.setHeaders(publicHeaders);
			methods[0][0] = getMethod;
		}
		return methods;
	}

	@Test(dataProvider = "methods")
	public void test(HttpUriRequest method, String errorMsg)
			throws ClientProtocolException, IOException {

		Assert.assertNull(errorMsg, errorMsg);
		System.out.println(method.getURI().toString());
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(method);
		HttpEntity respEntity = response.getEntity();
		System.out.println("data:"
				+ DecodeUtil.decodeUnicode(EntityUtils.toString(respEntity)));
	}

	private String parseUrl(String shortUrl) {
		if (shortUrl.startsWith("http")) {
			return shortUrl;
		}
		if (rooUrlEndWithSlash == shortUrl.startsWith("/")) {
			if (rooUrlEndWithSlash) {
				shortUrl = shortUrl.replaceFirst("/", "");
			} else {
				shortUrl = "/" + shortUrl;
			}
		}
		return rootUrl + shortUrl;
	};
}
