package com.revbook.demo.dto;

// this class is for registering users only and will not be returned to the frontend, only received

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long userId;

    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String firstName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lastName;

    private String password;

}
