package org.invoice.repository;

import org.invoice.domain.Course;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InvoiceRepositoryImpl implements InvoiceRepository {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRepositoryImpl.class);

    private Connection connection;

    public InvoiceRepositoryImpl() {
        try {
            this.connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to establish database connection.", e);
            throw new RepositoryException("Failed to establish database connection.", e);
        }
    }

    @Override
    public Invoice save(Invoice invoice) {
        String insertInvoiceSQL = "INSERT INTO invoices (student_id, course_id, discount, total_amount, invoice_date) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false); // Begin transaction

            // Ensure Student exists
            Student student = invoice.getStudent();
            if (student.getId() == null) {
                // Insert student
                String insertStudentSQL = "INSERT INTO students (name, email) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertStudentSQL, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, student.getName());
                    pstmt.setString(2, student.getEmail());
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            student.setId(rs.getInt(1));
                        } else {
                            throw new RepositoryException("Failed to insert student, no ID obtained.", null);
                        }
                    }
                }
            }

            // Ensure Course exists
            Course course = invoice.getCourse();
            if (course.getId() == null) {
                // Insert course
                String insertCourseSQL = "INSERT INTO courses (name, fee) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertCourseSQL, Statement.RETURN_GENERATED_KEYS)) {
                    pstmt.setString(1, course.getName());
                    pstmt.setDouble(2, course.getFee());
                    pstmt.executeUpdate();

                    try (ResultSet rs = pstmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            course.setId(rs.getInt(1));
                        } else {
                            throw new RepositoryException("Failed to insert course, no ID obtained.", null);
                        }
                    }
                }
            }

            // Insert Invoice
            try (PreparedStatement pstmt = connection.prepareStatement(insertInvoiceSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, student.getId());
                pstmt.setInt(2, course.getId());
                pstmt.setDouble(3, invoice.getDiscount());
                pstmt.setDouble(4, invoice.getTotalAmount());
                pstmt.setDate(5, new java.sql.Date(invoice.getInvoiceDate().getTime()));
                pstmt.executeUpdate();

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        invoice.setId(rs.getInt(1));
                    } else {
                        throw new RepositoryException("Failed to insert invoice, no ID obtained.", null);
                    }
                }
            }

            connection.commit(); // Commit transaction
            logger.info("Invoice saved successfully with ID: {}", invoice.getId());
            return invoice;

        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback transaction on error
                logger.error("Transaction rolled back due to error.", e);
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction.", rollbackEx);
            }
            throw new RepositoryException("Failed to save invoice.", e);
        } finally {
            try {
                connection.setAutoCommit(true); // Restore auto-commit
            } catch (SQLException e) {
                logger.error("Failed to restore auto-commit.", e);
                throw new RepositoryException("Failed to restore auto-commit.", e);
            }
        }
    }

    @Override
    public Invoice findById(int id) {
        return null;
    }

    @Override
    public List<Invoice> findAll() {
        return null;
    }

    @Override
    public List<Invoice> findByStudentName(String name) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    // Implement other methods similarly with logging
}
