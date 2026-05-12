package com.university.sfms.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AcademicDtos {
    public record DepartmentRequest(@NotBlank String code, @NotBlank String name) {}
    public record DepartmentResponse(Long id, String code, String name) {}
    public record SubjectRequest(@NotBlank String code, @NotBlank String name, @NotNull Integer semester, @NotNull Long departmentId, String facultyUserId) {}
    public record SubjectResponse(Long id, String code, String name, Integer semester, Long departmentId, String departmentName, String facultyUserId, String facultyName) {}
    public record AttendanceRequest(@NotBlank String studentUserId, @NotNull Long subjectId, @NotNull LocalDate attendanceDate, boolean present) {}
    public record AttendanceResponse(Long id, String studentUserId, String studentName, Long subjectId, String subjectName, String facultyUserId, LocalDate attendanceDate, boolean present) {}
    public record MarkRequest(@NotBlank String studentUserId, @NotNull Long subjectId, @NotBlank String examType, @DecimalMin("0.0") BigDecimal score, @DecimalMin("1.0") BigDecimal maxScore) {}
    public record MarkResponse(Long id, String studentUserId, String studentName, Long subjectId, String subjectName, String examType, BigDecimal score, BigDecimal maxScore) {}
    public record AssignmentRequest(@NotNull Long subjectId, @NotBlank String title, @NotBlank String description, LocalDate dueDate, String fileUrl) {}
    public record AssignmentResponse(Long id, Long subjectId, String subjectName, String facultyUserId, String title, String description, LocalDate dueDate, String fileUrl) {}
}
