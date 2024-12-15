package com.revbook.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReactionDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long reactionId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long postId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long commentId;

    private Long reacterId;

    private String reactionType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

}
