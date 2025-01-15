package org.invoice.repository;

import org.invoice.domain.Student;
import java.util.List;

public interface StudentRepository {
    Student save(Student s);
    Student findById(Long id);
    List<Student> findAll();
    boolean delete(Long id);
    void assignCourseToStudent(Long studentId, Long courseId);
}
