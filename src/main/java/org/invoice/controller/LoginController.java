package org.invoice.controller;

import org.invoice.domain.User;
import org.invoice.service.UserService;

public class LoginController {
    private final UserService userService;
    public LoginController(UserService us) { userService = us; }
    public User login(String username, String password){
        return userService.authenticate(username, password);
    }
}
