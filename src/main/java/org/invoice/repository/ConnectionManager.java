// File: src/main/java/org/invoice/repository/ConnectionManager.java

package org.invoice.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Manages database connections using HikariCP connection pooling.
 */
public class ConnectionManager {

    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/teaching_institute"); // Update as per your DB
        config.setUsername("root"); // Replace with your DB username
        config.setPassword("123456"); // Replace with your DB password
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000); // 30 seconds
        config.setConnectionTimeout(30000); // 30 seconds
        config.setMaxLifetime(1800000); // 30 minutes

        // Optional: Test query to ensure connections are valid
        config.setConnectionTestQuery("SELECT 1");

        dataSource = new HikariDataSource(config);
    }

    // Private constructor to prevent instantiation
    private ConnectionManager() {}

    /**
     * Retrieves a connection from the pool.
     *
     * @return A valid database connection.
     * @throws SQLException If unable to retrieve a connection.
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
