package org.invoice.service;

import org.invoice.domain.User;
import org.invoice.exception.ServiceException;
import org.invoice.repository.UserRepository;
import java.util.List;

public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository r){ repo=r; }

    public User authenticate(String username, String password){
        try{
            User u=repo.findByUsername(username);
            if(u!=null && u.getPassword().equals(password)) return u;
            return null;
        }catch(Exception e){ throw new ServiceException("Auth user failed", e); }
    }

    public User saveUser(User u){
        try{ return repo.save(u); }
        catch(Exception e){ throw new ServiceException("Save user failed", e); }
    }

    public User findById(Long id){
        try{ return repo.findById(id); }
        catch(Exception e){ throw new ServiceException("Find user failed", e); }
    }

    public List<User> listAll(){
        try{ return repo.findAll(); }
        catch(Exception e){ throw new ServiceException("List user failed", e); }
    }

    public boolean deleteUser(Long id){
        try{ return repo.delete(id); }
        catch(Exception e){ throw new ServiceException("Delete user failed", e); }
    }
}
