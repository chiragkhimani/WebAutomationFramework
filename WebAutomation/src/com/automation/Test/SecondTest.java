package com.automation.Test;

import org.testng.annotations.Test;

import com.automation.Locators.SecondTestLocators;
import com.automation.Utilities.DriverUtility;

public class SecondTest extends DriverUtility implements SecondTestLocators {

	@Test
	public void secondTest() {
		System.out.println("Second Test");
	}
}
