package com.automation.Test;

import org.testng.annotations.Test;

import com.automation.Locators.FirstTestLocators;
import com.automation.Utilities.DriverUtility;

public class FirstTest extends DriverUtility implements FirstTestLocators {
	HomePage homePage = new HomePage();
	LoginPage loginPage = new LoginPage();
	@Test
	public void firstTest() {
		
		//@given user is on homepage
		homePage.openWebsite();
		
		//@when I login into app with username "" and password ""
		loginPage.doLogin(username, password);
		
		//@Then veirfy login successful
		homePage.verifyLoginSuccesful();
		
	}
}
