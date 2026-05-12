package com.university.sfms.controller;

import com.university.sfms.dto.UserDtos.*;
import com.university.sfms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public List<UserResponse> all() {
        return service.all();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request, Authentication auth) {
        boolean superAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"));
        return service.create(request, superAdmin);
    }

    @PatchMapping("/{userId}/enabled")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public UserResponse enabled(@PathVariable String userId, @RequestBody EnabledRequest request) {
        return service.setEnabled(userId, request.enabled());
    }

    @PatchMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("isAuthenticated()")
    public void resetPassword(@PathVariable String userId, @Valid @RequestBody ResetPasswordRequest request, Authentication auth) {
        var target = service.getByUserId(userId);
        boolean isSuperAdmin = auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SUPERADMIN"));
        if (!isSuperAdmin && !auth.getName().equals(target.getEmail())) {
            throw new com.university.sfms.exception.ApiException(HttpStatus.FORBIDDEN, "Not allowed to change this password");
        }
        service.resetPassword(userId, request.newPassword());
    }
}
