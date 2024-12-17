package com.revbook.demo;

import com.revbook.demo.dto.PostDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.entity.Post;
import com.revbook.demo.entity.Reaction;
import com.revbook.demo.entity.User;
import com.revbook.demo.exception.InvalidInputException;
import com.revbook.demo.repository.PostRepository;
import com.revbook.demo.repository.ReactionRepository;
import com.revbook.demo.repository.UserRepository;
import com.revbook.demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReactionRepository reactionRepository;

    @InjectMocks
    private PostService postService;

    private User mockUser;
    private Post mockPost;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setUserId(1L);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        mockPost = new Post();
        mockPost.setPostId(1L);
        mockPost.setPoster(mockUser);
        mockPost.setPostText("Test post");
        mockPost.setTimePosted(LocalDateTime.now());
    }

    @Test
    void createPostDTO_success() {
        // Arrange
        PostDTO postDTO = new PostDTO();
        postDTO.setPosterId(mockUser.getUserId());
        postDTO.setPostText("This is a test post");

        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(postRepository.save(any(Post.class))).thenReturn(mockPost);

        // Act
        PostDTO result = postService.createPostDTO(postDTO);

        // Assert
        assertNotNull(result);
        assertEquals(mockPost.getPostText(), result.getPostText());
        verify(postRepository, times(1)).save(any(Post.class));
        verify(userRepository, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void createPostDTO_userNotFound() {
        // Arrange
        PostDTO postDTO = new PostDTO();
        postDTO.setPosterId(999L);
        postDTO.setPostText("This is a test post");

        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postService.createPostDTO(postDTO));
        verify(userRepository, times(1)).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void createPostDTO_invalidPostText() {
        // Arrange
        PostDTO postDTO = new PostDTO();
        postDTO.setPosterId(mockUser.getUserId());
        postDTO.setPostText(""); // Invalid text

        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(InvalidInputException.class, () -> postService.createPostDTO(postDTO));
        verify(userRepository, times(1)).findById(mockUser.getUserId());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void getUserFeed_success() {
        // Arrange
        Post connectionPost = new Post();
        connectionPost.setPostId(2L);
        connectionPost.setPoster(mockUser);
        connectionPost.setPostText("Connection post");
        connectionPost.setTimePosted(LocalDateTime.now().minusHours(1));

        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(postRepository.findByPoster(mockUser)).thenReturn(Optional.of(Collections.singletonList(mockPost)));
        when(postRepository.findByFollowing(mockUser)).thenReturn(Optional.of(Collections.singletonList(connectionPost)));

        // Act
        List<PostDTO> result = postService.getUserFeed(mockUser.getUserId());

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size()); // One post from user and one from connections
        assertEquals(mockPost.getPostText(), result.get(0).getPostText()); // Most recent post first
        verify(userRepository, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void getUserFeed_userNotFound() {
        // Arrange
        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postService.getUserFeed(mockUser.getUserId()));
        verify(userRepository, times(1)).findById(mockUser.getUserId());
    }

    @Test
    void deleteMessageById_success() {
        // Arrange
        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.of(mockPost));

        // Act
        postService.deletePostById(mockPost.getPostId());

        // Assert
        verify(postRepository, times(1)).findById(mockPost.getPostId());
        verify(postRepository, times(1)).delete(mockPost);
    }

    @Test
    void deleteMessageById_postNotFound() {
        // Arrange
        when(postRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postService.deletePostById(999L));
        verify(postRepository, times(1)).findById(999L);
        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void reactToPost_success() {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setReacterId(mockUser.getUserId());
        reactionDTO.setReactionType("LIKE");

        Reaction mockReaction = new Reaction();
        mockReaction.setReactionId(1L);
        mockReaction.setPost(mockPost);
        mockReaction.setReacter(mockUser);
        mockReaction.setReactionType(Reaction.ReactionType.LIKE);

        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.of(mockPost));
        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(reactionRepository.save(any(Reaction.class))).thenReturn(mockReaction);

        // Act
        ReactionDTO result = postService.reactToPost(mockPost.getPostId(), reactionDTO);

        // Assert
        assertNotNull(result);
        assertEquals("LIKE", result.getReactionType());
        verify(postRepository, times(1)).findById(mockPost.getPostId());
        verify(userRepository, times(1)).findById(mockUser.getUserId());
        verify(reactionRepository, times(1)).save(any(Reaction.class));
    }

    @Test
    void reactToPost_postOrUserNotFound() {
        // Arrange
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setReacterId(mockUser.getUserId());
        reactionDTO.setReactionType("LIKE");

        when(postRepository.findById(mockPost.getPostId())).thenReturn(Optional.empty());
        when(userRepository.findById(mockUser.getUserId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> postService.reactToPost(mockPost.getPostId(), reactionDTO));
        verify(postRepository, times(1)).findById(mockPost.getPostId());
        verify(userRepository, times(1)).findById(mockUser.getUserId());
        verify(reactionRepository, never()).save(any(Reaction.class));
    }
}