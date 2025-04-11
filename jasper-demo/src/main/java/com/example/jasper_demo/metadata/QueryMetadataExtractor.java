package com.example.jasper_demo.metadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;

public class QueryMetadataExtractor {
    public static ResultSetMetaData getQueryMetadata(String sqlQuery, Connection conn) throws Exception {
        PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        return stmt.executeQuery().getMetaData();
    }
}