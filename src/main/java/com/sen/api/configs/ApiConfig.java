package com.sen.api.configs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ApiConfig {

	public ApiConfig(String configFilePath) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document document = reader.read(configFilePath);
		Element rootElement = document.getRootElement();
		
		rootUrl = rootElement.element("rootUrl").getTextTrim();
		excels = rootElement.element("excel").getTextTrim();
		@SuppressWarnings("unchecked")
		List<Element> paramElements = rootElement.element("params").elements(
				"param");
		paramElements.forEach((ele)->{
			params.put(ele.attributeValue("name").trim(),
					ele.attributeValue("value").trim());
		});
		@SuppressWarnings("unchecked")
		List<Element> headerElements = rootElement.element("headers").elements(
				"header");
		headerElements.forEach((ele)->{
			headers.put(ele.attributeValue("name").trim(),
					ele.attributeValue("value").trim());
		});
	}
	private String rootUrl;
	
	private String excels;
	
	private Map<String,String> params = new HashMap<String, String>();
	
	private Map<String,String> headers = new HashMap<String, String>();

	public String getRootUrl() {
		return rootUrl;
	}

	public String getExcels() {
		return excels;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}
	
	
}

