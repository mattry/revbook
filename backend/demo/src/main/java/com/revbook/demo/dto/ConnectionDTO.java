package com.revbook.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long connectionId;

    private Long followerId;
    private Long followeeId;
}
