package com.example.loginscreen.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.loginscreen.model.User;

public interface LoginScreenRepository extends MongoRepository<User, String>{

    List<User> findAllByName(String name);
    Optional<User> findByName(String name);
}
