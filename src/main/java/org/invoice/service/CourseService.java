// File: src/main/java/org/invoice/service/CourseService.java

package org.invoice.service;

import org.invoice.domain.Course;
import org.invoice.exception.ServiceException;
import org.invoice.repository.CourseRepository;

import java.util.List;

/**
 * Service layer for managing courses.
 */
public class CourseService {

    private final CourseRepository courseRepository;

    /**
     * Constructs a CourseService with the specified CourseRepository.
     *
     * @param courseRepository The repository responsible for course data operations.
     */
    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    /**
     * Creates a new course.
     *
     * @param course The course to create.
     * @return The created Course object with an assigned ID.
     * @throws ServiceException If an error occurs during course creation.
     */
    public Course createCourse(Course course) throws ServiceException {
        try {
            return courseRepository.save(course);
        } catch (Exception e) {
            throw new ServiceException("Failed to create course.", e);
        }
    }

    /**
     * Retrieves a course by its unique ID.
     *
     * @param id The ID of the course.
     * @return The Course object if found; null otherwise.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public Course findCourseById(int id) throws ServiceException {
        try {
            return courseRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve course with ID: " + id, e);
        }
    }

    /**
     * Retrieves all courses.
     *
     * @return A list of all Course objects.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public List<Course> listAllCourses() throws ServiceException {
        try {
            return courseRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve all courses.", e);
        }
    }

    /**
     * Deletes a course by its unique ID.
     *
     * @param id The ID of the course to delete.
     * @return True if deletion was successful; false otherwise.
     * @throws ServiceException If an error occurs during deletion.
     */
    public boolean deleteCourse(int id) throws ServiceException {
        try {
            return courseRepository.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete course with ID: " + id, e);
        }
    }
}
