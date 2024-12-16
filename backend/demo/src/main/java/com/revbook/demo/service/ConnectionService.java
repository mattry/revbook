package com.revbook.demo.service;

import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import com.revbook.demo.dto.ConnectionDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.ConnectionRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public ConnectionDTO followUser(ConnectionDTO requestConnectionDTO) {
        Optional<User> followerOptional = userRepository.findById(requestConnectionDTO.getFollowerId());
        Optional<User> followeeOptional = userRepository.findById(requestConnectionDTO.getFolloweeId());

        if (followerOptional.isPresent() && followeeOptional.isPresent()) {
            User follower = followerOptional.get();
            User followee = followeeOptional.get();

            if (connectionRepository.existsByFollowerAndFollowee(follower, followee)) {
                throw new RuntimeException("Connection Already Exists");
            }
            Connection connection = new Connection();
            connection.setFollower(follower);
            connection.setFollowee(followee);
            Connection saved = connectionRepository.save(connection);
            return mapToConnectionDTO(saved);
        }
        throw new RuntimeException("Connection failed");
    }

    public void unfollowUser(ConnectionDTO removeConnectionDTO) {
        Optional<User> followerOptional = userRepository.findById(removeConnectionDTO.getFollowerId());
        Optional<User> followeeOptional = userRepository.findById(removeConnectionDTO.getFolloweeId());

        if (followerOptional.isPresent() && followeeOptional.isPresent()) {
            User follower = followerOptional.get();
            User followee = followeeOptional.get();

            if (connectionRepository.existsByFollowerAndFollowee(follower, followee)) {
                Connection connection = connectionRepository.findByFollowerAndFollowee(follower, followee);
                connectionRepository.delete(connection);
            }
            throw new RuntimeException("Connection not found");
        }
        throw new RuntimeException();
    }

    public Set<UserDTO> getFollowersByUserId(Long userId) {
        System.out.println("Service function called..");
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("User exists..");
            // return every connection where this user is being followed
            List<Connection> followers = connectionRepository.findByFollowee(user);
            Set<UserDTO> followerDTOs = new HashSet<>();
            for (Connection connection : followers) {
                System.out.println("Looping!");
                followerDTOs.add(mapToUserDTO(connection.getFollower()));
            }

            return followerDTOs;
        }

        throw new RuntimeException("User Not Found");
    }

    public Set<UserDTO> getFollowingByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            // return every connection where this user is the follower
           List<Connection> following = connectionRepository.findByFollower(userOptional.get());
           Set<UserDTO> followingDTOs = new HashSet<>();
           for (Connection connection : following) {
               followingDTOs.add(mapToUserDTO(connection.getFollowee()));
           }

           return followingDTOs;
        }

        throw new RuntimeException("User Not Found");
    }

    public ConnectionDTO mapToConnectionDTO(Connection connection) {
        ConnectionDTO connectionDTO = new ConnectionDTO();
        connectionDTO.setConnectionId(connection.getConnectionId());
        connectionDTO.setFollowerId(connection.getFollower().getUserId());
        connectionDTO.setFolloweeId(connection.getFollowee().getUserId());

        return connectionDTO;
    }

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserId(user.getUserId());

        return userDTO;
    }


}
