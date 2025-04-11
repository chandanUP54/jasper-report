package com.example.jasper_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jasper_demo.dynamic.DynamicReportService;

@RestController
public class DynamicJasperController {

	@Autowired
	private DynamicReportService dynamicTest;
	
	@PostMapping("/report")
	public void dynamic() throws Exception {
		  String sqlQuery = "SELECT name, id, category FROM  report_data";
          String outputPath = "reports/DynamicReport.pdf";
		dynamicTest.generateReport(sqlQuery,outputPath);
	}
}
