package com.sen.test.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
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
import com.sen.test.utils.ExcelUtil;

public class ApiTest {

	private String rootUrl;

	private boolean rooUrlEndWithSlash = false;

	private Header[] publicHeaders;

	private List<ApiDataBean> dataList;

	@BeforeSuite
	public void init() throws DocumentException, IOException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException {
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
		dataList = ExcelUtil.readExcel(ApiDataBean.class,
				System.getProperty("user.dir") + "/api-data.xls", "Sheet1");

	}

	@DataProvider(name = "methods")
	public Iterator<Object[]> getMethodData() {
		List<Object[]> dataProvider = new ArrayList<Object[]>();
		for (ApiDataBean data : dataList) {
			// [method][errorMsg]
			Object[] objs = new Object[2];
			String method_name = data.getMethod();

			if ("post".equalsIgnoreCase(method_name)) {
				HttpPost postMethod = new HttpPost(parseUrl(data.getUrl()));
				postMethod.setHeaders(publicHeaders);
				try {
					HttpEntity entity;
					entity = new StringEntity(data.getParam(), "UTF-8");
					postMethod.setEntity(entity);
				} catch (UnsupportedEncodingException e) {
					objs[1] = "参数转换失败:" + e.getMessage();
				}
				objs[0] = postMethod;
			} else {
				HttpGet getMethod = new HttpGet(parseUrl(data.getUrl()));
				// http://www.pm25.in/api/querys/pm2_5.json?city=zhuhai&token=5j1znBVAsnSf5xQyNQyq
				// "apistore/aqiservice/citylist"
				getMethod.setHeaders(publicHeaders);
				objs[0] = getMethod;
			}
			dataProvider.add(objs);
		}
		return dataProvider.iterator();
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

	@Test
	public void Test() {
		for (ApiDataBean api : dataList) {
			System.out.println("description" + api.getDescription());
		}
	}
}
