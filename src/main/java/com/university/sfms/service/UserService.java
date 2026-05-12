package com.university.sfms.service;

import com.university.sfms.dto.UserDtos.*;
import com.university.sfms.exception.ApiException;
import com.university.sfms.model.RoleName;
import com.university.sfms.model.User;
import com.university.sfms.repository.RoleRepository;
import com.university.sfms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;

    public UserService(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
    }

    public List<UserResponse> all() {
        return users.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public UserResponse create(CreateUserRequest request, boolean actorIsSuperAdmin) {
        if (!actorIsSuperAdmin && request.role() == RoleName.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only SUPERADMIN can create ADMIN users");
        }
        if (users.existsByEmail(request.email()) || users.existsByUserId(request.userId())) {
            throw new ApiException(HttpStatus.CONFLICT, "User id or email already exists");
        }
        User user = new User();
        user.setUserId(request.userId());
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(encoder.encode(request.password()));
        user.setRole(roles.findByName(request.role()).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, "Role not found")));
        return toResponse(users.save(user));
    }

    @Transactional
    public UserResponse setEnabled(String userId, boolean enabled) {
        User user = getByUserId(userId);
        user.setEnabled(enabled);
        return toResponse(user);
    }

    @Transactional
    public void resetPassword(String userId, String password) {
        getByUserId(userId).setPassword(encoder.encode(password));
    }

    public User getByUserId(String userId) {
        return users.findByUserId(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(user.getIndex(), user.getUserId(), user.getName(), user.getEmail(), user.getRole().getName(), user.getCreatedAt(), user.isEnabled());
    }
}
