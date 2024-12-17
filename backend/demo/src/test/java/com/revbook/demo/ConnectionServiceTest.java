package com.revbook.demo;

import com.revbook.demo.dto.ConnectionDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import com.revbook.demo.repository.ConnectionRepository;
import com.revbook.demo.repository.UserRepository;
import com.revbook.demo.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConnectionServiceTest {

    @Mock
    private ConnectionRepository connectionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ConnectionService connectionService;

    private User follower;
    private User followee;
    private Connection connection;

    @BeforeEach
    void setUp() {
        // Mock follower
        follower = new User();
        follower.setUserId(1L);
        follower.setFirstName("John");
        follower.setLastName("Doe");

        // Mock followee
        followee = new User();
        followee.setUserId(2L);
        followee.setFirstName("Jane");
        followee.setLastName("Smith");

        // Mock connection
        connection = new Connection();
        connection.setConnectionId(1L);
        connection.setFollower(follower);
        connection.setFollowee(followee);
    }

    @Test
    void followUser_success() {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFollowerId(follower.getUserId());
        connectionDTO.setFolloweeId(followee.getUserId());

        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.of(follower));
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.of(followee));
        when(connectionRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(false);
        when(connectionRepository.save(any(Connection.class))).thenReturn(connection);

        // Act
        ConnectionDTO result = connectionService.followUser(connectionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(connection.getConnectionId(), result.getConnectionId());
        verify(connectionRepository, times(1)).save(any(Connection.class));
    }

    @Test
    void followUser_connectionAlreadyExists() {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFollowerId(follower.getUserId());
        connectionDTO.setFolloweeId(followee.getUserId());

        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.of(follower));
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.of(followee));
        when(connectionRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> connectionService.followUser(connectionDTO));
        verify(connectionRepository, never()).save(any(Connection.class)); // Ensure no save happens
    }

    @Test
    void followUser_userNotFound() {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFollowerId(follower.getUserId());
        connectionDTO.setFolloweeId(followee.getUserId());

        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> connectionService.followUser(connectionDTO));
        verify(connectionRepository, never()).save(any(Connection.class));
    }

    @Test
    void unfollowUser_success() {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFollowerId(follower.getUserId());
        connectionDTO.setFolloweeId(followee.getUserId());

        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.of(follower));
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.of(followee));
        when(connectionRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(true);
        when(connectionRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(connection);

        // Act
        connectionService.unfollowUser(connectionDTO);

        // Assert
        verify(connectionRepository, times(1)).delete(connection);
    }

    @Test
    void unfollowUser_connectionNotFound() {
        // Arrange
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setFollowerId(follower.getUserId());
        connectionDTO.setFolloweeId(followee.getUserId());

        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.of(follower));
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.of(followee));
        when(connectionRepository.existsByFollowerAndFollowee(follower, followee)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> connectionService.unfollowUser(connectionDTO));
        verify(connectionRepository, never()).delete(any(Connection.class));
    }

    @Test
    void getFollowersByUserId_success() {
        // Arrange
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.of(followee));
        when(connectionRepository.findByFollowee(followee)).thenReturn(Collections.singletonList(connection));

        // Act
        Set<UserDTO> result = connectionService.getFollowersByUserId(followee.getUserId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getUserId().equals(follower.getUserId())));
    }

    @Test
    void getFollowersByUserId_userNotFound() {
        // Arrange
        when(userRepository.findById(followee.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> connectionService.getFollowersByUserId(followee.getUserId()));
        verify(connectionRepository, never()).findByFollowee(any(User.class));
    }

    @Test
    void getFollowingByUserId_success() {
        // Arrange
        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.of(follower));
        when(connectionRepository.findByFollower(follower)).thenReturn(Collections.singletonList(connection));

        // Act
        Set<UserDTO> result = connectionService.getFollowingByUserId(follower.getUserId());

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.getUserId().equals(followee.getUserId())));
    }

    @Test
    void getFollowingByUserId_userNotFound() {
        // Arrange
        when(userRepository.findById(follower.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> connectionService.getFollowingByUserId(follower.getUserId()));
        verify(connectionRepository, never()).findByFollower(any(User.class));
    }
}