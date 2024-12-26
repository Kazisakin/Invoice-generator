// File: src/main/java/org/invoice/repository/StudentRepository.java

package org.invoice.repository;

import org.invoice.domain.Student;

import java.util.List;

/**
 * Repository interface for managing Student entities.
 */
public interface StudentRepository {
    /**
     * Saves a new student to the database.
     *
     * @param student The student to save.
     * @return The saved student with an assigned ID.
     */
    Student save(Student student);

    /**
     * Finds a student by their unique ID.
     *
     * @param id The ID of the student.
     * @return The Student object if found; null otherwise.
     */
    Student findById(int id);

    /**
     * Retrieves all students from the database.
     *
     * @return A list of all students.
     */
    List<Student> findAll();

    /**
     * Deletes a student by their unique ID.
     *
     * @param id The ID of the student to delete.
     * @return True if deletion was successful; false otherwise.
     */
    boolean delete(int id);

    /**
     * Finds a student by their email.
     *
     * @param email The email of the student.
     * @return The Student object if found; null otherwise.
     */
    Student findByEmail(String email);
}
