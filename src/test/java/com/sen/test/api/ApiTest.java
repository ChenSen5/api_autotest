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
	public Iterator<Object[]> getMethodData(){
		List<Object[]> dataProvider = new ArrayList<Object[]>();
		for (ApiDataBean data : dataList) {
			dataProvider.add(new Object[]{data});
		}
		return dataProvider.iterator();
	}

	@Test(dataProvider = "methods")
	public void test(ApiDataBean apiDataBean)
			throws ClientProtocolException, IOException {
		HttpUriRequest method = parseHttpRequest(apiDataBean.getUrl(),apiDataBean.getMethod(),apiDataBean.getParam());
		System.out.println(method.getURI().toString());
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(method);
		HttpEntity respEntity = response.getEntity();
		String responseData = DecodeUtil.decodeUnicode(EntityUtils
				.toString(respEntity));
		System.out.println("data:" + responseData);
		Assert.assertTrue(responseData.contains(apiDataBean.getVerify()));
		// String value = JsonPath.read(responseData, "");
	}

	private HttpUriRequest parseHttpRequest(String url,String method,String param) throws UnsupportedEncodingException{
		if ("post".equalsIgnoreCase(method)) {
			HttpPost postMethod = new HttpPost(parseUrl(url));
			postMethod.setHeaders(publicHeaders); 
				HttpEntity entity = new StringEntity(param, "UTF-8");
				postMethod.setEntity(entity);
			return postMethod;
		} else {
			
			HttpGet getMethod = new HttpGet(parseUrl(url));
			getMethod.setHeaders(publicHeaders);
			return getMethod;
		}//delete put....
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
