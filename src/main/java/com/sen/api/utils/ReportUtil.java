package com.sen.api.utils;

import org.testng.Reporter;

import com.sen.api.utils.ReportUtil;
import com.sen.api.utils.StringUtil;

public class ReportUtil {
	private static String reportName = "自动化测试报告";
	public static void log(String msg) {
		Reporter.log(msg, true);
	}

	public static String getReportName() {
		return reportName;
	}

	public static void setReportName(String reportName) {
		if(StringUtil.isNotEmpty(reportName)){
			ReportUtil.reportName = reportName;
		}
	}
}

