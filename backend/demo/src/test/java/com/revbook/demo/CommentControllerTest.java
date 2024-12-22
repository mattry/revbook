package com.revbook.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revbook.demo.controller.CommentController;
import com.revbook.demo.dto.CommentDTO;
import com.revbook.demo.dto.ReactionDTO;
import com.revbook.demo.service.CommentService;
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
class CommentControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CommentController commentController;

    @Mock
    private CommentService commentService;

    private ObjectMapper objectMapper;

    // Common test data
    private CommentDTO commentDTORequest;
    private CommentDTO commentDTOResponse;
    private ReactionDTO reactionDTORequest;
    private ReactionDTO reactionDTOResponse;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
        objectMapper = new ObjectMapper();

        // Initialize test data
        commentDTORequest = new CommentDTO(
                null,              // commentId
                100L,                       // posterId
                200L,                       // postId
                "Test comment",             // commentText
                null,                       // parentCommentId
                null,                       // timePosted
                "John",                     // firstName
                "Doe"                       // lastName
        );

        commentDTOResponse = new CommentDTO(
                1L,                // commentId
                100L,                       // posterId
                200L,                       // postId
                "Test comment",             // commentText
                0L,                         // parentCommentId
                LocalDateTime.now(),        // timePosted
                "John",                     // firstName
                "Doe"                       // lastName
        );

        reactionDTORequest = new ReactionDTO(
                null,              // reactionId
                null,                       // postId
                1L,                         // commentId
                100L,                       // reacterId
                "LIKE",                     // reaction type
                "John",                     // firstName
                "Doe"                       // lastName
        );

        reactionDTOResponse = new ReactionDTO(
                1L,                         // reactionId
                null,                                // postId
                1L,                                 // commentId
                100L,                               // reacterId
                "LIKE",                             // reaction type
                "John",                             // firstName
                "Doe"                               // lastName
        );
    }

    @Test
    void commentOnPost_Success() throws Exception {
        // Arrange
        Mockito.when(commentService.commentOnPost(eq(200L), any(CommentDTO.class))).thenReturn(commentDTOResponse);

        // Act
        mockMvc.perform(post("/posts/200/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))
                .andExpect(jsonPath("$.postId").value(200))
                .andExpect(jsonPath("$.commentText").value("Test comment"));
    }

    @Test
    void commentOnPost_BadRequest() throws Exception {
        // Arrange
        Mockito.when(commentService.commentOnPost(eq(200L), any(CommentDTO.class)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(post("/posts/200/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void commentOnComment_Success() throws Exception {
        // Arrange
        Mockito.when(commentService.commentOnComment(eq(1L), any(CommentDTO.class))).thenReturn(commentDTOResponse);

        // Act
        mockMvc.perform(post("/comments/1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(1))
                .andExpect(jsonPath("$.parentCommentId").value(0))
                .andExpect(jsonPath("$.commentText").value("Test comment"));
    }

    @Test
    void commentOnComment_BadRequest() throws Exception {
        // Arrange
        Mockito.when(commentService.commentOnComment(eq(1L), any(CommentDTO.class)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(post("/comments/1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCommentsByPost_Success() throws Exception {
        // Arrange
        List<CommentDTO> commentDTOs = Arrays.asList(
                commentDTOResponse,
                new CommentDTO(2L, 101L, 200L, "Another comment", null, LocalDateTime.now(), "Jane", "Doe")
        );
        Mockito.when(commentService.getCommentDTOsByPostId(eq(200L))).thenReturn(commentDTOs);

        // Act
        mockMvc.perform(get("/posts/200/comments")
                        .contentType(MediaType.APPLICATION_JSON))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].commentId").value(1))
                .andExpect(jsonPath("$[1].commentId").value(2));
    }

    @Test
    void reactToComment_Success() throws Exception {
        // Arrange
        Mockito.when(commentService.reactToComment(eq(1L), any(ReactionDTO.class))).thenReturn(reactionDTOResponse);

        // Act
        mockMvc.perform(post("/comments/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionDTORequest)))

                // Assert
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reactionId").value(1))
                .andExpect(jsonPath("$.reactionType").value("LIKE"));
    }

    @Test
    void reactToComment_BadRequest() throws Exception {
        // Arrange
        Mockito.when(commentService.reactToComment(eq(1L), any(ReactionDTO.class)))
                .thenThrow(RuntimeException.class);

        // Act
        mockMvc.perform(post("/comments/1/reactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reactionDTORequest)))

                // Assert
                .andExpect(status().isBadRequest());
    }
}
