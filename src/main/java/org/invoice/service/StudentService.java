package org.invoice.service;

import org.invoice.domain.Student;
import org.invoice.exception.ServiceException;
import org.invoice.repository.StudentRepository;
import java.util.List;

public class StudentService {
    private final StudentRepository repo;

    public StudentService(StudentRepository r){ repo=r; }

    public Student createOrUpdateStudent(Student s){
        try{
            if(s.getId()==null){
                // Generate uniqueId if desired, e.g. "STU" + System.currentTimeMillis()
                String uniqueId = "STU" + System.currentTimeMillis();
                s.setUniqueId(uniqueId);
            }
            return repo.save(s);
        }catch(Exception e){ throw new ServiceException("Save student failed", e); }
    }

    public Student findById(Long id){
        try{ return repo.findById(id); }
        catch(Exception e){ throw new ServiceException("Find student failed", e); }
    }

    public List<Student> listAllStudents(){
        try{ return repo.findAll(); }
        catch(Exception e){ throw new ServiceException("List students failed", e); }
    }

    public boolean deleteStudent(Long id){
        try{ return repo.delete(id); }
        catch(Exception e){ throw new ServiceException("Delete student failed", e); }
    }

    public void assignCourse(Long studentId, Long courseId){
        try{ repo.assignCourseToStudent(studentId, courseId); }
        catch(Exception e){ throw new ServiceException("Assign course failed", e); }
    }
}
