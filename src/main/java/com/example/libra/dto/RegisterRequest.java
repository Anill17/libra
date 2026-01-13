package com.example.libra.dto;

import com.example.libra.model.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private Role role = Role.MEMBER;
    private Long memberId; // Optional: link to existing member
}