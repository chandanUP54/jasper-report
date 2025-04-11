package com.example.jasper_demo.dynamic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jasper_demo.db.UserDB;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;

@Service
public class DynamicReportService {

	public void generateReport(String sqlQuery, String outputPath) throws Exception {
		// Database connection
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "admin");

		try {
			// Create JasperDesign
			JasperDesign jasperDesign = DynamicReportGenerator.createDynamicReportDesign(sqlQuery, conn);

			// Compile the report
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			// Execute the query
			PreparedStatement stmt = conn.prepareStatement(sqlQuery);
			ResultSet rs = stmt.executeQuery();

			// Fill the report
			JRResultSetDataSource dataSource = new JRResultSetDataSource(rs);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

			// Export to PDF (or other formats)
			JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

}
