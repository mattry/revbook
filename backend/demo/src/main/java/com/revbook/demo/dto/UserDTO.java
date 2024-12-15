package com.revbook.demo.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
}
