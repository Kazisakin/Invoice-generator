package org.invoice.domain;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private Long id;
    private String uniqueId; // e.g., STU2023-001
    private String name;
    private String email;
    private List<Course> courses;

    public Student() {
        courses = new ArrayList<>();
    }

    public Student(Long i, String uid, String n, String e) {
        id = i; uniqueId = uid; name = n; email = e;
        courses = new ArrayList<>();
    }

    public Long getId() { return id; }
    public void setId(Long i) { id = i; }

    public String getUniqueId() { return uniqueId; }
    public void setUniqueId(String uid) { uniqueId = uid; }

    public String getName() { return name; }
    public void setName(String n) { name = n; }

    public String getEmail() { return email; }
    public void setEmail(String e) { email = e; }

    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> c) { courses = c; }
    public void addCourse(Course c) { courses.add(c); }
}
