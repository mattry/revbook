package com.revbook.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long commentId;

    private Long posterId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;

    private String commentText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long parentCommentId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timePosted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

}
