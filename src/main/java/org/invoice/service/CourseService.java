package org.invoice.service;

import org.invoice.domain.Course;
import org.invoice.exception.ServiceException;
import org.invoice.repository.CourseRepository;
import java.util.List;

public class CourseService {
    private final CourseRepository repo;

    public CourseService(CourseRepository r){ repo=r; }

    public Course createOrUpdateCourse(Course c){
        try{ return repo.save(c); }
        catch(Exception e){ throw new ServiceException("Save course failed", e); }
    }

    public Course findById(Long id){
        try{ return repo.findById(id); }
        catch(Exception e){ throw new ServiceException("Find course failed", e); }
    }

    public List<Course> listAllCourses(){
        try{ return repo.findAll(); }
        catch(Exception e){ throw new ServiceException("List courses failed", e); }
    }

    public boolean deleteCourse(Long id){
        try{ return repo.delete(id); }
        catch(Exception e){ throw new ServiceException("Delete course failed", e); }
    }
}
