package com.automation.Listners;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;

public class ReportListner implements IReporter {
	String date;
	DateFormat df = new SimpleDateFormat("dd_MMM_yyyy_hh-mm-ss");
	String folderNameWithTimeStamp = df.format(new Date());
	String currentDir = System.getProperty("user.dir") + "//test-output//";
	String finalPath = currentDir + folderNameWithTimeStamp;

	@SuppressWarnings("deprecation")
	public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
			String outputDirectory) {
		TestNG.getDefault().setOutputDirectory(finalPath);
		TestNG.getDefault().setXmlSuites(xmlSuites);
	}

}