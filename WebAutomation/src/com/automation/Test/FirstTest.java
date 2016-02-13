package com.automation.Test;

import org.testng.annotations.Test;

import com.automation.Locators.FirstTestLocators;
import com.automation.Utilities.DriverUtility;

public class FirstTest extends DriverUtility implements FirstTestLocators {
	@Test
	public void firstTest() {
		System.out.println("First Test");
	}
}
