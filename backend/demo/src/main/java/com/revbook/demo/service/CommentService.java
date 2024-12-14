package com.revbook.demo.service;

import com.revbook.demo.entity.Comment;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.User;
import com.revbook.demo.repository.CommentRepository;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment commentOnPost(Long postId, Comment requestComment) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(requestComment.getPoster().getUserId());

        if (postOptional.isPresent() && userOptional.isPresent()){
            Comment comment = new Comment();
            comment.setPost(postOptional.get());
            comment.setPoster(userOptional.get());
            comment.setCommentText(requestComment.getCommentText());
            comment.setParentComment(null);

            return commentRepository.save(comment);
        }

        // this shouldn't happen on requests coming from the frontend
        throw new RuntimeException("Post or User not found");

    }

    public Comment commentOnComment(Long commentId, Comment requestComment) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(requestComment.getPoster().getUserId());

        if (commentOptional.isPresent() && userOptional.isPresent()){
            Comment comment = new Comment();
            comment.setPost(null);
            comment.setPoster(userOptional.get());
            comment.setCommentText(requestComment.getCommentText());
            comment.setParentComment(commentOptional.get());

            return commentRepository.save(comment);
        }

        throw new RuntimeException("Comment or User not found");
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        Optional<List<Comment>> commentsOptional = commentRepository.findByPost_PostId(postId);
        if (commentsOptional.isPresent()) {
            return commentsOptional.get();
        }

        throw new RuntimeException("No comments found using that postId");
    }

    public Set<Comment> getChildComments(Long commentId) {
        Optional<Comment> parentCommentOptional = commentRepository.findById(commentId);
        if(parentCommentOptional.isPresent()) {
            return parentCommentOptional.get().getChildComments();
        }

        throw new RuntimeException("Parent comment not found");

    }




}
