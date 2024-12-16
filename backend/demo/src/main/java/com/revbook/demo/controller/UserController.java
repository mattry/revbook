package com.revbook.demo.controller;

import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.dto.UserAuthDTO;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.service.UserService;
import com.revbook.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserAuthDTO userAuthDTO) {
        try {
            UserDTO registeredUserDTO = userService.registerUser(userAuthDTO);
            return ResponseEntity.ok(registeredUserDTO);
        } catch (EmailAlreadyInUseException | InvalidInputException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserAuthDTO userAuthDTO) {
        try{
            UserDTO authUserDTO = userService.authenticate(userAuthDTO);
            return ResponseEntity.ok(authUserDTO);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

}
