package org.invoice.domain;

public class Course {
    private Long id;
    private String name;
    private double fee;

    public Course() {}
    public Course(Long i, String n, double f) {
        id = i; name = n; fee = f;
    }

    public Long getId() { return id; }
    public void setId(Long i) { id = i; }

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public double getFee() { return fee; }
    public void setFee(double f) { fee = f; }
}
