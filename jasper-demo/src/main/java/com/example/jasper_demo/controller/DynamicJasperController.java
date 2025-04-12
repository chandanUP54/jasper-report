package com.example.jasper_demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jasper_demo.dto.QueryDTO;
import com.example.jasper_demo.dynamic.DynamicReportService;

@RestController
public class DynamicJasperController {

	@Autowired
	private DynamicReportService dynamicTest;
	
//	@PostMapping("/report")
//	public void dynamic(@RequestBody QueryDTO queryDTO) throws Exception {
//        String outputPath = "reports/DynamicReport.pdf"; //--> give response or byte stream
//		dynamicTest.generateReport(queryDTO.getQuery(),outputPath);
//	}
	
	   @PostMapping("/report")
	    public ResponseEntity<byte[]> dynamic(@RequestBody QueryDTO queryDTO) throws Exception {
	    	
	        byte[] pdfContent = dynamicTest.generateReport(queryDTO.getQuery());

	        // Set HTTP headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        headers.setContentDispositionFormData("attachment", "DynamicReport.pdf");

	        // Return the PDF as a response
	        return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
	    }
}
