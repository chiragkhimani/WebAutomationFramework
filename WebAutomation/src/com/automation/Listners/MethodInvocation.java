package com.automation.Listners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.automation.Utilities.DataPropertyReader;

public class MethodInvocation implements ITestListener {

	@Override
	public void onFinish(ITestContext arg0) {

	}

	@Override
	public void onStart(ITestContext arg0) {
		DataPropertyReader.setIsTestFailed(false);
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestFailure(ITestResult arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTestSkipped(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTestStart(ITestResult arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * After test success check if test is failed using framework verification
	 * then mark test as a failed and print same into the report
	 */
	@Override
	public void onTestSuccess(ITestResult arg0) {
		if (DataPropertyReader.getIsTestFailed()) {
			arg0.setStatus(ITestResult.FAILURE);
		}
	}

}