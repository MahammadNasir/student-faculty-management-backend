package com.university.sfms.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BiodataDtos {
    public record StudentBiodataRequest(
            @NotBlank String userId, @NotBlank String fatherName, @NotBlank String motherName,
            LocalDate dob, String gender, String address, String phone, String emergencyContact,
            String bloodGroup, @NotNull Long departmentId, Integer year, String section, String imageUrl) {}
    public record StudentBiodataResponse(
            String userId, String name, String fatherName, String motherName, LocalDate dob, String gender,
            String address, String phone, String emergencyContact, String bloodGroup, Long departmentId,
            String departmentName, Integer year, String section, String imageUrl) {}
    public record FacultyBiodataRequest(
            @NotBlank String userId, @NotBlank String qualification, @NotNull Integer experience,
            @NotBlank String specialization, @NotNull Long departmentId, String imageUrl) {}
    public record FacultyBiodataResponse(
            String userId, String name, String qualification, Integer experience, String specialization,
            Long departmentId, String departmentName, String imageUrl) {}
}
