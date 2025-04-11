package com.example.jasper_demo.services;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jasper_demo.dto.ReportFilterDTO;
import com.example.jasper_demo.entity.ReportData;
import com.example.jasper_demo.repository.ReportDataRepository;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	@Autowired
	private ReportDataRepository repository;

	public byte[] generateReport(ReportFilterDTO filters) throws JRException {
		logger.info("Generating report with filters: {}", filters);

		// Fetch data with filtering
		List<ReportData> data = filters.getCategory() != null && !filters.getCategory().isEmpty()
				? repository.findByCategory(filters.getCategory())
				: repository.findAll();

		if (data.isEmpty()) {
			throw new IllegalStateException("No data found for the given filters");
		}

		// Load JRXML template
		InputStream reportStream = getClass().getResourceAsStream("/reports/report.jrxml");
		JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

		// Prepare data source
		JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
		Map<String, Object> parameters = new HashMap<>();

		// Fill report
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

		// Export based on format
		switch (filters.getFormat().toLowerCase()) {
		case "pdf":
			return JasperExportManager.exportReportToPdf(jasperPrint);
		case "excel":
			JRXlsxExporter xlsxExporter = new JRXlsxExporter();
			ByteArrayOutputStream xlsReport = new ByteArrayOutputStream();
			xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
			xlsxExporter.exportReport();
			return xlsReport.toByteArray();
		case "csv":
			JRCsvExporter csvExporter = new JRCsvExporter();
			ByteArrayOutputStream csvReport = new ByteArrayOutputStream();
			csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			csvExporter.setExporterOutput(new SimpleWriterExporterOutput(csvReport));
			csvExporter.exportReport();
			return csvReport.toByteArray();
		default:
			throw new IllegalArgumentException("Unsupported format: " + filters.getFormat());
		}
	}
}