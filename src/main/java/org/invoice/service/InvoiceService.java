package org.invoice.service;

import org.invoice.domain.Invoice;
import org.invoice.domain.Student;
import org.invoice.exception.ServiceException;
import org.invoice.repository.InvoiceRepository;
import java.time.LocalDate;
import java.util.List;

public class InvoiceService {
    private final InvoiceRepository repo;

    public InvoiceService(InvoiceRepository r){ repo=r; }

    public Invoice createInvoice(Student stu, double subtotal, double discount, double taxRate){
        try{
            double st=Math.max(0, subtotal-discount);
            double tax=st*taxRate;
            double tot=st+tax;
            Invoice inv=new Invoice();
            inv.setInvoiceNumber(generateInvoiceNumber());
            inv.setStudent(stu);
            inv.setSubtotal(st);
            inv.setDiscount(discount);
            inv.setTaxRate(taxRate);
            inv.setTaxAmount(tax);
            inv.setTotal(tot);
            inv.setDateIssued(LocalDate.now());
            inv.setDueDate(LocalDate.now().plusDays(30));
            Invoice saved = repo.save(inv);
            // Optional auto-email stub
            sendInvoiceEmail(saved);
            return saved;
        }catch(Exception e){
            throw new ServiceException("Create invoice failed", e);
        }
    }

    // Example method: combined invoice for multiple courses
    // public Invoice createCombinedInvoice(Student s, List<Course> courses, double disc, double taxRate){...}

    private String generateInvoiceNumber(){
        return "INV" + System.currentTimeMillis();
    }

    private void sendInvoiceEmail(Invoice inv){
        // Stub, e.g. "Emailing invoice #inv.invoiceNumber to inv.student.email"
        // For demonstration only
    }

    public Invoice findById(Long id){
        try{ return repo.findById(id); }
        catch(Exception e){ throw new ServiceException("Find invoice failed", e); }
    }

    public List<Invoice> listAllInvoices(){
        try{ return repo.findAll(); }
        catch(Exception e){ throw new ServiceException("List invoices failed", e); }
    }

    public boolean deleteInvoice(Long id){
        try{ return repo.delete(id); }
        catch(Exception e){ throw new ServiceException("Delete invoice failed", e); }
    }

    public List<Invoice> searchInvoicesByStudentName(String name){
        try{ return repo.findByCustomerName(name); }
        catch(Exception e){ throw new ServiceException("Search invoice by name failed", e); }
    }
}
