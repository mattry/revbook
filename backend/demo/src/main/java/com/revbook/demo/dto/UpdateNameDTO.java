package com.revbook.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNameDTO {
    private Long userId;
    private String firstName;
    private String lastName;
}
