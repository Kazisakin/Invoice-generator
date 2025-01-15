package org.invoice.repository;

import org.invoice.domain.Student;
import org.invoice.exception.RepositoryException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {
    private final Connection connection;

    public StudentRepositoryImpl() {
        try { connection = ConnectionManager.getConnection(); }
        catch(SQLException e){ throw new RepositoryException("DB error", e); }
    }
    public Student save(Student s) {
        if(s.getId()==null){
            String sql="INSERT INTO students (name, email) VALUES(?,?)";
            // If you want to store 'uniqueId' in DB, add a column 'unique_id' to the table
            try(PreparedStatement ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1,s.getName());
                ps.setString(2,s.getEmail());
                ps.executeUpdate();
                try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) s.setId(rs.getLong(1)); }
            }catch(Exception e){ throw new RepositoryException("Save student failed", e); }
        } else {
            String sql="UPDATE students SET name=?, email=? WHERE id=?";
            try(PreparedStatement ps=connection.prepareStatement(sql)){
                ps.setString(1,s.getName());
                ps.setString(2,s.getEmail());
                ps.setLong(3,s.getId());
                ps.executeUpdate();
            }catch(Exception e){ throw new RepositoryException("Update student failed", e); }
        }
        return s;
    }
    public Student findById(Long id){
        String sql="SELECT id, name, email FROM students WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    Student st=new Student();
                    st.setId(rs.getLong("id"));
                    st.setName(rs.getString("name"));
                    st.setEmail(rs.getString("email"));
                    return st;
                }
            }
        }catch(Exception e){ throw new RepositoryException("Find student by id failed", e); }
        return null;
    }
    public List<Student> findAll(){
        List<Student> list=new ArrayList<>();
        String sql="SELECT id, name, email FROM students";
        try(Statement st=connection.createStatement(); ResultSet rs=st.executeQuery(sql)){
            while(rs.next()){
                Student s=new Student();
                s.setId(rs.getLong("id"));
                s.setName(rs.getString("name"));
                s.setEmail(rs.getString("email"));
                list.add(s);
            }
        }catch(Exception e){ throw new RepositoryException("List students failed", e); }
        return list;
    }
    public boolean delete(Long id){
        String sql="DELETE FROM students WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch(Exception e){ throw new RepositoryException("Delete student failed", e); }
    }
    public void assignCourseToStudent(Long studentId, Long courseId){
        String sql="INSERT INTO student_courses (student_id, course_id) VALUES(?,?)";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1, studentId);
            ps.setLong(2, courseId);
            ps.executeUpdate();
        }catch(Exception e){ throw new RepositoryException("Assign course failed", e); }
    }
}
