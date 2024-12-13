package com.revbook.demo.controller;

import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.service.UserService;
import com.revbook.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userSerice;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userSerice.registerUser(user);
            return ResponseEntity.ok(registeredUser);
        } catch (EmailAlreadyInUseException | InvalidInputException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

}
