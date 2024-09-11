package com.example.loginscreen.controller;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;

import org.springframework.http.ResponseEntity;
//import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.loginscreen.exception.RequiredFieldException;
import com.example.loginscreen.exception.UserAlreadyExistsException;
import com.example.loginscreen.exception.UserNotFoundException;
import com.example.loginscreen.model.User;
import com.example.loginscreen.service.LoginScreenService;

import lombok.AllArgsConstructor;

@RestController     //bir classı controller yapmak için kullanılır
@RequestMapping("/users")           //url eklendi
@AllArgsConstructor
public class LoginScreenController {

    private final LoginScreenService loginScreenService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) String name){

        //HttpStatus.OK -> sag tiklayip static yaparsak asagidaki gibi OK olur.
        return new ResponseEntity<>(loginScreenService.getUsers(name), OK);
    }

    @GetMapping("{id}") //getUser(@PathVariable("id") String UserId) asagidakiyle ayni
    public ResponseEntity<User> getUser(@PathVariable String id){
        return new ResponseEntity<>(getUserById(id), OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User newUser) {

        if (newUser.getName() == null) {
            throw new RequiredFieldException("Name is required");
        }
        if (newUser.getPassword() == null) {
            throw new RequiredFieldException("Password is required");
        }
        return new ResponseEntity<>(loginScreenService.createUser(newUser), CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> replaceUser(@PathVariable String id, @RequestBody User newUser){

        loginScreenService.updateUser(id, newUser);

        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id){
        loginScreenService.deleteUser(id);
        return new ResponseEntity<>(OK);
    }

    //id ile kullanicileri cekme methodu
    private User getUserById(String id) {

        return loginScreenService.getUserById(id);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex){
        return new ResponseEntity<>(ex.getMessage(), CONFLICT);
    }
    @ExceptionHandler(RequiredFieldException.class)
    public ResponseEntity<String> RequiredFieldException(RequiredFieldException ex){
        return new ResponseEntity<>(ex.getMessage(), CONFLICT);
    }
}
