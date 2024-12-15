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

        System.out.println("Making user with email: " + user.getEmail());
        System.out.println("Making user with password: " + user.getPassword());

        return userRepository.save(user);
    }

    public User authenticate(String email, String password) {

        // Check if user with this email exists
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User existing = userOptional.get();

            // Verify password match
            if(!existing.getPassword().equals(password)) {
                throw new InvalidInputException("Incorrect password, please try again");
            }

            return existing;
        } else {
            throw new InvalidInputException("Email not registered, please sign-up");
        }
    }

}
