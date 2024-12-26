// File: src/main/java/org/invoice/domain/Invoice.java

package org.invoice.domain;

import java.util.Date;

/**
 * Represents an invoice in the system.
 */
public class Invoice {
    private Integer id;
    private Student student;
    private Course course;
    private double discount;
    private double totalAmount;
    private Date invoiceDate;

    // Constructors
    public Invoice() {}

    public Invoice(Integer id, Student student, Course course, double discount, double totalAmount, Date invoiceDate) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.discount = discount;
        this.totalAmount = totalAmount;
        this.invoiceDate = invoiceDate;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    // toString
    @Override
    public String toString() {
        return "Invoice{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                ", discount=" + discount +
                ", totalAmount=" + totalAmount +
                ", invoiceDate=" + invoiceDate +
                '}';
    }
}
