// File: src/main/java/org/invoice/domain/Course.java

package org.invoice.domain;

/**
 * Represents a course in the system.
 */
public class Course {
    private Integer id;
    private String name;
    private double fee;

    // Constructors
    public Course() {}

    public Course(Integer id, String name, double fee) {
        this.id = id;
        this.name = name;
        this.fee = fee;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    // toString
    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fee=" + fee +
                '}';
    }
}
