// File: src/main/java/org/invoice/repository/CourseRepositoryImpl.java

package org.invoice.repository;

import org.invoice.domain.Course;
import org.invoice.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the CourseRepository interface.
 */
public class CourseRepositoryImpl implements CourseRepository {

    private static final Logger logger = LoggerFactory.getLogger(CourseRepositoryImpl.class);

    private final Connection connection;

    /**
     * Constructs a CourseRepositoryImpl with a database connection.
     *
     * @throws RepositoryException If a connection cannot be established.
     */
    public CourseRepositoryImpl() {
        try {
            this.connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to establish database connection in CourseRepositoryImpl", e);
            throw new RepositoryException("Database connection failed.", e);
        }
    }

    @Override
    public Course save(Course course) {
        String insertSQL = "INSERT INTO courses (name, fee) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, course.getName());
            pstmt.setDouble(2, course.getFee());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RepositoryException("Creating course failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                } else {
                    throw new RepositoryException("Creating course failed, no ID obtained.");
                }
            }
            logger.info("Course saved successfully: {}", course);
            return course;
        } catch (SQLException e) {
            logger.error("Error saving course: {}", course, e);
            throw new RepositoryException("Error saving course.", e);
        }
    }

    @Override
    public Course findById(int id) {
        String query = "SELECT id, name, fee FROM courses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Course course = new Course();
                    course.setId(rs.getInt("id"));
                    course.setName(rs.getString("name"));
                    course.setFee(rs.getDouble("fee"));
                    logger.info("Course found: {}", course);
                    return course;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding course with ID: {}", id, e);
            throw new RepositoryException("Error finding course by ID.", e);
        }
        logger.warn("Course with ID: {} not found.", id);
        return null;
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT id, name, fee FROM courses";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setName(rs.getString("name"));
                course.setFee(rs.getDouble("fee"));
                courses.add(course);
            }
            logger.info("Retrieved {} courses from the database.", courses.size());
            return courses;
        } catch (SQLException e) {
            logger.error("Error retrieving all courses.", e);
            throw new RepositoryException("Error retrieving all courses.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String deleteSQL = "DELETE FROM courses WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Course with ID: {} deleted successfully.", id);
                return true;
            } else {
                logger.warn("No course found with ID: {} to delete.", id);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error deleting course with ID: {}", id, e);
            throw new RepositoryException("Error deleting course.", e);
        }
    }
}
