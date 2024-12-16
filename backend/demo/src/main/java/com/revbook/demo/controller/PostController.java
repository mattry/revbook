package com.revbook.demo.controller;

import com.revbook.demo.dto.PostDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.entity.User;
import com.revbook.demo.service.PostService;
import com.revbook.demo.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/*
    This controller will handle creating posts, serving posts, and deleting posts
*/

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO requestPostDTO) {
        try{
            PostDTO createdPostDTO = postService.createPostDTO(requestPostDTO);
            return ResponseEntity.ok(createdPostDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/feed/{userId}")
    public ResponseEntity<List<PostDTO>> getFeed(@PathVariable Long userId) {
        try{
            List<PostDTO> feed = postService.getUserFeed(userId);
            return ResponseEntity.ok(feed);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/posts/{userId}")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable Long userId) {
        try {
            List<PostDTO> userPostDTOs = postService.getUserPostDTOs(userId);
            return ResponseEntity.ok(userPostDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable Long postId){
        try {
            postService.deleteMessageById(postId);
            return ResponseEntity.ok(1);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("posts/{postId}/reactions")
    public ResponseEntity<ReactionDTO> reactToPost (@PathVariable Long postId, @RequestBody ReactionDTO requestReactionDTO) {
        try {
            ReactionDTO madeReactionDTO = postService.reactToPost(postId, requestReactionDTO);
            return ResponseEntity.ok(madeReactionDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/posts/{postId}/reactions")
    public ResponseEntity<Set<ReactionDTO>> getPostReactions (@PathVariable Long postId) {
        try {
            Set<ReactionDTO> reactionDTOS = postService.getPostReactions(postId);
            return ResponseEntity.ok(reactionDTOS);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


}
