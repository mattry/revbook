package com.revbook.demo;

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
import com.revbook.demo.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private CommentService commentService;

    private Post post;
    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        // Mock post
        post = new Post();
        post.setPostId(1L);

        // Mock user
        user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        // Mock comment
        comment = new Comment();
        comment.setCommentId(1L);
        comment.setPost(post);
        comment.setPoster(user);
        comment.setCommentText("Sample comment");
    }

    @Test
    void commentOnPost_success() {
        // Arrange
        CommentDTO request = new CommentDTO();
        request.setPosterId(user.getUserId());
        request.setCommentText("This is a comment");

        when(postRepository.findById(post.getPostId())).thenReturn(Optional.of(post));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            saved.setCommentId(10L);
            return saved;
        });

        // Act
        CommentDTO result = commentService.commentOnPost(post.getPostId(), request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getCommentId());
        assertEquals(request.getCommentText(), result.getCommentText());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void commentOnPost_postNotFound() {
        // Arrange
        CommentDTO request = new CommentDTO();
        request.setPosterId(user.getUserId());
        request.setCommentText("This is a comment");

        when(postRepository.findById(post.getPostId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.commentOnPost(post.getPostId(), request));
        assertEquals("Post or User not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void commentOnComment_success() {
        // Arrange
        Long parentCommentId = 1L;
        Comment parentComment = new Comment();
        parentComment.setCommentId(parentCommentId);

        CommentDTO request = new CommentDTO();
        request.setPosterId(user.getUserId());
        request.setCommentText("This is a reply");

        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment saved = invocation.getArgument(0);
            saved.setCommentId(10L);
            return saved;
        });

        // Act
        CommentDTO result = commentService.commentOnComment(parentCommentId, request);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getCommentId());
        assertEquals("This is a reply", result.getCommentText());
        assertEquals(parentCommentId, result.getParentCommentId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void commentOnComment_commentNotFound() {
        // Arrange
        Long parentCommentId = 1L;
        CommentDTO request = new CommentDTO();
        request.setPosterId(user.getUserId());
        request.setCommentText("This is a reply");

        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.commentOnComment(parentCommentId, request));
        assertEquals("Comment or User not found", exception.getMessage());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void getCommentDTOsByPostId_success() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        Comment comment1 = new Comment();
        comment1.setCommentId(1L);
        comment1.setCommentText("Comment 1");
        comment1.setPoster(user);

        Comment comment2 = new Comment();
        comment2.setCommentId(2L);
        comment2.setCommentText("Comment 2");
        comment2.setPoster(user);

        when(commentRepository.findByPost_PostId(post.getPostId())).thenReturn(Optional.of(Arrays.asList(comment1, comment2)));

        // Act
        List<CommentDTO> result = commentService.getCommentDTOsByPostId(post.getPostId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findByPost_PostId(post.getPostId());
    }

    @Test
    void getCommentDTOsByPostId_noComments() {
        // Arrange
        when(commentRepository.findByPost_PostId(post.getPostId())).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.getCommentDTOsByPostId(post.getPostId()));
        assertEquals("No comments found using that postId", exception.getMessage());
        verify(commentRepository, times(1)).findByPost_PostId(post.getPostId());
    }

    @Test
    void reactToComment_success() {
        // Arrange
        Long commentId = comment.getCommentId();
        ReactionDTO request = new ReactionDTO();
        request.setReacterId(user.getUserId());
        request.setReactionType("LIKE");

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(reactionRepository.save(any(Reaction.class))).thenAnswer(invocation -> {
            Reaction reaction = invocation.getArgument(0);
            reaction.setReactionType(Reaction.ReactionType.LIKE);
            reaction.setReacter(user);
            return reaction;
        });

        // Act
        ReactionDTO result = commentService.reactToComment(commentId, request);

        // Assert
        assertNotNull(result);
        assertEquals(commentId, result.getCommentId());
        assertEquals(user.getUserId(), result.getReacterId());
        assertEquals("LIKE", result.getReactionType());
        verify(reactionRepository, times(1)).save(any(Reaction.class));
    }

    @Test
    void reactToComment_commentNotFound() {
        // Arrange
        Long commentId = comment.getCommentId();
        ReactionDTO request = new ReactionDTO();
        request.setReacterId(user.getUserId());
        request.setReactionType("LIKE");

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> commentService.reactToComment(commentId, request));
        assertEquals("Error reacting to post", exception.getMessage());
        verify(reactionRepository, never()).save(any(Reaction.class));
    }
}
