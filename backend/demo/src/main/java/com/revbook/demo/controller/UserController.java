package com.revbook.demo.controller;

import com.revbook.demo.dto.UpdateNameDTO;
import com.revbook.demo.dto.UpdatePasswordDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.dto.UserAuthDTO;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.service.UserService;
import com.revbook.demo.entity.User;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserAuthDTO userAuthDTO, HttpSession session) {
        try {
            UserDTO registeredUserDTO = userService.registerUser(userAuthDTO);
            session.setAttribute("userId", registeredUserDTO.getUserId());
            session.setAttribute("userPassword", userAuthDTO.getPassword());
            return ResponseEntity.ok(registeredUserDTO);
        } catch (EmailAlreadyInUseException | InvalidInputException e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserAuthDTO userAuthDTO,  HttpSession session) {
        try{
            UserDTO authUserDTO = userService.authenticate(userAuthDTO);
            session.setAttribute("userId", authUserDTO.getUserId());
            session.setAttribute("userPassword", userAuthDTO.getPassword());
            return ResponseEntity.ok(authUserDTO);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PatchMapping("/update-name")
    public ResponseEntity<UserDTO> updateName(@RequestBody UpdateNameDTO updateNameDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            UserDTO updatedUser = userService.updateName(updateNameDTO);
            return ResponseEntity.ok(updatedUser);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            userService.updatePassword(updatePasswordDTO);
            return ResponseEntity.ok().build();
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId, HttpSession session){
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            UserDTO userDTO = userService.getUserById(userId);
            return ResponseEntity.ok(userDTO);
        } catch (InvalidInputException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }

}
