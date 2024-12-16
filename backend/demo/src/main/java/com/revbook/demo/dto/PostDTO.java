package com.revbook.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;

    private Long posterId;

    private String postText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private LocalDateTime timePosted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;
}
