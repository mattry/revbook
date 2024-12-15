package com.revbook.demo.service;

import com.revbook.demo.dto.CommentDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.entity.Comment;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.Reaction;
import com.revbook.demo.entity.User;
import com.revbook.demo.repository.CommentRepository;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.ReactionRepository;
import com.revbook.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    public CommentDTO commentOnPost(Long postId, CommentDTO requestCommentDTO) {
        System.out.println("Function called");
        Optional<Post> postOptional = postRepository.findById(postId);
        Optional<User> userOptional = userRepository.findById(requestCommentDTO.getPosterId());

        if (postOptional.isPresent() && userOptional.isPresent()){

            System.out.println("Commenting on post with ID: " + postId);
            System.out.println("User ID: " + requestCommentDTO.getPosterId());
            System.out.println("Comment to be added: " + requestCommentDTO.getCommentText());

            Comment comment = new Comment();
            comment.setPost(postOptional.get());
            comment.setPoster(userOptional.get());
            comment.setCommentText(requestCommentDTO.getCommentText());
            comment.setParentComment(null);

            Comment saved = commentRepository.save(comment);
            System.out.println("Saved comment via commentRepository");
            System.out.println("Mapping saved comment to a DTO");
            CommentDTO response = mapToDTO(saved);

            return response;
        }

        // this shouldn't happen on requests coming from the frontend
        throw new RuntimeException("Post or User not found");

    }

    public CommentDTO commentOnComment(Long commentId, CommentDTO requestCommentDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById(requestCommentDTO.getPosterId());

        if (commentOptional.isPresent() && userOptional.isPresent()){

            System.out.println("Commenting on comment with ID: " + commentId);
            System.out.println("User ID: " + requestCommentDTO.getPosterId());
            System.out.println("Comment to be added: " + requestCommentDTO.getCommentText());

            Comment comment = new Comment();
            comment.setPost(null);
            comment.setPoster(userOptional.get());
            comment.setCommentText(requestCommentDTO.getCommentText());
            comment.setParentComment(commentOptional.get());

            System.out.println("Saved comment via commentRepository");
            Comment saved = commentRepository.save(comment);
            System.out.println("Saved comment via commentRepository");
            System.out.println("Mapping saved comment to a DTO");
            CommentDTO response = mapToDTO(saved);

            return response;
        }

        throw new RuntimeException("Comment or User not found");
    }

    public List<CommentDTO> getCommentDTOsByPostId(Long postId) {
        Optional<List<Comment>> commentsOptional = commentRepository.findByPost_PostId(postId);
        if (commentsOptional.isPresent()) {
            List<Comment> comments = commentsOptional.get();
            List<CommentDTO> responseDTOs = new ArrayList<>();
            for(Comment comment : comments) {
                responseDTOs.add(mapToDTO(comment));
            }

            return responseDTOs;
        }

        throw new RuntimeException("No comments found using that postId");
    }

    public Set<CommentDTO> getChildCommentDTOs(Long commentId) {
        Set<CommentDTO> childCommentDTOs = new HashSet<>();
        Optional<Comment> parentCommentOptional = commentRepository.findById(commentId);
        if(parentCommentOptional.isPresent()) {
            System.out.println("Parent exists..");
            Comment parentComment = parentCommentOptional.get();
            System.out.println("Getting childComments from parent..");
            Optional<List<Comment>> childCommentsOptional = commentRepository.findByParentComment(parentComment);

            if(childCommentsOptional.isPresent()){
                System.out.println("Child comments exist..");
                List<Comment> childComments = childCommentsOptional.get();
                System.out.println("No. of child comments: " + childComments.size());

                for (Comment childComment : childComments) {
                    System.out.println("Looping!");
                    System.out.println("Child comment: " + childComment.getCommentText());
                    childCommentDTOs.add(mapToDTO(childComment));
                }
            }

            return childCommentDTOs;
        }

        throw new RuntimeException("Parent comment not found");

    }

    public ReactionDTO reactToComment(Long commentId, ReactionDTO requestReactionDTO) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<User> userOptional = userRepository.findById((requestReactionDTO.getReacterId()));

        if (commentOptional.isPresent() && userOptional.isPresent()){
            Reaction reaction = new Reaction();
            reaction.setPost(null);
            reaction.setComment(commentOptional.get());
            reaction.setReacter(userOptional.get());
            reaction.setReactionType(Reaction.ReactionType.valueOf(requestReactionDTO.getReactionType()));

            Reaction saved = reactionRepository.save(reaction);

            return mapToReactionDTO(saved);
        }

        throw new RuntimeException("Error reacting to post");
    }

    public Set<ReactionDTO> getCommentReactions(Long commentId){
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Set<ReactionDTO> reactionDTOS = new HashSet<>();

        if (commentOptional.isPresent()){
            Comment comment = commentOptional.get();
            Optional<List<Reaction>> reactionsOptional = reactionRepository.findByComment(comment);

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


    public CommentDTO mapToDTO (Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setCommentText(comment.getCommentText());
        dto.setPosterId(comment.getPoster().getUserId());
        dto.setTimePosted(comment.getTimePosted());
        dto.setFirstName(comment.getPoster().getFirstName());
        dto.setLastName(comment.getPoster().getLastName());

        // handle possible null pointers in parent post/comment fields
        if (comment.getPost() != null) {
            dto.setPostId(comment.getPost().getPostId());
        } else {
            dto.setPostId(null);
        }

        if (comment.getParentComment() != null) {
            dto.setParentCommentId(comment.getParentComment().getCommentId());
        } else {
            dto.setPostId(null);
        }

        return dto;
    }

    // since this mapper is defined in the Comment Service we will always set the post to null
    public ReactionDTO mapToReactionDTO(Reaction reaction) {
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setReactionType(reaction.getReactionType().name());
        reactionDTO.setReacterId(reaction.getReacter().getUserId());
        reactionDTO.setCommentId(reaction.getComment().getCommentId());
        reactionDTO.setPostId(null);
        reactionDTO.setFirstName(reaction.getReacter().getFirstName());
        reactionDTO.setLastName(reaction.getReacter().getLastName());

        return reactionDTO;
    }



}
