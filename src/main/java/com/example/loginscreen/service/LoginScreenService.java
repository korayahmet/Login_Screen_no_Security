package com.example.loginscreen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.loginscreen.exception.UserAlreadyExistsException;
import com.example.loginscreen.exception.UserNotFoundException;
import com.example.loginscreen.model.User;
import com.example.loginscreen.repository.LoginScreenRepository;

import lombok.AllArgsConstructor;

//@Component
@Service
@AllArgsConstructor
public class LoginScreenService {

    public final LoginScreenRepository LoginScreenRepository;

    public List<User> getUsers(String name) {

        if(name == null){
            return LoginScreenRepository.findAll();
        } else {
            return LoginScreenRepository.findAllByName(name);
        }
    }

    public User createUser(User newUser) {

        Optional<User> UserByName = LoginScreenRepository.findByName(newUser.getName());
        if (UserByName.isPresent()){
            throw new UserAlreadyExistsException("User already exists with username: " + newUser.getName());
        }
        return LoginScreenRepository.save(newUser);
    }

    public void deleteUser(String id) {
        LoginScreenRepository.deleteById(id);
    }

    public User getUserById(String id) {
        return LoginScreenRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public void updateUser(String id, User newUser) {
        User oldUser = getUserById(id);
        oldUser.setName(newUser.getName());
        LoginScreenRepository.save(oldUser);
    }
}
