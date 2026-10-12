package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // Make sure the database name matches your SQL tool
    private static final String URL = "jdbc:mysql://localhost:3306/inventorysystem";
    private static final String USER = "root";
    private static final String PASSWORD = "Zandi@2004";

    public static Connection getConnection() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD
            );

            System.out.println("Connected Successfully!");

        } catch (SQLException e) {
            System.out.println("Connection Failed!");
            e.printStackTrace();
        }

        return connection;
    }

    public static void main(String[] args) {
        getConnection();
    }
}