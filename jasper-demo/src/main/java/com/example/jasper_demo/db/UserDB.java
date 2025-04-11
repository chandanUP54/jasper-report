package com.example.jasper_demo.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;

public class UserDB {

	@Value("${spring.datasource.url}")
	private String URL;

	@Value("${spring.datasource.username}")
	private String USERNAME;

	@Value("${spring.datasource.password}")
	private String PASSWORD;

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}
}
