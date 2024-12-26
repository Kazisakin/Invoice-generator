// File: src/main/java/org/invoice/repository/InvoiceRepositoryImpl.java

package org.invoice.repository;

import org.invoice.domain.Course;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the InvoiceRepository interface.
 */
public class InvoiceRepositoryImpl implements InvoiceRepository {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceRepositoryImpl.class);

    private final Connection connection;

    /**
     * Constructs an InvoiceRepositoryImpl with a database connection.
     *
     * @throws RepositoryException If a connection cannot be established.
     */
    public InvoiceRepositoryImpl() {
        try {
            this.connection = ConnectionManager.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to establish database connection in InvoiceRepositoryImpl", e);
            throw new RepositoryException("Database connection failed.", e);
        }
    }

    @Override
    public Invoice save(Invoice invoice) {
        String insertSQL = "INSERT INTO invoices (student_id, course_id, discount, total_amount, invoice_date) VALUES (?, ?, ?, ?, ?)";
        try {
            connection.setAutoCommit(false); // Begin transaction

            // Ensure Student exists
            Student student = invoice.getStudent();
            if (student.getId() == null) {
                String insertStudentSQL = "INSERT INTO students (name, email) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertStudentSQL, Statement.RETURN_GENERATED_KEYS)) {
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
                }
            }

            // Ensure Course exists
            Course course = invoice.getCourse();
            if (course.getId() == null) {
                String insertCourseSQL = "INSERT INTO courses (name, fee) VALUES (?, ?)";
                try (PreparedStatement pstmt = connection.prepareStatement(insertCourseSQL, Statement.RETURN_GENERATED_KEYS)) {
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
                }
            }

            // Insert Invoice
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setInt(1, student.getId());
                pstmt.setInt(2, course.getId());
                pstmt.setDouble(3, invoice.getDiscount());
                pstmt.setDouble(4, invoice.getTotalAmount());
                pstmt.setDate(5, new java.sql.Date(invoice.getInvoiceDate().getTime()));
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new RepositoryException("Creating invoice failed, no rows affected.");
                }
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        invoice.setId(generatedKeys.getInt(1));
                    } else {
                        throw new RepositoryException("Creating invoice failed, no ID obtained.");
                    }
                }
            }

            connection.commit(); // Commit transaction
            logger.info("Invoice saved successfully: {}", invoice);
            return invoice;
        } catch (SQLException | RepositoryException e) {
            try {
                connection.rollback(); // Rollback transaction on error
                logger.error("Transaction rolled back due to error.", e);
            } catch (SQLException rollbackEx) {
                logger.error("Failed to rollback transaction.", rollbackEx);
            }
            throw new RepositoryException("Error saving invoice.", e);
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
        String query = "SELECT i.id as invoice_id, i.discount, i.total_amount, i.invoice_date, " +
                "s.id as student_id, s.name as student_name, s.email as student_email, " +
                "c.id as course_id, c.name as course_name, c.fee as course_fee " +
                "FROM invoices i " +
                "JOIN students s ON i.student_id = s.id " +
                "JOIN courses c ON i.course_id = c.id " +
                "WHERE i.id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    logger.info("Invoice found: {}", invoice);
                    return invoice;
                }
            }
        } catch (SQLException e) {
            logger.error("Error finding invoice with ID: {}", id, e);
            throw new RepositoryException("Error finding invoice by ID.", e);
        }
        logger.warn("Invoice with ID: {} not found.", id);
        return null;
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.id as invoice_id, i.discount, i.total_amount, i.invoice_date, " +
                "s.id as student_id, s.name as student_name, s.email as student_email, " +
                "c.id as course_id, c.name as course_name, c.fee as course_fee " +
                "FROM invoices i " +
                "JOIN students s ON i.student_id = s.id " +
                "JOIN courses c ON i.course_id = c.id";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Invoice invoice = mapResultSetToInvoice(rs);
                invoices.add(invoice);
            }
            logger.info("Retrieved {} invoices from the database.", invoices.size());
            return invoices;
        } catch (SQLException e) {
            logger.error("Error retrieving all invoices.", e);
            throw new RepositoryException("Error retrieving all invoices.", e);
        }
    }

    @Override
    public List<Invoice> findByStudentName(String name) {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT i.id as invoice_id, i.discount, i.total_amount, i.invoice_date, " +
                "s.id as student_id, s.name as student_name, s.email as student_email, " +
                "c.id as course_id, c.name as course_name, c.fee as course_fee " +
                "FROM invoices i " +
                "JOIN students s ON i.student_id = s.id " +
                "JOIN courses c ON i.course_id = c.id " +
                "WHERE s.name LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, "%" + name + "%"); // Partial match
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = mapResultSetToInvoice(rs);
                    invoices.add(invoice);
                }
            }
            logger.info("Found {} invoices for student name containing '{}'.", invoices.size(), name);
            return invoices;
        } catch (SQLException e) {
            logger.error("Error searching invoices by student name: {}", name, e);
            throw new RepositoryException("Error searching invoices by student name.", e);
        }
    }

    @Override
    public boolean delete(int id) {
        String deleteSQL = "DELETE FROM invoices WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Invoice with ID: {} deleted successfully.", id);
                return true;
            } else {
                logger.warn("No invoice found with ID: {} to delete.", id);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error deleting invoice with ID: {}", id, e);
            throw new RepositoryException("Error deleting invoice.", e);
        }
    }

    /**
     * Helper method to map a ResultSet row to an Invoice object.
     *
     * @param rs The ResultSet containing invoice data.
     * @return The mapped Invoice object.
     * @throws SQLException If an SQL error occurs.
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getInt("invoice_id"));
        invoice.setDiscount(rs.getDouble("discount"));
        invoice.setTotalAmount(rs.getDouble("total_amount"));
        invoice.setInvoiceDate(rs.getDate("invoice_date"));

        // Map Student
        Student student = new Student();
        student.setId(rs.getInt("student_id"));
        student.setName(rs.getString("student_name"));
        student.setEmail(rs.getString("student_email"));
        invoice.setStudent(student);

        // Map Course
        Course course = new Course();
        course.setId(rs.getInt("course_id"));
        course.setName(rs.getString("course_name"));
        course.setFee(rs.getDouble("course_fee"));
        invoice.setCourse(course);

        return invoice;
    }
}
