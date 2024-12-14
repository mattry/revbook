package com.revbook.demo.controller;

import com.revbook.demo.entity.Comment;
import com.revbook.demo.service.CommentService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Comment> commentOnPost(@PathVariable Long postId, @RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.commentOnPost(postId, comment);
            return ResponseEntity.ok(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<Comment> commentOnComment(@PathVariable Long commentId, @RequestBody Comment comment) {
        try {
            Comment createdComment = commentService.commentOnComment(commentId, comment);
            return ResponseEntity.ok(createdComment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPost(@PathVariable Long postId) {
        try {
            List<Comment> comments = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(comments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<Set<Comment>> getChildComments(@PathVariable Long commentId) {
        try {
            Set<Comment> childComments = commentService.getChildComments(commentId);
            return ResponseEntity.ok(childComments);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}
