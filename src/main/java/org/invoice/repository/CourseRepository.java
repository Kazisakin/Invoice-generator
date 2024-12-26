// File: src/main/java/org/invoice/repository/CourseRepository.java

package org.invoice.repository;

import org.invoice.domain.Course;

import java.util.List;

/**
 * Repository interface for managing Course entities.
 */
public interface CourseRepository {
    /**
     * Saves a new course to the database.
     *
     * @param course The course to save.
     * @return The saved course with an assigned ID.
     */
    Course save(Course course);

    /**
     * Finds a course by its unique ID.
     *
     * @param id The ID of the course.
     * @return The Course object if found; null otherwise.
     */
    Course findById(int id);

    /**
     * Retrieves all courses from the database.
     *
     * @return A list of all courses.
     */
    List<Course> findAll();

    /**
     * Deletes a course by its unique ID.
     *
     * @param id The ID of the course to delete.
     * @return True if deletion was successful; false otherwise.
     */
    boolean delete(int id);
}
