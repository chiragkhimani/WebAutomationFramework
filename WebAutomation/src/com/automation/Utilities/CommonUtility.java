package com.automation.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class CommonUtility extends DriverUtility {
	DataPropertyReader dataProperties = new DataPropertyReader();

	/**
	 * Get screenshot of current screen and store into /img folder
	 * 
	 * @return Screenshot Image Path
	 */
	public String getScreenShot() {
		DateFormat df = new SimpleDateFormat("DD_MMM_yyyy_hh-mm-ss");
		String screenshotName = df.format(new Date());
		String screenshotPath = null;
		File scrFile = ((TakesScreenshot) getDriver())
				.getScreenshotAs(OutputType.FILE);
		try {
			screenshotPath = System.getProperty("user.dir") + "\\img\\"
					+ screenshotName + ".png";
			FileUtils.copyFile(scrFile, new File(screenshotPath));

		} catch (IOException e) {
		}

		return screenshotPath;
	}

	/**
	 * Email report after completion of test execution
	 * 
	 * @throws Exception
	 */
	public void sendMail() {
		zipFolder(System.getProperty("user.dir") + "\\test-output",
				System.getProperty("user.dir") + "\\img\\report.zip");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.mail.yahoo.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(dataProperties
								.getProperty("email.username"), dataProperties
								.getProperty("email.password"));
					}
				});

		try {

			String filename = System.getProperty("user.dir")
					+ "\\img\\chirag.zip";
			DataSource source = new FileDataSource(filename);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(filename);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart2);

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(dataProperties
					.getProperty("from.email")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress
					.parse(dataProperties.getProperty("to.email")));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please!");
			message.setContent(multipart);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Convert xPath from given text
	 * 
	 * @param text
	 * @return xPath
	 */

	public static String convertXpathFromText(String text) {
		String xpath = "//*[text()=\"%s\"]";
		xpath = String.format(xpath, text);
		return xpath;
	}

	/**
	 * Highlight specific element in screenshot for better understanding where
	 * exactly it's failed
	 * 
	 * @param webdriver
	 * @param element
	 * @throws InterruptedException
	 */
	public void highLightElement(WebDriver webdriver, WebElement element)
			throws InterruptedException {
		JavascriptExecutor driver = (JavascriptExecutor) webdriver;
		driver.executeScript(
				"arguments[0].setAttribute('style',arguments[1]);", element,
				"border: 2px solid red;");
	}

	/**
	 * Wait for specified millisecond
	 * 
	 * @param millis
	 */
	public static void wait(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Verify specified condition is true or false like element.isDisplayed(),
	 * isEnable() etc and print result into the report
	 * 
	 * @param condition
	 * @param passMessage
	 * @param failedMessage
	 */
	public void verifyTrue(boolean condition, String passMessage,
			String failedMessage) {
		if (condition) {
			Reporter.log(String.format(
					dataProperties.getReporterAssertPassMsg(), passMessage));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			Reporter.log(String.format(
					dataProperties.getReporterAssertFailMsg(), failedMessage,
					getScreenShot()));
		}
	}

	/**
	 * Verify element is visible or not and print result into the report
	 * 
	 * @param element
	 * @param label
	 */

	public void verifyVisible(WebElement element, String label) {
		if (element.isDisplayed()) {
			Reporter.log(String.format(
					dataProperties.getAssertElementVisiblePassMsg(), label,
					label));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			Reporter.log(String.format(
					dataProperties.getAssertElementVisibleFailMsg(), label,
					label, getScreenShot()));
		}
	}

	/**
	 * Get data from excel file
	 * 
	 * @param fileName
	 * @return ExcelData in Array
	 */
	public String[][] getExcelData(String fileName) {
		String[][] arrayExcelData = null;
		File file = new File(fileName);

		FileInputStream inputStream;
		try {
			inputStream = new FileInputStream(file);
			XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
			XSSFSheet sh = workBook.getSheetAt(0);

			int totalNoOfCols = sh.getRow(1).getLastCellNum();
			int totalNoOfRows = sh.getLastRowNum();
			arrayExcelData = new String[totalNoOfRows][totalNoOfCols];
			for (int i = 1; i <= totalNoOfRows; i++) {

				for (int j = 0; j < totalNoOfCols; j++) {
					if (sh.getRow(i).getCell(j) != null) {
						arrayExcelData[i - 1][j] = sh.getRow(i).getCell(j)
								.toString();
					} else {
						arrayExcelData[i - 1][j] = null;
					}
				}
			}
			workBook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return arrayExcelData;
	}

	/**
	 * Verify specified element contains(exact match) specified text or not and
	 * print result into the report
	 * 
	 * @param element
	 * @param text
	 * @param label
	 */
	public void verifyText(WebElement element, String text, String label) {
		System.out.println(element.getText());
		System.out.println(text);
		if (element.getText().equalsIgnoreCase(text)) {
			Reporter.log(String.format(dataProperties.getAssertTextPass(),
					label, text, label, element.getText()));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			Reporter.log(String.format(dataProperties.getAssertTextFail(),
					label, text, label, element.getText(), getScreenShot()));
		}
	}

	/**
	 * Zip specified folder
	 * 
	 * @param srcFolder
	 * @param destZipFile
	 */
	static public void zipFolder(String srcFolder, String destZipFile) {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;

		try {
			fileWriter = new FileOutputStream(destZipFile);
			addFolderToZip("", srcFolder, zip);
			zip = new ZipOutputStream(fileWriter);
			zip.flush();
			zip.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add any file into zip
	 * 
	 * @param path
	 * @param srcFile
	 * @param zip
	 * @throws Exception
	 */
	static private void addFileToZip(String path, String srcFile,
			ZipOutputStream zip) throws Exception {

		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in = new FileInputStream(srcFile);
			zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
			while ((len = in.read(buf)) > 0) {
				zip.write(buf, 0, len);
			}
			in.close();
		}

	}

	/**
	 * Add any folder to zip
	 * 
	 * @param path
	 * @param srcFolder
	 * @param zip
	 * @throws Exception
	 */
	static private void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) throws Exception {
		File folder = new File(srcFolder);

		for (String fileName : folder.list()) {
			if (path.equals("")) {
				addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
			} else {
				addFileToZip(path + "/" + folder.getName(), srcFolder + "/"
						+ fileName, zip);
			}
		}
	}

	/**
	 * Verify specified element contains specified text or not and print result
	 * into the report
	 * 
	 * @param element
	 * @param text
	 * @param label
	 */
	public void verifyTextContains(WebElement element, String text, String label) {
		if (element.getText().contains(text)) {
			Reporter.log(String.format(dataProperties.getAssertTextPass(),
					label, text, label, element.getText()));
		} else {
			DataPropertyReader.setIsTestFailed(true);
			Reporter.log(String.format(dataProperties.getAssertTextFail(),
					label, text, label, element.getText(), getScreenShot()));
		}
	}

}
