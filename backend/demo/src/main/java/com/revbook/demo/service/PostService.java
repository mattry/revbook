package com.revbook.demo.service;

import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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


    // this method is used to get the user's feed
    // user feed consists of a user's own posts and posts from users they follow.
    public List<Post> getUserFeed(Long userId){

        // Check that the userId belongs to an actual user
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User poster = userOptional.get();
            Optional<List<Post>> userPostsOptional = postRepository.findByPoster(poster);
            Optional<List<Post>> connectionPostsOptional = postRepository.findByFollowing(poster);
            List<Post> userFeed = new ArrayList<Post>();
            userPostsOptional.ifPresent(userFeed::addAll);
            connectionPostsOptional.ifPresent(userFeed::addAll);

            // reverse the posts by timePosted so they display in a reverse chronological order
            // most recent -> least recent
            userFeed.sort(Comparator.comparing(Post::getTimePosted).reversed());
            return userFeed;
        }

        throw new RuntimeException("User not found");
    }
}
