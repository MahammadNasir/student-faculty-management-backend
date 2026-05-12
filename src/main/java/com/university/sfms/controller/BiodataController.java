package com.university.sfms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.sfms.dto.BiodataDtos.*;
import com.university.sfms.service.BiodataService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/biodata")
public class BiodataController {
    private final BiodataService service;

    public BiodataController(BiodataService service) {
        this.service = service;
    }

    @PostMapping("/students")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public StudentBiodataResponse saveStudent(@Valid @RequestBody StudentBiodataRequest request) {
        return service.saveStudent(request);
    }

    @GetMapping("/students/{userId}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY') or (hasRole('STUDENT') and authentication.name == @academicService.user(#userId).email)")
    public StudentBiodataResponse student(@PathVariable String userId) {
        return service.student(userId);
    }

    @PostMapping("/faculty")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public FacultyBiodataResponse saveFaculty(@Valid @RequestBody FacultyBiodataRequest request) {
        return service.saveFaculty(request);
    }

    @GetMapping("/faculty/{userId}")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN') or (hasRole('FACULTY') and authentication.name == @academicService.user(#userId).email)")
    public FacultyBiodataResponse faculty(@PathVariable String userId) {
        return service.faculty(userId);
    }
}
