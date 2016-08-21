package com.sen.api.beans;

/**
 * 基础类
 * 
 * @author chenwx
 *
 */
public class BaseBean {

	private String excelName;

	private String sheetName;

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getExcelName() {
		return excelName;
	}

	public void setExcelName(String excelName) {
		this.excelName = excelName;
	}

}

