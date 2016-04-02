package com.automation.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class is used to read properties from data.properties
 * 
 * @author chirag.khimani
 * 
 */
public class DataPropertyReader {
	private String assertElementVisiblePassMsg;
	private String assertElementVisibleFailMsg;
	private String reporterAssertPassMsg;
	private String reporterAssertFailMsg;
	private static boolean isTestFailed;
	private String assertTextPass;
	private String assertTextFail;
	Properties properties;
	

	public DataPropertyReader() {
		System.out.println(System.getProperty("user.dir")+"\\resources\\data.properties");
		 properties = loadProperties(System.getProperty("user.dir")+"\\resources\\data.properties");
	}
	
	
	public String getProperty(String key) {
		String value = properties.getProperty(key);
		return value;
	}


	public Properties loadProperties(String propPath) {
		Properties properties = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(propPath);
			System.out.println("---->"+input);
			properties.load(input);
			assertElementVisiblePassMsg = properties
					.getProperty("element.visible.msg.pass");
			assertElementVisibleFailMsg = properties
					.getProperty("element.visible.msg.fail");
			reporterAssertPassMsg = properties
					.getProperty("reporter.assert.pass.msg");
			reporterAssertFailMsg = properties
					.getProperty("reporter.assert.fail.msg");
			isTestFailed = Boolean.parseBoolean(properties
					.getProperty("is.test.failed"));
			assertTextPass = properties.getProperty("element.assert.text.pass");
			assertTextFail = properties.getProperty("element.assert.text.fail");

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return properties;
	}

	public String getAssertTextFail() {
		return assertTextFail;
	}

	public String getAssertTextPass() {
		return assertTextPass;
	}

	public static boolean getIsTestFailed() {
		return isTestFailed;
	}

	public static void setIsTestFailed(boolean isTestFailed) {
		DataPropertyReader.isTestFailed = isTestFailed;
	}

	public String getAssertElementVisiblePassMsg() {
		return assertElementVisiblePassMsg;
	}

	public String getAssertElementVisibleFailMsg() {
		return assertElementVisibleFailMsg;
	}

	public String getReporterAssertPassMsg() {
		return reporterAssertPassMsg;
	}

	public String getReporterAssertFailMsg() {
		return reporterAssertFailMsg;
	}

}
