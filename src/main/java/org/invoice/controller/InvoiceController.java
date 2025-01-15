package org.invoice.controller;

import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.service.InvoiceService;
import java.util.List;

public class InvoiceController {
    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService s) {
        invoiceService = s;
    }

    public Invoice createInvoice(Student stu, double subtotal, double discount, double taxRate){
        return invoiceService.createInvoice(stu, subtotal, discount, taxRate);
    }

    public Invoice findInvoiceById(Long id){
        return invoiceService.findById(id);
    }

    public List<Invoice> listAllInvoices(){
        return invoiceService.listAllInvoices();
    }

    public boolean deleteInvoice(Long id){
        return invoiceService.deleteInvoice(id);
    }

    public List<Invoice> searchInvoicesByName(String name){
        return invoiceService.searchInvoicesByStudentName(name);
    }
}
