package com.revbook.demo.controller;

import com.revbook.demo.dto.ConnectionDTO;
import com.revbook.demo.dto.UserDTO;
import com.revbook.demo.entity.Connection;
import com.revbook.demo.entity.User;
import com.revbook.demo.service.ConnectionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/users/{userId}/follow")
    public ResponseEntity<ConnectionDTO> followUser(@RequestBody ConnectionDTO requestConnectionDTO, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            ConnectionDTO newConnectionDTO = connectionService.followUser(requestConnectionDTO);
            return ResponseEntity.ok(newConnectionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/users/{userId}/unfollow")
    public ResponseEntity<?> unfollowUser(@RequestBody ConnectionDTO removeConnectionDTO, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            connectionService.unfollowUser(removeConnectionDTO);
            return ResponseEntity.ok(1);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/users/{userId}/followers")
    public ResponseEntity<Set<UserDTO>> getFollowers(@PathVariable Long userId, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            Set<UserDTO> followers = connectionService.getFollowersByUserId(userId);
            return ResponseEntity.ok(followers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/users/{userId}/following")
    public ResponseEntity<Set<UserDTO>> getFollowing(@PathVariable Long userId, HttpSession session) {
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try{
            Set<UserDTO> following = connectionService.getFollowingByUserId(userId);
            return ResponseEntity.ok(following);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/users/{followerId}/isFollowing/{followeeId}")
    public ResponseEntity<Boolean> isUserFollowering(@PathVariable Long followerId, @PathVariable Long followeeId, HttpSession session){
        Long sessionUserId = (Long) session.getAttribute("userId");
        if (sessionUserId == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            boolean isFollowing = connectionService.isUserFollowing(followerId, followeeId);
            return ResponseEntity.ok(isFollowing);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        }
    }

}
