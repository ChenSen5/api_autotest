package com.sen.api.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class AutoTestListener extends TestListenerAdapter {

	@Override
	public void onTestSuccess(ITestResult tr) {
		// TODO Auto-generated method stub
		TestngRetry.resetRetryCount();
		super.onTestSuccess(tr);
	}

	public void onTestFailure(ITestResult tr) {
		saveResult(tr);
		super.onTestFailure(tr);
	}

	public void onTestSkipped(ITestResult tr) {
		saveResult(tr);
		super.onTestSkipped(tr);
	}

	private void saveResult(ITestResult tr) {
		Throwable throwable = tr.getThrowable();
		if (null == throwable) {
			return;
		}
		// String imgPath = WebdriverUtil.captureEntirePageScreenshot();
		// log.error("用例执行错误截图：" + imgPath);
		// Reporter.setCurrentTestResult(tr);
		// Reporter.log("path path path path");
	}

	@Override
	public void onFinish(ITestContext testContext) {
		super.onFinish(testContext);

		// List of test results which we will delete later
		ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
		// collect all id's from passed test
		Set<Integer> passedTestIds = new HashSet<Integer>();
		for (ITestResult passedTest : testContext.getPassedTests()
				.getAllResults()) {
			// logger.info("PassedTests = " + passedTest.getName());
			passedTestIds.add(getId(passedTest));
		}

		Set<Integer> failedTestIds = new HashSet<Integer>();
		for (ITestResult failedTest : testContext.getFailedTests()
				.getAllResults()) {
			// logger.info("failedTest = " + failedTest.getName());
			// id = class + method + dataprovider
			int failedTestId = getId(failedTest);

			// if we saw this test as a failed test before we mark as to be
			// deleted
			// or delete this failed test if there is at least one passed
			// version
			if (failedTestIds.contains(failedTestId)
					|| passedTestIds.contains(failedTestId)) {
				testsToBeRemoved.add(failedTest);
			} else {
				failedTestIds.add(failedTestId);
			}
		}

		// finally delete all tests that are marked
		for (Iterator<ITestResult> iterator =

		testContext.getFailedTests().getAllResults().iterator(); iterator
				.hasNext();) {
			ITestResult testResult = iterator.next();

			if (testsToBeRemoved.contains(testResult)) {
				// logger.info("Remove repeat Fail Test: " +
				// testResult.getName());
				iterator.remove();
			}
		}

	}

	private int getId(ITestResult result) {
		int id = result.getTestClass().getName().hashCode();
		id = id + result.getMethod().getMethodName().hashCode();
		id = id
				+ (result.getParameters() != null ? Arrays.hashCode(result
						.getParameters()) : 0);
		return id;
	}
}
