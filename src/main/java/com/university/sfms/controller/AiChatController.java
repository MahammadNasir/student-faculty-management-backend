package com.university.sfms.controller;

import com.university.sfms.dto.AiDtos.*;
import com.university.sfms.service.AiChatService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {
    private final AiChatService service;

    public AiChatController(AiChatService service) {
        this.service = service;
    }

    @PostMapping("/chat")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY','STUDENT')")
    public ChatResponse chat(@Valid @RequestBody ChatRequest request, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        return new ChatResponse(service.reply(request.message(), request.history(), role));
    }

    @PostMapping("/summarize")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY','STUDENT')")
    public DocumentSummaryResponse summarize(@RequestParam MultipartFile file, Authentication authentication) {
        String role = authentication.getAuthorities().stream()
                .findFirst()
                .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                .orElse("USER");
        return new DocumentSummaryResponse(file.getOriginalFilename(), service.summarize(file, role));
    }
}
