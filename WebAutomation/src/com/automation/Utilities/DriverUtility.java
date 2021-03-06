package com.automation.Utilities;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class DriverUtility {

	public static WebDriver driver;
	ConfigPropertyReader properties = new ConfigPropertyReader();
	public static final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";

	@BeforeSuite
	public void beforeSuite() {
		System.setProperty(ESCAPE_PROPERTY, "false");
		setUpBrowser();
	}

	@AfterSuite
	public void afterSuite() throws Exception {
		driver.quit();

	}

	public WebDriver getDriver() {
		return driver;
	}

	private void setUpBrowser() {
		chooseBrowser(properties.getDefaultBrowser());
		if (System.getProperty("env.baseurl") != null) {
			driver.get(System.getProperty("env.baseurl"));
		} else {
			driver.get(properties.getBaseUrl());
		}
		driver.manage().window().maximize();
	}

	public void navigateToUrl(String url) {
		if (System.getProperty("env.baseurl") != null) {
			driver.get(System.getProperty("env.baseurl") + url);
		} else {
			driver.get(properties.getBaseUrl() + url);
		}
	}

	/**
	 * This method used to create driver for specified browser
	 * 
	 * @param browser
	 */
	private void chooseBrowser(String browser) {
		System.out.println(browser);
		switch (browser) {
		case "firefoxDriver":

			driver = new FirefoxDriver();
			break;

		case "chromeDriver":

			System.setProperty("webdriver.chrome.driver",
					properties.getDriverFile());
			ChromeOptions options = new ChromeOptions();
			driver = new ChromeDriver(options);
			break;
		}
	}

	/**
	 * This method used to wait till particular element present
	 * 
	 * @param locStrategy
	 * @param locator
	 */
	public void waitForElementPresent(String locStrategy, String locator) {
		WebDriverWait wait = new WebDriverWait(getDriver(),
				Long.parseLong(properties.getDefaultTimeOut()));
		try {
			if (locStrategy.equalsIgnoreCase("xpath")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.xpath(locator)));
			} else if (locStrategy.equalsIgnoreCase("CSS")) {
				wait.until(ExpectedConditions.presenceOfElementLocated(By
						.cssSelector(locator)));
			} else {
				System.out.println("Invalid locator strategy");
			}
		} catch (Exception e) {

		}

	}

	/**
	 * Selenium method to get element by Text
	 * 
	 * @param text
	 * @return
	 */
	public WebElement getElementByText(String text) {
		String xpath = CommonUtility.convertXpathFromText(text);
		waitForElementPresent("xpath", xpath);
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * Selenium method to get element by Xpath
	 * 
	 * @param text
	 * @return
	 */

	public WebElement getElementByXpath(String xpath) {
		waitForElementPresent("xpath", xpath);
		return driver.findElement(By.xpath(xpath));
	}

	/**
	 * Selenium method to get element by CSS
	 * 
	 * @param text
	 * @return
	 */

	public WebElement getElementByCSS(String CSS) {
		waitForElementPresent("CSS", CSS);
		return driver.findElement(By.cssSelector(CSS));
	}

	/**
	 * Selenium method to get element list by CSS
	 * 
	 * @param text
	 * @return
	 */

	public List<WebElement> getElementListByCSS(String CSS) {
		waitForElementPresent("CSS", CSS);
		return driver.findElements(By.cssSelector(CSS));
	}

	/**
	 * Open Link in new window
	 * 
	 * @param element
	 * @return
	 */

	public String openLinkInNewWindow(WebElement element) {
		Actions actionOpenLinkInNewTab = new Actions(driver);
		actionOpenLinkInNewTab.moveToElement(element).keyDown(Keys.COMMAND)
				.keyDown(Keys.SHIFT).click(element).keyUp(Keys.COMMAND)
				.keyUp(Keys.SHIFT).perform();
		ArrayList<String> tabs = new ArrayList<String>(
				driver.getWindowHandles());
		driver.switchTo().window(tabs.get(1));
		return tabs.get(0);
	}

	/**
	 * This is used to check element is present or not
	 * 
	 * @param locator
	 * @return
	 */
	public boolean isPresent(String locator) {
		try {
			driver.findElement(By.cssSelector(locator));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * This is used to switch to new window
	 * 
	 * @return
	 * @throws Exception
	 */

	public String switchToNewWindow() throws Exception {
		String winHandleBefore = getDriver().getWindowHandle();
		Thread.sleep(5000);
		for (String winHandle : getDriver().getWindowHandles()) {
			getDriver().switchTo().window(winHandle);
		}
		return winHandleBefore;
	}
}
