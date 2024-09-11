package com.example.loginscreen.service;

import org.springframework.stereotype.Service;
import com.example.loginscreen.model.User;
import com.example.loginscreen.repository.LoginScreenRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private LoginScreenRepository loginScreenRepository;

    public boolean authenticateUser(String username, String password) {
        User user = loginScreenRepository.findByName(username).orElse(null);
        if (user != null) {
            return user.getPassword().equals(password);
        }
        return false;
    }
}