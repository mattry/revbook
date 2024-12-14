package com.revbook.demo.service;

import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.ConnectionRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ConnectionService {

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public Connection followUser(Long followerId, Long followeeId) {
        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followeeOptional = userRepository.findById(followeeId);

        if (followerOptional.isPresent() && followeeOptional.isPresent()) {
            User follower = followerOptional.get();
            User followee = followeeOptional.get();

            if (connectionRepository.existsByFollowerAndFollowee(follower, followee)) {
                throw new RuntimeException("Connection Already Exists");
            }
            Connection connection = new Connection();
            connection.setFollower(follower);
            connection.setFollowee(followee);
            return connectionRepository.save(connection);
        }
        throw new RuntimeException("Connection failed");
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        Optional<User> followerOptional = userRepository.findById(followerId);
        Optional<User> followeeOptional = userRepository.findById(followeeId);

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

    public Set<User> getFollowersByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            Optional<Set<Connection>> followersOptional = connectionRepository.findByFollowee(userOptional.get());
            if (followersOptional.isPresent()) {
                Set<User> followers = new HashSet<>();
                for (Connection connection : followersOptional.get()) {
                    followers.add(connection.getFollower());
                }
                return followers;
            }
            throw new RuntimeException("No followers found");
        }
        throw new RuntimeException("User Not Found");
    }

    public Set<User> getFollowingByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            Optional<Set<Connection>> followingOptional = connectionRepository.findByFollower(userOptional.get());
            if (followingOptional.isPresent()) {
                Set<User> following = new HashSet<>();
                for (Connection connection : followingOptional.get()) {
                    following.add(connection.getFollowee());
                }
                return following;
            }
            throw new RuntimeException("No followees found");
        }
        throw new RuntimeException("User Not Found");
    }


}
