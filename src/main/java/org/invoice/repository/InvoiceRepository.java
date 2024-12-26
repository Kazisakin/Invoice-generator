// File: src/main/java/org/invoice/repository/InvoiceRepository.java

package org.invoice.repository;

import org.invoice.domain.Invoice;

import java.util.List;

/**
 * Repository interface for managing Invoice entities.
 */
public interface InvoiceRepository {
    /**
     * Saves a new invoice to the database.
     *
     * @param invoice The invoice to save.
     * @return The saved invoice with an assigned ID.
     */
    Invoice save(Invoice invoice);

    /**
     * Finds an invoice by its unique ID.
     *
     * @param id The ID of the invoice.
     * @return The Invoice object if found; null otherwise.
     */
    Invoice findById(int id);

    /**
     * Retrieves all invoices from the database.
     *
     * @return A list of all invoices.
     */
    List<Invoice> findAll();

    /**
     * Finds invoices by the student's name.
     *
     * @param name The name of the student.
     * @return A list of invoices associated with the student.
     */
    List<Invoice> findByStudentName(String name);

    /**
     * Deletes an invoice by its unique ID.
     *
     * @param id The ID of the invoice to delete.
     * @return True if deletion was successful; false otherwise.
     */
    boolean delete(int id);
}
