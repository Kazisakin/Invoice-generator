package org.invoice.repository;

import org.invoice.domain.User;
import org.invoice.exception.RepositoryException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection;
    public UserRepositoryImpl() {
        try { connection = ConnectionManager.getConnection(); }
        catch(SQLException e){ throw new RepositoryException("DB error", e); }
    }
    public User save(User u) {
        if(u.getId()==null){
            String sql="INSERT INTO users (username, password, role) VALUES(?,?,?)";
            try(PreparedStatement ps=connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1,u.getUsername());
                ps.setString(2,u.getPassword());
                ps.setString(3,u.getRole());
                ps.executeUpdate();
                try(ResultSet rs=ps.getGeneratedKeys()){ if(rs.next()) u.setId(rs.getLong(1)); }
            }catch(Exception e){ throw new RepositoryException("Save user failed", e); }
        } else {
            String sql="UPDATE users SET username=?, password=?, role=? WHERE id=?";
            try(PreparedStatement ps=connection.prepareStatement(sql)){
                ps.setString(1,u.getUsername());
                ps.setString(2,u.getPassword());
                ps.setString(3,u.getRole());
                ps.setLong(4,u.getId());
                ps.executeUpdate();
            }catch(Exception e){ throw new RepositoryException("Update user failed", e); }
        }
        return u;
    }
    public User findById(Long id){
        String sql="SELECT id, username, password, role FROM users WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    User user=new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }catch(Exception e){ throw new RepositoryException("Find user failed", e); }
        return null;
    }
    public User findByUsername(String username){
        String sql="SELECT id, username, password, role FROM users WHERE username=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setString(1,username);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    User user=new User();
                    user.setId(rs.getLong("id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setRole(rs.getString("role"));
                    return user;
                }
            }
        }catch(Exception e){ throw new RepositoryException("Find user by username failed", e); }
        return null;
    }
    public List<User> findAll(){
        List<User> list=new ArrayList<>();
        String sql="SELECT id, username, password, role FROM users";
        try(Statement st=connection.createStatement(); ResultSet rs=st.executeQuery(sql)){
            while(rs.next()){
                User user=new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                list.add(user);
            }
        }catch(Exception e){ throw new RepositoryException("List users failed", e); }
        return list;
    }
    public boolean delete(Long id){
        String sql="DELETE FROM users WHERE id=?";
        try(PreparedStatement ps=connection.prepareStatement(sql)){
            ps.setLong(1,id);
            int rows=ps.executeUpdate();
            return rows>0;
        }catch(Exception e){ throw new RepositoryException("Delete user failed", e); }
    }
}
