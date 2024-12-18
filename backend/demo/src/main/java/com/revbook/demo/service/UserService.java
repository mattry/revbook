package com.revbook.demo.service;

import com.revbook.demo.dto.UpdateNameDTO;
import com.revbook.demo.dto.UpdatePasswordDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.dto.UserAuthDTO;
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

    public UserDTO registerUser(UserAuthDTO userAuthDTO) {
        // Verify email is not already in use
        Optional<User> userOptional = userRepository.findByEmail(userAuthDTO.getEmail());
        if(userOptional.isPresent()){
            throw new EmailAlreadyInUseException("Email is already in use");
        }

        // Check if the email is null or blank
        // This will probably get handled on the frontend form, but we'll verify anyway
        if(userAuthDTO.getEmail() == null || userAuthDTO.getEmail().isBlank()){
            throw new InvalidInputException("Invalid email");
        }

        User user = new User();
        user.setEmail(userAuthDTO.getEmail());
        user.setFirstName(userAuthDTO.getFirstName());
        user.setLastName(userAuthDTO.getLastName());
        user.setPassword(userAuthDTO.getPassword());

        System.out.println("Making user with email: " + user.getEmail());
        System.out.println("Making user with password: " + user.getPassword());

        User saved = userRepository.save(user);

        return mapToDTO(saved);
    }

    public UserDTO authenticate(UserAuthDTO userAuthDTO ) {

        // Check if user with this email exists
        Optional<User> userOptional = userRepository.findByEmail(userAuthDTO.getEmail());
        if (userOptional.isPresent()) {
            User existing = userOptional.get();

            // Verify password match
            if(!existing.getPassword().equals(userAuthDTO.getPassword())) {
                throw new InvalidInputException("Incorrect password, please try again");
            }

            return mapToDTO(existing);
        } else {
            throw new InvalidInputException("Email not registered, please sign-up");
        }
    }

    public UserDTO updateName(UpdateNameDTO updateNameDTO) {
        Optional<User> userOptional = userRepository.findById(updateNameDTO.getUserId());
        if (userOptional.isPresent()) {
            User existing = userOptional.get();
            existing.setFirstName(updateNameDTO.getFirstName());
            existing.setLastName(updateNameDTO.getLastName());
            User updated = userRepository.save(existing);
            return mapToDTO(updated);
        } else {
            throw new InvalidInputException("User not found");
        }
    }

    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        Optional<User> userOptional = userRepository.findById(updatePasswordDTO.getUserId());
        if (userOptional.isPresent()) {
            User existing = userOptional.get();
            if (existing.getPassword().equals(updatePasswordDTO.getCurrentPassword())) {
                existing.setPassword(updatePasswordDTO.getNewPassword());
                User updated = userRepository.save(existing);
                return;
            } else {
                throw new InvalidInputException("Password mismatch");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            return mapToDTO(user);
        } else {
            throw new InvalidInputException("Invalid user id");
        }
    }



    // UserDTOs do not have password fields so they can be sent to the client without exposing sensitive information
    public UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserId(user.getUserId());

        return userDTO;
    }

}
