package com.example.jasper_demo.dynamic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.jasper_demo.db.DatabaseConnect;

import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;



@Service
public class DynamicReportService {
	
	@Autowired
	private DatabaseConnect dbconnect;

	public  byte[] generateReport(String sqlQuery) throws Exception {

        Connection conn = dbconnect.getConnection();

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

            // Export to PDF as byte array
            return JasperExportManager.exportReportToPdf(jasperPrint);

        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

}
