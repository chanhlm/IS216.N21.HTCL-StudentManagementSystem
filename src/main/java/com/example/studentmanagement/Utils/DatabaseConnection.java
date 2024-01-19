package com.example.studentmanagement.Utils;

import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    // init database constants
    private static final String DATABASE_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static final String DATABASE_URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String DATABASE_USERNAME = "c##student_management_system";
    private static final String DATABASE_PASSWORD = "userpass";
    private static final String DATABASE_MAX_POOL = "250";

//    private static final String DATABASE_DRIVER = ENVUtils.get("DATABASE_DRIVER");
//    private static final String DATABASE_URL = ENVUtils.get("DATABASE_URL");
//    private static final String DATABASE_USERNAME = ENVUtils.get("DATABASE_USERNAME");
//    private static final String DATABASE_PASSWORD = ENVUtils.get("DATABASE_PASSWORD");
//    private static final String DATABASE_MAX_POOL = ENVUtils.get("DATABASE_MAX_POOL");

    public DatabaseConnection() {
        connect();
    }

    // init connection object
    private static Connection connection;
    // init properties object
    private static Properties properties;

    // create properties
    private static Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            properties.setProperty("user", DATABASE_USERNAME);
            properties.setProperty("password", DATABASE_PASSWORD);
            properties.setProperty("MaxPooledStatements", DATABASE_MAX_POOL);
        }
        return properties;
    }

    // connect database
    public static Connection connect() {
        if (connection == null) {
            try {
                Class.forName(DATABASE_DRIVER);
                connection = DriverManager.getConnection(DATABASE_URL, getProperties());
                System.out.println("Connected to database successfully!");
            } catch (Exception e) {
                System.out.println("ERROR connecting to database!");
                System.out.println(e.toString());
            }
        }
        return connection;
    }

    public static ResultSet query(String query) throws  SQLException{
        Statement statement = connection.createStatement();
        System.out.println("sql query: " + query);
        return statement.executeQuery(query);
    }

    public static void mutation(String query) throws SQLException {
        System.out.println("sql mutation: " + query);
        connection.createStatement().executeUpdate(query);
    }

    public static Integer mutationReturnGeneratedKeys(String query, String idFieldName) throws SQLException {
        System.out.println("sql mutation: " + query);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query, new String[]{idFieldName});
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Creating user failed, no ID obtained.");
        }
    }

    // disconnect database
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
