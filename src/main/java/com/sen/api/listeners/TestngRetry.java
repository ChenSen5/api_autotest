package com.sen.api.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.sen.api.excepions.ErrorRespStatusException;


public class TestngRetry implements IRetryAnalyzer {
	private static int retryCount = 1;
	private static int maxRetryCount = 2;

	public boolean retry(ITestResult result) {
		// TODO Auto-generated method stub
		if (result.getThrowable() instanceof ErrorRespStatusException && retryCount % maxRetryCount != 0) {
//			String message = "running retry for  '" + result.getName()
//					+ "' on class " + this.getClass().getName() + " Retrying "
//					+ retryCount + " times";
			Reporter.setCurrentTestResult(result);		
			
			Reporter.log("RunCount=" + (retryCount + 1));
			retryCount++;
			return true;
		} else {
			resetRetryCount();
			return false;
		}
	}

	public static void resetRetryCount() {
		retryCount = 1;
	}
}

