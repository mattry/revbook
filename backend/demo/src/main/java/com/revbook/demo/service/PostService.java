package com.revbook.demo.service;

import com.revbook.demo.dto.CommentDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.entity.Comment;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.Reaction;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.EmailAlreadyInUseException;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.ReactionRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactionRepository reactionRepository;

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

    // method to get all user posts, used to display posts on user pages
    public List<Post> getUserPosts(Long userId) {

        // Check that the userId belongs to an actual user
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User poster = userOptional.get();
            Optional<List<Post>> userPostsOptional = postRepository.findByPoster(poster);
            List<Post> userPosts = new ArrayList<Post>();
            userPostsOptional.ifPresent(userPosts::addAll);

            // reverse the posts by timePosted so they display in a reverse chronological order
            // most recent -> least recent
            userPosts.sort(Comparator.comparing(Post::getTimePosted).reversed());
            return userPosts;
        }

        throw new RuntimeException("User not found");
    }

    public void deleteMessageById(Long postId) {

        // verify that the post exists before trying to delete it
        Optional<Post> postOptional = postRepository.findById(postId);
        postOptional.ifPresent(post -> postRepository.delete(post));

        throw new RuntimeException("Message not found");

    }

    public ReactionDTO reactToPost(Long postId, ReactionDTO requestReactionDTO) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById((requestReactionDTO.getReacterId()));

        if (postOptional.isPresent() && userOptional.isPresent()){
            Reaction reaction = new Reaction();
            reaction.setPost(postOptional.get());
            reaction.setComment(null);
            reaction.setReacter(userOptional.get());
            reaction.setReactionType(Reaction.ReactionType.valueOf(requestReactionDTO.getReactionType()));

            Reaction saved = reactionRepository.save(reaction);

            return mapToReactionDTO(saved);
        }

        throw new RuntimeException("Error reacting to post");
    }

    public Set<ReactionDTO> getPostReactions(Long postId){
        Optional<Post> postOptional = postRepository.findById(postId);
        Set<ReactionDTO> reactionDTOS = new HashSet<>();

        if (postOptional.isPresent()){
            Post post = postOptional.get();
            Optional<List<Reaction>> reactionsOptional = reactionRepository.findByPost(post);

            if (reactionsOptional.isPresent()){
                List<Reaction> reactions = reactionsOptional.get();
                for (Reaction reaction : reactions){
                    reactionDTOS.add(mapToReactionDTO(reaction));
                }
            }

            return reactionDTOS;
        }

        throw new RuntimeException("Error getting reactions");
    }

    // since this mapper is defined in the Post Service we will always set the comment to null
    public ReactionDTO mapToReactionDTO(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setReactionType(reaction.getReactionType().name());
        reactionDTO.setReacterId(reaction.getReacter().getUserId());
        reactionDTO.setCommentId(null);
        reactionDTO.setPostId(reaction.getPost().getPostId());
        reactionDTO.setFirstName(reaction.getReacter().getFirstName());
        reactionDTO.setLastName(reaction.getReacter().getLastName());

        return reactionDTO;
    }





}
