package com.revbook.demo;

import com.revbook.demo.dto.UserAuthDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.UserRepository;
import com.revbook.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserAuthDTO validUserAuthDTO;
    private User savedUser;

    @BeforeEach
    void setUp() {
        // Set up test data for UserAuthDTO and User
        validUserAuthDTO = new UserAuthDTO(null, "test@test.com", "John", "Doe", "password");

        savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setEmail("test@test.com");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setPassword("password"); // For simplicity, using plain text
    }

    @Test
    void shouldRegisterUserSuccessfully_whenEmailIsNotInUse() {
        // Arrange
        when(userRepository.findByEmail(validUserAuthDTO.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UserDTO result = userService.registerUser(validUserAuthDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getFirstName(), result.getFirstName());
        assertEquals(savedUser.getLastName(), result.getLastName());
        assertEquals(savedUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowEmailAlreadyInUseException_whenEmailIsAlreadyRegistered() {
        // Arrange
        when(userRepository.findByEmail(validUserAuthDTO.getEmail())).thenReturn(Optional.of(savedUser));

        // Act & Assert
        assertThrows(EmailAlreadyInUseException.class, () -> userService.registerUser(validUserAuthDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldAuthenticateUserSuccessfully_whenValidCredentialsProvided() {
        // Arrange
        when(userRepository.findByEmail(validUserAuthDTO.getEmail())).thenReturn(Optional.of(savedUser));

        // Act
        UserDTO result = userService.authenticate(validUserAuthDTO);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getUserId(), result.getUserId());
        assertEquals(savedUser.getFirstName(), result.getFirstName());
        assertEquals(savedUser.getLastName(), result.getLastName());
        assertEquals(savedUser.getEmail(), result.getEmail());
        verify(userRepository, times(1)).findByEmail(validUserAuthDTO.getEmail());
    }

    @Test
    void shouldThrowInvalidInputException_whenPasswordIsIncorrect() {
        // Arrange
        UserAuthDTO invalidPasswordAuthDTO = new UserAuthDTO(null, "test@test.com", "John", "Doe", "wrongpassword");
        when(userRepository.findByEmail(invalidPasswordAuthDTO.getEmail())).thenReturn(Optional.of(savedUser));

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> userService.authenticate(invalidPasswordAuthDTO));
        verify(userRepository, times(1)).findByEmail(invalidPasswordAuthDTO.getEmail());
    }

    @Test
    void shouldThrowInvalidInputException_whenEmailIsNotRegistered() {
        // Arrange
        when(userRepository.findByEmail(validUserAuthDTO.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> userService.authenticate(validUserAuthDTO));
        verify(userRepository, times(1)).findByEmail(validUserAuthDTO.getEmail());
    }
}