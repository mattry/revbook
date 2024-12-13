package com.revbook.demo.controller;

import com.revbook.demo.service.PostService;
import com.revbook.demo.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost/3000")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Post> createPost(@RequestBody Post post) {
        try{
            Post createdPost = postService.createPost(post);
            return ResponseEntity.ok(createdPost);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}
