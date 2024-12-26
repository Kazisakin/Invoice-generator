// File: src/main/java/org/invoice/repository/UserRepository.java

package org.invoice.repository;

import org.invoice.domain.User;

import java.util.List;

/**
 * Repository interface for managing User entities.
 */
public interface UserRepository {
    /**
     * Saves a new user to the database.
     *
     * @param user The user to save.
     * @return The saved user with an assigned ID.
     */
    User save(User user);

    /**
     * Finds a user by their unique ID.
     *
     * @param id The ID of the user.
     * @return The User object if found; null otherwise.
     */
    User findById(int id);

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    List<User> findAll();

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to delete.
     * @return True if deletion was successful; false otherwise.
     */
    boolean delete(int id);

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user.
     * @return The User object if found; null otherwise.
     */
    User findByUsername(String username);
}
