package com.university.sfms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.sfms.dto.AcademicDtos.*;
import com.university.sfms.service.AcademicService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/academics")
public class AcademicController {
    private final AcademicService service;

    public AcademicController(AcademicService service) {
        this.service = service;
    }

    @GetMapping("/departments")
    public List<DepartmentResponse> departments() {
        return service.departments();
    }

    @PostMapping("/departments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public DepartmentResponse saveDepartment(@Valid @RequestBody DepartmentRequest request) {
        return service.saveDepartment(request);
    }

    @GetMapping("/subjects")
    public List<SubjectResponse> subjects() {
        return service.subjects();
    }

    @PostMapping("/subjects")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN')")
    public SubjectResponse saveSubject(@Valid @RequestBody SubjectRequest request) {
        return service.saveSubject(request);
    }
}
