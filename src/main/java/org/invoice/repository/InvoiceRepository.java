package org.invoice.repository;

import org.invoice.domain.Invoice;
import java.util.List;

public interface InvoiceRepository {
    Invoice save(Invoice i);
    Invoice findById(Long id);
    List<Invoice> findAll();
    List<Invoice> findByCustomerName(String name);
    boolean delete(Long id);
}
