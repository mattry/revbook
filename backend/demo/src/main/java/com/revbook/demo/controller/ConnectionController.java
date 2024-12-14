package com.revbook.demo.controller;

import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import com.revbook.demo.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/follow")
    public ResponseEntity<Connection> followUser(@RequestParam Long followerId, @RequestParam Long followeeId) {
        try{
            Connection newConnection = connectionService.followUser(followerId, followeeId);
            return ResponseEntity.ok(newConnection);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestParam Long followerId, @RequestParam Long followeeId) {
        try{
            connectionService.unfollowUser(followerId, followeeId);
            return ResponseEntity.ok(1);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<Set<User>> getFollowers(@PathVariable Long userId) {
        try{
            Set<User> followers = connectionService.getFollowersByUserId(userId);
            return ResponseEntity.ok(followers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<Set<User>> getFollowing(@PathVariable Long userId) {
        try{
            Set<User> following = connectionService.getFollowingByUserId(userId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}
