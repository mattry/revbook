package com.revbook.demo.service;

import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public Post createPost(Post post) {

        // Check that the user attempting to make the post exists
        Optional<User> userOptional = userRepository.findByEmail(post.getPoster().getEmail());
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found");
        }

        // Check that post text is valid
        // we will limit their length to 280 characters
        if(post.getPostText() == null || post.getPostText().isBlank() || post.getPostText().length() > 280) {
            throw new InvalidInputException("Invalid message text");
        }

        return postRepository.save(post);
    }
}
