package com.revbook.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revbook.demo.controller.PostController;
import com.revbook.demo.dto.PostDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    private ObjectMapper objectMapper;

    // Common test data
    private PostDTO postDTORequest;
    private PostDTO postDTOResponse;
    private ReactionDTO reactionDTORequest;
    private ReactionDTO reactionDTOResponse;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        // Initialize test data
        postDTORequest = new PostDTO(
                null,                 // postId
                101L,                       // posterId
                "Test Post Content",        // postText
                LocalDateTime.now(),        // timePosted
                "John",                     // firstName
                "Doe"                       // lastName

        );

        postDTOResponse = new PostDTO(
                1L,                    // postId
                101L,                       // posterId
                "Test Post Content",        // postText
                LocalDateTime.now(),        // timePosted
                "John",                     // firstName
                "Doe"                       // lastName
        );

        reactionDTORequest = new ReactionDTO(
                null,              // reactionId
                1L,                         // postId
                null,                       // commentId
                100L,                       // reacterId
                "LIKE",                     // reaction type
                "John",                     // firstName
                "Doe"                       // lastName
        );

        reactionDTOResponse = new ReactionDTO(
                1L,                        // reactionId
                1L,                                 // postId
                null,                               // commentId
                100L,                               // reacterId
                "LIKE",                             // reaction type
                "John",                             // firstName
                "Doe"                               // lastName
        );
    }

    @Test
    void createPost_Success() throws Exception {
        // Arrange
        Mockito.when(postService.createPostDTO(any(PostDTO.class))).thenReturn(postDTOResponse);

        // Act
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.postText").value("Test Post Content"));
    }


    @Test
    void getFeed_Success() throws Exception {
        // Arrange
        List<PostDTO> feed = Arrays.asList(
                postDTOResponse,
                new PostDTO(2L, 102L, "Second post content", LocalDateTime.now(), "Test", "User")
        );
        Mockito.when(postService.getUserFeed(eq(101L))).thenReturn(feed);

        // Act
        mockMvc.perform(get("/feed/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[1].postId").value(2));
    }

    @Test
    void getFeed_BadRequest() throws Exception {
        // Arrange
        Mockito.when(postService.getUserFeed(eq(101L))).thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/feed/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getUserPosts_Success() throws Exception {
        // Arrange
        List<PostDTO> userPosts = Arrays.asList(postDTOResponse);
        Mockito.when(postService.getUserPostDTOs(eq(101L))).thenReturn(userPosts);

        // Act
        mockMvc.perform(get("/posts/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[0].postText").value("Test Post Content"));
    }

    @Test
    void getUserPosts_BadRequest() throws Exception {
        // Arrange
        Mockito.when(postService.getUserPostDTOs(eq(101L))).thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/posts/101")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void deletePostById_Success() throws Exception {
        // Act
        mockMvc.perform(delete("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk());
    }

    @Test
    void deletePostById_BadRequest() throws Exception {
        // Arrange
        Mockito.doThrow(RuntimeException.class).when(postService).deletePostById(eq(1L));

        // Act
        mockMvc.perform(delete("/posts/1")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void reactToPost_Success() throws Exception {
        // Arrange
        Mockito.when(postService.reactToPost(eq(1L), any(ReactionDTO.class))).thenReturn(reactionDTOResponse);

        // Act
        mockMvc.perform(post("/posts/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reactionId").value(1))
                .andExpect(jsonPath("$.reactionType").value("LIKE"));
    }

    @Test
    void reactToPost_BadRequest() throws Exception {
        // Arrange
        Mockito.when(postService.reactToPost(eq(1L), any(ReactionDTO.class)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(post("/posts/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPostReactions_Success() throws Exception {
        // Arrange
        Set<ReactionDTO> reactions = new HashSet<>(Arrays.asList(reactionDTORequest, reactionDTOResponse));
        Mockito.when(postService.getPostReactions(eq(1L))).thenReturn(reactions);

        // Act
        mockMvc.perform(get("/posts/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reactionType").value("LIKE"))
                .andExpect(jsonPath("$[1].reactionType").value("LIKE"));
    }

    @Test
    void getPostReactions_BadRequest() throws Exception {
        // Arrange
        Mockito.when(postService.getPostReactions(eq(1L))).thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(get("/posts/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isBadRequest());
    }
}
