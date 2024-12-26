// File: src/main/java/org/invoice/service/UserService.java

package org.invoice.service;

import org.invoice.domain.User;
import org.invoice.exception.ServiceException;
import org.invoice.repository.UserRepository;

/**
 * Service layer for managing users.
 */
public class UserService {

    private final UserRepository userRepository;

    /**
     * Constructs a UserService with the specified UserRepository.
     *
     * @param userRepository The repository responsible for user data operations.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user.
     *
     * @param user The user to create.
     * @return The created User object with an assigned ID.
     * @throws ServiceException If an error occurs during user creation.
     */
    public User createUser(User user) throws ServiceException {
        try {
            // In a real application, hash the password before saving.
            return userRepository.save(user);
        } catch (Exception e) {
            throw new ServiceException("Failed to create user.", e);
        }
    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated User object if credentials are valid; null otherwise.
     * @throws ServiceException If an error occurs during authentication.
     */
    public User authenticateUser(String username, String password) throws ServiceException {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null && user.getPassword().equals(password)) { // Note: Use hashed passwords
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new ServiceException("Failed to authenticate user.", e);
        }
    }

    /**
     * Retrieves a user by their unique ID.
     *
     * @param id The ID of the user.
     * @return The User object if found; null otherwise.
     * @throws ServiceException If an error occurs during retrieval.
     */
    public User findUserById(int id) throws ServiceException {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to retrieve user with ID: " + id, e);
        }
    }

    /**
     * Deletes a user by their unique ID.
     *
     * @param id The ID of the user to delete.
     * @return True if deletion was successful; false otherwise.
     * @throws ServiceException If an error occurs during deletion.
     */
    public boolean deleteUser(int id) throws ServiceException {
        try {
            return userRepository.delete(id);
        } catch (Exception e) {
            throw new ServiceException("Failed to delete user with ID: " + id, e);
        }
    }
}
