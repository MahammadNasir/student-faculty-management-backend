package com.university.sfms.service;

import com.university.sfms.exception.ApiException;
import com.university.sfms.dto.AiDtos.ChatMessage;
import com.university.sfms.model.Department;
import com.university.sfms.model.RoleName;
import com.university.sfms.model.Subject;
import com.university.sfms.repository.DepartmentRepository;
import com.university.sfms.repository.SubjectRepository;
import com.university.sfms.repository.UserRepository;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiChatService {
    private final String hfToken;
    private final String model;
    private final UserRepository users;
    private final DepartmentRepository departments;
    private final SubjectRepository subjects;
    private final RestClient restClient;
    private final Tika tika = new Tika();

    public AiChatService(
            @Value("${app.huggingface.token}") String hfToken,
            @Value("${app.huggingface.model}") String model,
            UserRepository users,
            DepartmentRepository departments,
            SubjectRepository subjects,
            RestClient.Builder builder) {
        this.hfToken = cleanToken(hfToken);
        this.model = model;
        this.users = users;
        this.departments = departments;
        this.subjects = subjects;
        this.restClient = builder
                .baseUrl("https://router.huggingface.co/v1")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String reply(String message, List<ChatMessage> history, String role) {
        String localAnswer = answerFromDatabase(message);
        if (localAnswer != null) {
            return localAnswer;
        }

        requireConfiguredAi();

        List<Map<String, Object>> input = new ArrayList<>();
        if (history != null) {
            history.stream()
                    .filter(item -> item != null && item.text() != null && !item.text().isBlank())
                    .skip(Math.max(0, history.size() - 10))
                    .forEach(item -> input.add(Map.of(
                            "role", "assistant".equalsIgnoreCase(item.role()) ? "assistant" : "user",
                            "content", item.text()
                    )));
        }
        input.add(Map.of("role", "user", "content", message));

        return askHuggingFace(input, systemInstructions(role));
    }

    public String summarize(MultipartFile file, String role) {
        requireConfiguredAi();
        if (file == null || file.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Upload a document to summarize.");
        }

        String text;
        try {
            text = tika.parseToString(file.getInputStream());
        } catch (IOException | TikaException ex) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Could not read text from this document. Try a PDF, DOCX, TXT, or another text-based file.");
        }

        if (text == null || text.isBlank()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This document does not contain readable text.");
        }

        String trimmed = text.length() > 60000 ? text.substring(0, 60000) : text;
        String prompt = "Summarize this uploaded document for a " + role + " user. "
                + "Include the main points, important dates or action items if present, and a short final takeaway.\n\n"
                + "Document name: " + safeFileName(file.getOriginalFilename()) + "\n\n"
                + trimmed;
        return askHuggingFace(List.of(Map.of("role", "user", "content", prompt)), systemInstructions(role));
    }

    private void requireConfiguredAi() {
        if (hfToken == null || hfToken.isBlank() || hfToken.equals("paste-your-huggingface-token-here")) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "AI assistant is not configured. Set HF_TOKEN in backend/.env and restart the backend.");
        }
    }

    private String cleanToken(String value) {
        if (value == null) {
            return null;
        }
        String cleaned = value.trim();
        if ((cleaned.startsWith("\"") && cleaned.endsWith("\""))
                || (cleaned.startsWith("'") && cleaned.endsWith("'"))) {
            cleaned = cleaned.substring(1, cleaned.length() - 1).trim();
        }
        return cleaned;
    }

    private String askHuggingFace(List<Map<String, Object>> input, String instructions) {
        List<Map<String, Object>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", instructions));
        messages.addAll(input);

        Map<String, Object> body = Map.of(
                "model", model,
                "messages", messages,
                "max_tokens", 900
        );

        try {
            Map<?, ?> response = restClient.post()
                    .uri("/chat/completions")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + hfToken)
                    .body(body)
                    .retrieve()
                    .body(Map.class);
            return extractChatCompletionText(response);
        } catch (RestClientResponseException ex) {
            String details = ex.getResponseBodyAsString();
            String reason = details == null || details.isBlank()
                    ? ex.getStatusText()
                    : details.length() > 260 ? details.substring(0, 260) + "..." : details;
            throw new ApiException(HttpStatus.BAD_GATEWAY, "Hugging Face request failed with status " + ex.getStatusCode().value() + ": " + reason);
        } catch (RuntimeException ex) {
            throw new ApiException(HttpStatus.BAD_GATEWAY, "AI service request failed. Check HF_TOKEN, HF_MODEL, and network access.");
        }
    }

    private String systemInstructions(String role) {
        return "You are the AI assistant inside a Student Faculty Management System. "
                + "Answer general questions clearly, summarize documents, and help users understand workflows for users, departments, subjects, attendance, marks, assignments, biodata, and role permissions. "
                + "The current signed-in user's role is " + role + ". "
                + "Do not claim to perform database actions unless the UI/API actually exposes them. "
                + "Keep answers practical, structured, and concise unless the user asks for detail.";
    }

    private String safeFileName(String fileName) {
        return fileName == null || fileName.isBlank() ? "uploaded document" : fileName;
    }

    private String answerFromDatabase(String message) {
        String normalized = message.toLowerCase();
        if (mentionsDepartment(normalized)) {
            List<Department> allDepartments = departments.findAll();
            if (allDepartments.isEmpty()) {
                return "There are 0 departments.";
            }
            String names = allDepartments.stream()
                    .map(department -> department.getCode() + " - " + department.getName())
                    .sorted()
                    .reduce((left, right) -> left + ", " + right)
                    .orElse("");
            return "There are " + allDepartments.size() + " departments: " + names + ".";
        }
        if (mentionsSubject(normalized)) {
            List<Subject> allSubjects = subjects.findAll();
            if (allSubjects.isEmpty()) {
                return "There are 0 subjects.";
            }
            String names = allSubjects.stream()
                    .map(subject -> subject.getCode() + " - " + subject.getName())
                    .sorted()
                    .reduce((left, right) -> left + ", " + right)
                    .orElse("");
            return "There are " + allSubjects.size() + " subjects: " + names + ".";
        }
        if (mentionsRole(normalized)) {
            return "User roles: "
                    + users.countByRole_Name(RoleName.SUPERADMIN) + " SUPERADMIN, "
                    + users.countByRole_Name(RoleName.ADMIN) + " ADMIN, "
                    + users.countByRole_Name(RoleName.FACULTY) + " FACULTY, "
                    + users.countByRole_Name(RoleName.STUDENT) + " STUDENT.";
        }
        if (!normalized.contains("how many") && !normalized.contains("count")) {
            return null;
        }
        if (normalized.contains("superadmin") || normalized.contains("super admin")) {
            return "There are " + users.countByRole_Name(RoleName.SUPERADMIN) + " SUPERADMIN users.";
        }
        if (normalized.contains("admin")) {
            return "There are " + users.countByRole_Name(RoleName.ADMIN) + " ADMIN users.";
        }
        if (normalized.contains("faculty")) {
            return "There are " + users.countByRole_Name(RoleName.FACULTY) + " FACULTY users.";
        }
        if (normalized.contains("student")) {
            return "There are " + users.countByRole_Name(RoleName.STUDENT) + " STUDENT users.";
        }
        if (normalized.contains("user")) {
            return "There are " + users.count() + " total users.";
        }
        return null;
    }

    private boolean mentionsDepartment(String message) {
        return message.contains("department") || message.contains("departments");
    }

    private boolean mentionsSubject(String message) {
        return message.contains("subject") || message.contains("subjects");
    }

    private boolean mentionsRole(String message) {
        return message.contains("role") || message.contains("roles");
    }

    private String extractChatCompletionText(Map<?, ?> response) {
        Object choices = response == null ? null : response.get("choices");
        if (choices instanceof List<?> items && !items.isEmpty()
                && items.get(0) instanceof Map<?, ?> choice
                && choice.get("message") instanceof Map<?, ?> message
                && message.get("content") instanceof String text
                && !text.isBlank()) {
            return text;
        }
        return "I could not read the AI response. Please try again.";
    }
}
