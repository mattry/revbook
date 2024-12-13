package com.revbook.demo.service;

import com.revbook.demo.entity.User;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        // Verify email is not already in use
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if(userOptional.isPresent()){
            throw new EmailAlreadyInUseException("Email is already in use");
        }

        // Check if the email is null or blank
        // This will probably get handled on the frontend form, but we'll verify anyway
        if(user.getEmail() == null || user.getEmail().isBlank()){
            throw new InvalidInputException("Invalid email");
        }

        return userRepository.save(user);
    }
}