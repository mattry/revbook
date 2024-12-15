package com.revbook.demo.controller;

import com.revbook.demo.dto.CommentDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.entity.Comment;
import com.revbook.demo.service.CommentService;
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
    public ResponseEntity<CommentDTO> commentOnPost(@PathVariable Long postId, @RequestBody CommentDTO requestCommentDTO) {
        try {
            CommentDTO createdCommentDTO = commentService.commentOnPost(postId, requestCommentDTO);
            System.out.println("DTO created" + createdCommentDTO);
            return ResponseEntity.ok(createdCommentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/comments/{commentId}/reply")
    public ResponseEntity<CommentDTO> commentOnComment(@PathVariable Long commentId, @RequestBody CommentDTO requestCommentDTO) {
        try {
            CommentDTO createdCommentDTO = commentService.commentOnComment(commentId, requestCommentDTO);
            return ResponseEntity.ok(createdCommentDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPost(@PathVariable Long postId) {
        try {
            List<CommentDTO> commentDTOs = commentService.getCommentDTOsByPostId(postId);
            return ResponseEntity.ok(commentDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/comments/{commentId}/replies")
    public ResponseEntity<Set<CommentDTO>> getChildComments(@PathVariable Long commentId) {
        try {
            Set<CommentDTO> childCommentDTOs = commentService.getChildCommentDTOs(commentId);
            return ResponseEntity.ok(childCommentDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("comments/{commentId}/reactions")
    public ResponseEntity<ReactionDTO> reactToPost (@PathVariable Long commentId, @RequestBody ReactionDTO requestReactionDTO) {
        try {
            ReactionDTO madeReactionDTO = commentService.reactToComment(commentId, requestReactionDTO);
            return ResponseEntity.ok(madeReactionDTO);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/comments/{commentId}/reactions")
    public ResponseEntity<Set<ReactionDTO>> getPostReactions (@PathVariable Long commentId) {
        try {
            Set<ReactionDTO> reactionDTOS = commentService.getCommentReactions(commentId);
            return ResponseEntity.ok(reactionDTOS);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



}
