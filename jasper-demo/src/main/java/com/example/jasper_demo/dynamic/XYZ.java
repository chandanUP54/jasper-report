package com.example.jasper_demo.dynamic;

import java.sql.Connection;
import java.sql.ResultSetMetaData;

import com.example.jasper_demo.metadata.QueryMetadataExtractor;

public class XYZ {

	public static String generateJRXML(String sqlQuery, Connection conn) throws Exception {
	    ResultSetMetaData metaData = QueryMetadataExtractor.getQueryMetadata(sqlQuery, conn);
	    StringBuilder jrxml = new StringBuilder();
	    jrxml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	    jrxml.append("<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" " +
	                 "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	                 "xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports " +
	                 "http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\" " +
	                 "name=\"DynamicReport\" pageWidth=\"595\" pageHeight=\"842\" " +
	                 "columnWidth=\"555\" leftMargin=\"20\" rightMargin=\"20\" " +
	                 "topMargin=\"20\" bottomMargin=\"20\">\n");

	    // Fields
	    for (int i = 1; i <= metaData.getColumnCount(); i++) {
	        jrxml.append("  <field name=\"").append(metaData.getColumnName(i))
//	             .append("\" class=\"").append(getJavaClassName(metaData.getColumnTypeName(i)))
	             .append("\"/>\n");
	    }

	    // Title
	    jrxml.append("  <title><band height=\"50\">\n");
	    jrxml.append("    <staticText><reportElement x=\"0\" y=\"10\" width=\"555\" height=\"30\"/>\n");
	    jrxml.append("      <text><![CDATA[Dynamic Report]]></text></staticText>\n");
	    jrxml.append("  </band></title>\n");

	    // Column Header
	    jrxml.append("  <columnHeader><band height=\"20\">\n");
	    int xPos = 0;
	    for (int i = 1; i <= metaData.getColumnCount(); i++) {
	        jrxml.append("    <staticText><reportElement x=\"").append(xPos)
	             .append("\" y=\"0\" width=\"100\" height=\"20\"/>\n");
	        jrxml.append("      <text><![CDATA[").append(metaData.getColumnName(i)).append("]]></text></staticText>\n");
	        xPos += 100;
	    }
	    jrxml.append("  </band></columnHeader>\n");

	    // Detail
	    jrxml.append("  <detail><band height=\"20\">\n");
	    xPos = 0;
	    for (int i = 1; i <= metaData.getColumnCount(); i++) {
	        jrxml.append("    <textField><reportElement x=\"").append(xPos)
	             .append("\" y=\"0\" width=\"100\" height=\"20\"/>\n");
	        jrxml.append("      <textFieldExpression><![CDATA[$F{")
	             .append(metaData.getColumnName(i)).append("}]]></textFieldExpression></textField>\n");
	        xPos += 100;
	    }
	    jrxml.append("  </band></detail>\n");

	    jrxml.append("</jasperReport>");
	    return jrxml.toString();
	}
	
//	to report 
//	String jrxmlContent = generateJRXML(sqlQuery, conn);
//	JasperReport jasperReport = JasperCompileManager.compileReport(
//	    new ByteArrayInputStream(jrxmlContent.getBytes())
//	);
}
