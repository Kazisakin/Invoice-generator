// File: src/main/java/org/invoice/repository/UserRepositoryImpl.java

package org.invoice.repository;

import org.invoice.domain.User;
import org.invoice.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the UserRepository interface.
 */
public class UserRepositoryImpl implements UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);

    private final Connection connection;

    /**
     * Constructs a UserRepositoryImpl with a database connection.
     *
     * @throws RepositoryException If a connection cannot be established.
     */
    public UserRepositoryImpl() {
        try {
            this.connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to establish database connection in UserRepositoryImpl", e);
            throw new RepositoryException("Database connection failed.", e);
        }
    }

    @Override
    public User save(User user) {
        String insertSQL = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // In real apps, hash passwords!
            pstmt.setString(3, user.getRole());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new RepositoryException("Creating user failed, no ID obtained.");
                }
            }
            logger.info("User saved successfully: {}", user);
            return user;
        } catch (SQLException e) {
            logger.error("Error saving user: {}", user, e);
            throw new RepositoryException("Error saving user.", e);
        }
    }

    @Override
    public User findById(int id) {
        String query = "SELECT id, username, password, role FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    logger.info("User found: {}", user);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user with ID: {}", id, e);
            throw new RepositoryException("Error finding user by ID.", e);
        }
        logger.warn("User with ID: {} not found.", id);
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, password, role FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
            logger.info("Retrieved {} users from the database.", users.size());
            return users;
        } catch (SQLException e) {
            logger.error("Error retrieving all users.", e);
            throw new RepositoryException("Error retrieving all users.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String deleteSQL = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("User with ID: {} deleted successfully.", id);
                return true;
            } else {
                logger.warn("No user found with ID: {} to delete.", id);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error deleting user with ID: {}", id, e);
            throw new RepositoryException("Error deleting user.", e);
        }
    }

    @Override
    public User findByUsername(String username) {
        String query = "SELECT id, username, password, role FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    logger.info("User found by username: {}", user);
                    return user;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding user with username: {}", username, e);
            throw new RepositoryException("Error finding user by username.", e);
        }
        logger.warn("User with username: {} not found.", username);
        return null;
    }
}
