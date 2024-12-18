package com.revbook.demo.service;

import com.revbook.demo.dto.CommentDTO;
import com.revbook.demo.dto.PostDTO;
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

    public PostDTO createPostDTO(PostDTO requestPostDTO) {

        // Check that the user attempting to make the post exists
        Optional<User> userOptional = userRepository.findById(requestPostDTO.getPosterId());
        if(userOptional.isEmpty()){
            throw new RuntimeException("User not found");
        }

        // Check that post text is valid
        // we will limit their length to 280 characters
        if(requestPostDTO.getPostText() == null || requestPostDTO.getPostText().isBlank() || requestPostDTO.getPostText().length() > 280) {
            throw new InvalidInputException("Invalid message text");
        }

        Post post = new Post();
        post.setPoster(userOptional.get());
        post.setPostText(requestPostDTO.getPostText());
        Post saved = postRepository.save(post);

        return mapToPostDTO(saved);
    }


    // this method is used to get the user's feed
    // user feed consists of a user's own posts and posts from users they follow.
    public List<PostDTO> getUserFeed(Long userId){

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

            List<PostDTO> userFeedDTOs = new ArrayList<>();

            for (Post post: userFeed){
                userFeedDTOs.add(mapToPostDTO(post));
            }
            return userFeedDTOs;
        }
        throw new RuntimeException("User not found");
    }

    // method to get all user posts, used to display posts on user pages
    public List<PostDTO> getUserPostDTOs(Long userId) {

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

            List<PostDTO> userPostDTOS = new ArrayList<>();

            for (Post post: userPosts){
                userPostDTOS.add(mapToPostDTO(post));
            }

            return userPostDTOS;

        }

        throw new RuntimeException("User not found");
    }

    public void deletePostById(Long postId) {

        // verify that the post exists before trying to delete it
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()){
            postRepository.delete(postOptional.get());
        } else {
            throw new RuntimeException("Post not found");
        }
    }

    public ReactionDTO reactToPost(Long postId, ReactionDTO requestReactionDTO) {
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById((requestReactionDTO.getReacterId()));

        if (postOptional.isPresent() && userOptional.isPresent()){
            Post post = postOptional.get();
            User user = userOptional.get();

            // check if user has reacted to post already
            Optional<Reaction> existingReaction = reactionRepository.findByReacterAndPost(user, post);

            if (existingReaction.isPresent()) {
                Reaction reaction = existingReaction.get();

                // if the user has already reacted to this post with the same reaction type, we will undo their reaction
                if (reaction.getReactionType().name().equals(requestReactionDTO.getReactionType())) {
                    reactionRepository.delete(reaction);
                    return null;
                }

                // if the user has reacted with a different reaction type, change type
                reaction.setReactionType(Reaction.ReactionType.valueOf(requestReactionDTO.getReactionType()));
                Reaction updatedReaction = reactionRepository.save(reaction);

                return mapToReactionDTO(updatedReaction);
            }

            // no reaction currently exists, so we make a new one
            Reaction reaction = new Reaction();
            reaction.setComment(null);
            reaction.setPost(postOptional.get());
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

    public PostDTO mapToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setTimePosted(post.getTimePosted());
        postDTO.setPostText(post.getPostText());
        postDTO.setPosterId(post.getPoster().getUserId());
        postDTO.setLastName(post.getPoster().getLastName());
        postDTO.setFirstName(post.getPoster().getFirstName());
        postDTO.setPostId(post.getPostId());


        return postDTO;
    }




}
