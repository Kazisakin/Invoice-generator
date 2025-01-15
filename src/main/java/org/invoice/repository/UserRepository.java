package org.invoice.repository;

import org.invoice.domain.User;
import java.util.List;

public interface UserRepository {
    User save(User u);
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    boolean delete(Long id);
}
