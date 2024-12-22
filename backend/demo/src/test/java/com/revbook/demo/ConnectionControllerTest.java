package com.revbook.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revbook.demo.controller.ConnectionController;
import com.revbook.demo.dto.ConnectionDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.service.ConnectionService;
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

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ConnectionControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private ConnectionController connectionController;

    @Mock
    private ConnectionService connectionService;

    private ObjectMapper objectMapper;

    // Test data
    private ConnectionDTO connectionDTORequest;
    private ConnectionDTO connectionDTOResponse;
    private UserDTO userDTO;
    private Set<UserDTO> followers;
    private Set<UserDTO> following;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(connectionController).build();
        objectMapper = new ObjectMapper();

        // Initialize test data
        connectionDTORequest = new ConnectionDTO(
                null,       // connectionId
                100L,       // followerId
                101L        // followeeId
        );

        connectionDTOResponse = new ConnectionDTO(
                1L,         // connectionId
                100L,       // followerId
                101L        // followeeId
        );

        userDTO = new UserDTO(
                100L,         // userId
                "jdoe@mail.com",    // email
                "John",             // firstName
                "Doe"               // lastName
        );

        followers = new HashSet<>();
        followers.add(userDTO);

        following = new HashSet<>();
        following.add(new UserDTO(101L, "jdoe@mail.com", "Jane", "Doe"));
    }

    @Test
    void followUser_Success() throws Exception {
        // Arrange
        Mockito.when(connectionService.followUser(any(ConnectionDTO.class)))
                .thenReturn(connectionDTOResponse);

        // Act
        mockMvc.perform(post("/users/100/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectionDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.connectionId").value(1))
                .andExpect(jsonPath("$.followerId").value(100))
                .andExpect(jsonPath("$.followeeId").value(101));
    }

    @Test
    void followUser_BadRequest() throws Exception {
        // Arrange
        Mockito.when(connectionService.followUser(any(ConnectionDTO.class)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(post("/users/100/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectionDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void unfollowUser_Success() throws Exception {
        // Act
        mockMvc.perform(delete("/users/100/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectionDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    void unfollowUser_BadRequest() throws Exception {
        // Arrange
        Mockito.doThrow(RuntimeException.class)
                .when(connectionService).unfollowUser(any(ConnectionDTO.class));

        // Act
        mockMvc.perform(delete("/users/100/unfollow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(connectionDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFollowers_Success() throws Exception {
        // Arrange
        Mockito.when(connectionService.getFollowersByUserId(eq(100L)))
                .thenReturn(followers);

        // Act
        mockMvc.perform(get("/users/100/followers")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(100))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getFollowers_BadRequest() throws Exception {
        // Arrange
        Mockito.when(connectionService.getFollowersByUserId(eq(100L)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/users/100/followers")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFollowing_Success() throws Exception {
        // Arrange
        Mockito.when(connectionService.getFollowingByUserId(eq(100L)))
                .thenReturn(following);

        // Act
        mockMvc.perform(get("/users/100/following")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(101))
                .andExpect(jsonPath("$[0].firstName").value("Jane"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getFollowing_BadRequest() throws Exception {
        // Arrange
        Mockito.when(connectionService.getFollowingByUserId(eq(100L)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/users/100/following")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void isUserFollowing_Success() throws Exception {
        // Arrange
        Mockito.when(connectionService.isUserFollowing(eq(100L), eq(101L)))
                .thenReturn(true);

        // Act
        mockMvc.perform(get("/users/100/isFollowing/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void isUserFollowing_BadRequest() throws Exception {
        // Arrange
        Mockito.when(connectionService.isUserFollowing(eq(100L), eq(101L)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/users/100/isFollowing/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }
}