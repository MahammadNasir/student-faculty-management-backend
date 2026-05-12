package com.university.sfms.dto;

import java.time.Instant;

import com.university.sfms.model.RoleName;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserDtos {
    public record CreateUserRequest(
            @NotBlank String userId,
            @NotBlank String name,
            @Email @NotBlank String email,
            @Size(min = 8) String password,
            @NotNull RoleName role
    ) {}

    public record UserResponse(Long index, String userId, String name, String email, RoleName role, Instant createdAt, boolean enabled) {}
    public record ResetPasswordRequest(@Size(min = 8) String newPassword) {}
    public record EnabledRequest(boolean enabled) {}
}
