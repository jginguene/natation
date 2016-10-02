package fr.natation;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection createConnection() throws Exception {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:test.db");

        } catch (Exception e) {
            throw new Exception("createConnection() failed", e);
        }
    }
}
