package org.invoice.repository;

import org.invoice.domain.Course;
import org.invoice.exception.RepositoryException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    private final Connection connection;

    public CourseRepositoryImpl() {
        try { connection = ConnectionManager.getConnection(); }
        catch(SQLException e){ throw new RepositoryException("DB error", e); }
    }
    public Course save(Course c) {
        if(c.getId()==null){
            String sql="INSERT INTO courses (name, fee) VALUES(?,?)";
            try(PreparedStatement ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1,c.getName());
                ps.setDouble(2,c.getFee());
                ps.executeUpdate();
                try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) c.setId(rs.getLong(1)); }
            }catch(Exception e){ throw new RepositoryException("Save course failed", e); }
        } else {
            String sql="UPDATE courses SET name=?, fee=? WHERE id=?";
            try(PreparedStatement ps=connection.prepareStatement(sql)){
                ps.setString(1,c.getName());
                ps.setDouble(2,c.getFee());
                ps.setLong(3,c.getId());
                ps.executeUpdate();
            }catch(Exception e){ throw new RepositoryException("Update course failed", e); }
        }
        return c;
    }
    public Course findById(Long id){
        String sql="SELECT id, name, fee FROM courses WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    Course c=new Course();
                    c.setId(rs.getLong("id"));
                    c.setName(rs.getString("name"));
                    c.setFee(rs.getDouble("fee"));
                    return c;
                }
            }
        }catch(Exception e){ throw new RepositoryException("Find course by id failed", e); }
        return null;
    }
    public List<Course> findAll(){
        List<Course> list=new ArrayList<>();
        String sql="SELECT id, name, fee FROM courses";
        try(Statement st=connection.createStatement(); ResultSet rs=st.executeQuery(sql)){
            while(rs.next()){
                Course c=new Course();
                c.setId(rs.getLong("id"));
                c.setName(rs.getString("name"));
                c.setFee(rs.getDouble("fee"));
                list.add(c);
            }
        }catch(Exception e){ throw new RepositoryException("List courses failed", e); }
        return list;
    }
    public boolean delete(Long id){
        String sql="DELETE FROM courses WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch(Exception e){ throw new RepositoryException("Delete course failed", e); }
    }
}
