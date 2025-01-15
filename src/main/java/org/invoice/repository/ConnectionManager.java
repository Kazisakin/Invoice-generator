package org.invoice.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionManager {
    private static HikariDataSource dataSource;
    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/invoice_db");
        config.setUsername("root");
        config.setPassword("123456");
        config.setMaximumPoolSize(5);
        dataSource = new HikariDataSource(config);
    }
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
