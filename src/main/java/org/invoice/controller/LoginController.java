package org.invoice.controller;

import org.invoice.domain.User;
import org.invoice.exception.ServiceException;
import org.invoice.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller class responsible for handling user login operations.
 */
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    /**
     * Constructs a LoginController with the specified UserService.
     *
     * @param userService The service layer responsible for user operations.
     */
    public LoginController(UserService userService) {
        this.userService = userService;
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
            User user = userService.authenticateUser(username, password);
            if (user != null) {
                logger.info("User authenticated successfully: {}", username);
            } else {
                logger.warn("Authentication failed for user: {}", username);
            }
            return user;
        } catch (ServiceException e) {
            logger.error("Failed to authenticate user: {}", username, e);
            throw e;
        }
    }

    /**
     * Handles user login process.
     *
     * @param username The username.
     * @param password The password.
     * @return The authenticated User object if login is successful; null otherwise.
     */
    public User login(String username, String password) {
        try {
            // Authenticate the user
            User user = authenticateUser(username, password);

            // If user is authenticated, proceed with login
            if (user != null) {
                logger.info("Login successful for user: {}", username);
                return user;
            } else {
                logger.warn("Login failed for user: {}", username);
                return null;
            }
        } catch (ServiceException e) {
            logger.error("Error during login process for user: {}", username, e);
            return null;
        }
    }
}
