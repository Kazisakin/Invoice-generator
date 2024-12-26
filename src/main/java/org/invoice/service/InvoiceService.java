// File: src/main/java/org/invoice/service/InvoiceService.java

package org.invoice.service;

import org.invoice.domain.Course;
import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.ServiceException;
import org.invoice.repository.InvoiceRepository;

import java.util.Date;
import java.util.List;

/**
 * Service layer for managing invoices.
 */
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    /**
     * Constructs an InvoiceService with the specified InvoiceRepository.
     *
     * @param invoiceRepository The repository responsible for invoice data operations.
     */
    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    /**
     * Creates a new invoice by ensuring that the associated student and course exist.
     *
     * @param student  The student for whom the invoice is created.
     * @param course   The course associated with the invoice.
     * @param discount The discount applied to the course fee.
     * @return The created Invoice object with a generated ID.
     * @throws ServiceException If an error occurs during invoice creation.
     */
    public Invoice createInvoice(Student student, Course course, double discount) throws ServiceException {
        try {
            // Business logic: Discount should not exceed course fee
            if (discount > course.getFee()) {
                throw new ServiceException("Discount cannot exceed the course fee.");
            }

            // Additional business logic can be added here

            Invoice invoice = new Invoice();
            invoice.setStudent(student);
            invoice.setCourse(course);
            invoice.setDiscount(discount);
            invoice.setTotalAmount(course.getFee() - discount);
            invoice.setInvoiceDate(new Date());

            return invoiceRepository.save(invoice);
        } catch (Exception e) {
            throw new ServiceException("Failed to create invoice.", e);
        }
    }

    /**
     * Retrieves an invoice by its unique ID.
     *
     * @param id The ID of the invoice.
     * @return The Invoice object if found; null otherwise.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public Invoice findInvoiceById(int id) throws ServiceException {
        try {
            return invoiceRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve invoice with ID: " + id, e);
        }
    }

    /**
     * Retrieves all invoices.
     *
     * @return A list of all Invoice objects.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public List<Invoice> listAllInvoices() throws ServiceException {
        try {
            return invoiceRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve all invoices.", e);
        }
    }

    /**
     * Searches for invoices by a student's name.
     *
     * @param studentName The name of the student to search for.
     * @return A list of Invoice objects associated with the specified student.
     * @throws ServiceException If an error occurs during the search.
     */
    public List<Invoice> searchInvoicesByStudentName(String studentName) throws ServiceException {
        try {
            return invoiceRepository.findByStudentName(studentName);
        } catch (Exception e) {
            throw new ServiceException("Failed to search invoices by student name: " + studentName, e);
        }
    }

    /**
     * Deletes an invoice by its unique ID.
     *
     * @param id The ID of the invoice to delete.
     * @return True if deletion was successful; false otherwise.
     * @throws ServiceException If an error occurs during deletion.
     */
    public boolean deleteInvoice(int id) throws ServiceException {
        try {
            return invoiceRepository.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete invoice with ID: " + id, e);
        }
    }
}
