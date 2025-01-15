package org.invoice.repository;

import org.invoice.domain.Course;
import java.util.List;

public interface CourseRepository {
    Course save(Course c);
    Course findById(Long id);
    List<Course> findAll();
    boolean delete(Long id);
}
