package com.revbook.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revbook.demo.controller.UserController;
import com.revbook.demo.dto.*;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private ObjectMapper objectMapper;

    // Shared test data
    private UserAuthDTO validUserAuthDTO;
    private UserAuthDTO invalidUserAuthDTO;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Init MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        // Common test data
        validUserAuthDTO = new UserAuthDTO(null, "test@example.com", "Test", "User", "password");
        invalidUserAuthDTO = new UserAuthDTO(null, "test@example.com", "Test", "User", "wrong-password");
        userDTO = new UserDTO(1L, "test@example.com", "Test", "User");
    }

    @Test
    void registerUser_Success() throws Exception {
        // Arrange
        Mockito.when(userService.registerUser(any(UserAuthDTO.class))).thenReturn(userDTO);

        // Act
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserAuthDTO)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void registerUser_EmailAlreadyInUse() throws Exception {
        // Arrange
        Mockito.when(userService.registerUser(any(UserAuthDTO.class))).thenThrow(EmailAlreadyInUseException.class);

        // Act
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserAuthDTO)))

                // Assert
                .andExpect(status().isConflict());
    }

    @Test
    void login_Success() throws Exception {
        // Arrange
        Mockito.when(userService.authenticate(any(UserAuthDTO.class))).thenReturn(userDTO);

        // Act
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserAuthDTO)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));

    }

    @Test
    void login_InvalidInput_Unauthorized() throws Exception {
        // Arrange
        Mockito.when(userService.authenticate(any(UserAuthDTO.class))).thenThrow(InvalidInputException.class);

        // Act
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserAuthDTO)))

                // Assert
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateName_Success() throws Exception {
        // Arrange
        UpdateNameDTO updateNameDTO = new UpdateNameDTO(1L, "Updated", "Name");
        UserDTO updatedUserDTO = new UserDTO(1L, "test@example.com", "Updated", "Name");

        Mockito.when(userService.updateName(any(UpdateNameDTO.class))).thenReturn(updatedUserDTO);

        // Act
        mockMvc.perform(patch("/update-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNameDTO)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath(".lastName").value("Name"));
    }

    @Test
    void updateName_InvalidInput_BadRequest() throws Exception {
        // Arrange
        UpdateNameDTO updateNameDTO = new UpdateNameDTO(1L, "", "");
        Mockito.when(userService.updateName(any(UpdateNameDTO.class))).thenThrow(InvalidInputException.class);

        // Act
        mockMvc.perform(patch("/update-name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateNameDTO)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePassword_Success() throws Exception {
        // Arrange
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(1L, "oldPassword", "newPassword");

        // Act
        mockMvc.perform(patch("/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDTO)))

                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void updatePassword_InvalidInput_BadRequest() throws Exception {
        // Arrange
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO(1L, "", "newPassword");

        Mockito.doThrow(InvalidInputException.class).when(userService).updatePassword(any(UpdatePasswordDTO.class));

        // Act
        mockMvc.perform(patch("/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePasswordDTO)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserById_Success() throws Exception {
        // Arrange
        Mockito.when(userService.getUserById(eq(1L))).thenReturn(userDTO);

        // Act
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    void getUserById_InvalidInput_BadRequest() throws Exception {
        // Arrange
        Mockito.when(userService.getUserById(eq(1L))).thenThrow(InvalidInputException.class);

        // Act
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }
}
