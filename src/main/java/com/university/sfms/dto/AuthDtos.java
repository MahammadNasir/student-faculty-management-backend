package com.university.sfms.dto;

import com.university.sfms.model.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class AuthDtos {
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
    public record LoginResponse(String token, String tokenType, String userId, String name, String email, RoleName role) {}
}
