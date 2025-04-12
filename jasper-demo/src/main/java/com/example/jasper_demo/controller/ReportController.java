package com.example.jasper_demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.jasper_demo.dto.ReportFilterDTO;
import com.example.jasper_demo.services.ReportService;

//in this dynamic report we dont need to create jrxml seperate we create it by java => DynamicReportGenerator

@RestController
@RequestMapping("/api/report")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @PostMapping("/download")
    public ResponseEntity<byte[]> downloadReport( @RequestBody ReportFilterDTO filters) {
        try {
            byte[] reportBytes = reportService.generateReport(filters);

            String contentType = switch (filters.getFormat().toLowerCase()) {
                case "pdf" -> MediaType.APPLICATION_PDF_VALUE;
                case "excel" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                case "csv" -> MediaType.TEXT_PLAIN_VALUE;
                default -> MediaType.APPLICATION_OCTET_STREAM_VALUE;
            };

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report." + filters.getFormat())
                .contentType(MediaType.parseMediaType(contentType))
                .body(reportBytes);
        } catch (Exception e) {
            logger.error("Error generating report: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }
}