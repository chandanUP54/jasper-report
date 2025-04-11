package com.example.jasper_demo.dynamic;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import com.example.jasper_demo.metadata.QueryMetadataExtractor;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

public class DynamicReportGenerator {
    public static JasperDesign createDynamicReportDesign(String sqlQuery, Connection conn) throws Exception {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("DynamicReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(555);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);

        // Get query metadata
        ResultSetMetaData metaData = QueryMetadataExtractor.getQueryMetadata(sqlQuery, conn);

        // Add fields dynamically
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            JRDesignField field = new JRDesignField();
            field.setName(metaData.getColumnName(i));
            field.setValueClassName(getJavaClassName(metaData.getColumnTypeName(i)));
            jasperDesign.addField(field);
        }

        // Title Band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(50);
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setText("Dynamic Report");
        titleText.setX(0);
        titleText.setY(10);
        titleText.setWidth(555);
        titleText.setHeight(30);
        titleBand.addElement(titleText);
        jasperDesign.setTitle(titleBand);

        // Column Header Band
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(20);
        int xPos = 0;
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            JRDesignStaticText columnHeader = new JRDesignStaticText();
            columnHeader.setText(metaData.getColumnName(i));
            columnHeader.setX(xPos);
            columnHeader.setY(0);
            columnHeader.setWidth(100);
            columnHeader.setHeight(20);
            columnHeaderBand.addElement(columnHeader);
            xPos += 100;
        }
        jasperDesign.setColumnHeader(columnHeaderBand);

        // Detail Band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        xPos = 0;
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            JRDesignTextField textField = new JRDesignTextField();
            textField.setX(xPos);
            textField.setY(0);
            textField.setWidth(100);
            textField.setHeight(20);
            textField.setExpression(new JRDesignExpression("$F{" + metaData.getColumnName(i) + "}"));
            detailBand.addElement(textField);
            xPos += 100;
        }

        // Create a JRDesignSection for the detail section and add the band
        JRDesignSection detailSection = (JRDesignSection) jasperDesign.getDetailSection();
        detailSection.addBand(detailBand);

        return jasperDesign;
    }

    private static String getJavaClassName(String sqlType) {
        switch (sqlType.toUpperCase()) {
            case "VARCHAR":
            case "CHAR":
            case "TEXT":
                return "java.lang.String";
            case "INTEGER":
            case "INT":
                return "java.lang.Integer";
            case "DOUBLE":
            case "FLOAT":
                return "java.lang.Double";
            case "DATE":
            case "TIMESTAMP":
                return "java.util.Date";
            default:
                return "java.lang.String";
        }
    }
}