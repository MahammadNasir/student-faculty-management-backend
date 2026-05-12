package com.university.sfms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public class AiDtos {
    public record ChatRequest(@NotBlank @Size(max = 2000) String message, List<ChatMessage> history) {}
    public record ChatMessage(@NotBlank String role, @NotBlank @Size(max = 4000) String text) {}
    public record ChatResponse(String answer) {}
    public record DocumentSummaryResponse(String fileName, String summary) {}
}
