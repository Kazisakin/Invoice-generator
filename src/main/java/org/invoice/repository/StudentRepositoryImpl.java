// File: src/main/java/org/invoice/repository/StudentRepositoryImpl.java

package org.invoice.repository;

import org.invoice.domain.Student;
import org.invoice.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the StudentRepository interface.
 */
public class StudentRepositoryImpl implements StudentRepository {

    private static final Logger logger = LoggerFactory.getLogger(StudentRepositoryImpl.class);

    private final Connection connection;

    /**
     * Constructs a StudentRepositoryImpl with a database connection.
     *
     * @throws RepositoryException If a connection cannot be established.
     */
    public StudentRepositoryImpl() {
        try {
            this.connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to establish database connection in StudentRepositoryImpl", e);
            throw new RepositoryException("Database connection failed.", e);
        }
    }

    @Override
    public Student save(Student student) {
        String insertSQL = "INSERT INTO students (name, email) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getEmail());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("Creating student failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                } else {
                    throw new RepositoryException("Creating student failed, no ID obtained.");
                }
            }
            logger.info("Student saved successfully: {}", student);
            return student;
        } catch (SQLException e) {
            logger.error("Error saving student: {}", student, e);
            throw new RepositoryException("Error saving student.", e);
        }
    }

    @Override
    public Student findById(int id) {
        String query = "SELECT id, name, email FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    logger.info("Student found: {}", student);
                    return student;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding student with ID: {}", id, e);
            throw new RepositoryException("Error finding student by ID.", e);
        }
        logger.warn("Student with ID: {} not found.", id);
        return null;
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT id, name, email FROM students";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setEmail(rs.getString("email"));
                students.add(student);
            }
            logger.info("Retrieved {} students from the database.", students.size());
            return students;
        } catch (SQLException e) {
            logger.error("Error retrieving all students.", e);
            throw new RepositoryException("Error retrieving all students.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String deleteSQL = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Student with ID: {} deleted successfully.", id);
                return true;
            } else {
                logger.warn("No student found with ID: {} to delete.", id);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error deleting student with ID: {}", id, e);
            throw new RepositoryException("Error deleting student.", e);
        }
    }

    @Override
    public Student findByEmail(String email) {
        String query = "SELECT id, name, email FROM students WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt("id"));
                    student.setName(rs.getString("name"));
                    student.setEmail(rs.getString("email"));
                    logger.info("Student found by email: {}", student);
                    return student;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding student with email: {}", email, e);
            throw new RepositoryException("Error finding student by email.", e);
        }
        logger.warn("Student with email: {} not found.", email);
        return null;
    }
}
