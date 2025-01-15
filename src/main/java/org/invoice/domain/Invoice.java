package org.invoice.domain;

import java.time.LocalDate;

public class Invoice {
    private Long id;
    private String invoiceNumber; // unique, e.g., INV2023-0001
    private Student student;
    private double subtotal;
    private double discount;
    private double taxRate;
    private double taxAmount;
    private double total;
    private LocalDate dateIssued;
    private LocalDate dueDate;

    public Invoice() {}

    public Invoice(Long i, String invNum, Student s, double sub, double disc, double tr, double ta, double tot, LocalDate di, LocalDate dd) {
        id = i; invoiceNumber = invNum; student = s;
        subtotal = sub; discount = disc; taxRate = tr;
        taxAmount = ta; total = tot; dateIssued = di; dueDate = dd;
    }

    public Long getId() { return id; }
    public void setId(Long i) { id = i; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String n) { invoiceNumber = n; }

    public Student getStudent() { return student; }
    public void setStudent(Student s) { student = s; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double d) { subtotal = d; }

    public double getDiscount() { return discount; }
    public void setDiscount(double d) { discount = d; }

    public double getTaxRate() { return taxRate; }
    public void setTaxRate(double t) { taxRate = t; }

    public double getTaxAmount() { return taxAmount; }
    public void setTaxAmount(double t) { taxAmount = t; }

    public double getTotal() { return total; }
    public void setTotal(double t) { total = t; }

    public LocalDate getDateIssued() { return dateIssued; }
    public void setDateIssued(LocalDate d) { dateIssued = d; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate d) { dueDate = d; }
}
