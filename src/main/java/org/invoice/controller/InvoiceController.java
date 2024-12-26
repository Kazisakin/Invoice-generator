// File: src/main/java/org/invoice/controller/InvoiceController.java

package org.invoice.controller;

import org.invoice.domain.Course;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.ServiceException;
import org.invoice.service.InvoiceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Controller class responsible for handling invoice-related operations.
 * It acts as an intermediary between the UI layer and the service layer.
 */
public class InvoiceController {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceController.class);

    private final InvoiceService invoiceService;

    /**
     * Constructs an InvoiceController with the specified InvoiceService.
     *
     * @param invoiceService The service layer responsible for invoice operations.
     */
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    /**
     * Creates a new invoice with the given student, course, and discount.
     *
     * @param student  The student associated with the invoice.
     * @param course   The course associated with the invoice.
     * @param discount The discount applied to the course fee.
     * @return The created Invoice object with a generated ID.
     * @throws ServiceException If an error occurs during invoice creation.
     */
    public Invoice createInvoice(Student student, Course course, double discount) throws ServiceException {
        try {
            Invoice invoice = invoiceService.createInvoice(student, course, discount);
            logger.info("Invoice created successfully with ID: {}", invoice.getId());
            return invoice;
        } catch (ServiceException e) {
            logger.error("Failed to create invoice: {}", e.getMessage());
            throw e; // Propagate the exception to the caller (e.g., UI layer)
        }
    }

    /**
     * Retrieves an invoice by its unique identifier.
     *
     * @param id The unique ID of the invoice.
     * @return The Invoice object if found.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public Invoice findInvoiceById(int id) throws ServiceException {
        try {
            Invoice invoice = invoiceService.findInvoiceById(id);
            if (invoice != null) {
                logger.info("Invoice retrieved successfully with ID: {}", id);
            } else {
                logger.warn("Invoice with ID: {} not found.", id);
            }
            return invoice;
        } catch (ServiceException e) {
            logger.error("Failed to retrieve invoice with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    /**
     * Retrieves a list of all invoices.
     *
     * @return A list containing all Invoice objects.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public List<Invoice> listAllInvoices() throws ServiceException {
        try {
            List<Invoice> invoices = invoiceService.listAllInvoices();
            logger.info("Retrieved {} invoices successfully.", invoices.size());
            return invoices;
        } catch (ServiceException e) {
            logger.error("Failed to retrieve all invoices: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Searches for invoices associated with a student's name.
     *
     * @param studentName The name of the student to search for.
     * @return A list of Invoice objects associated with the specified student.
     * @throws ServiceException If an error occurs during the search.
     */
    public List<Invoice> searchInvoicesByStudentName(String studentName) throws ServiceException {
        try {
            List<Invoice> invoices = invoiceService.searchInvoicesByStudentName(studentName);
            logger.info("Found {} invoices for student name containing '{}'.", invoices.size(), studentName);
            return invoices;
        } catch (ServiceException e) {
            logger.error("Failed to search invoices by student name '{}': {}", studentName, e.getMessage());
            throw e;
        }
    }

    /**
     * Deletes an invoice by its unique identifier.
     *
     * @param id The unique ID of the invoice to delete.
     * @return True if the invoice was deleted successfully; false otherwise.
     * @throws ServiceException If an error occurs during deletion.
     */
    public boolean deleteInvoice(int id) throws ServiceException {
        try {
            boolean isDeleted = invoiceService.deleteInvoice(id);
            if (isDeleted) {
                logger.info("Invoice with ID: {} deleted successfully.", id);
            } else {
                logger.warn("Invoice with ID: {} could not be deleted or does not exist.", id);
            }
            return isDeleted;
        } catch (ServiceException e) {
            logger.error("Failed to delete invoice with ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}
