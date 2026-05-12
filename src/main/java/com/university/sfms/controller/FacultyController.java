package com.university.sfms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.university.sfms.dto.AcademicDtos.*;
import com.university.sfms.service.FacultyWorkService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/faculty")
@PreAuthorize("hasRole('FACULTY')")
public class FacultyController {
    private final FacultyWorkService service;

    public FacultyController(FacultyWorkService service) {
        this.service = service;
    }

    @PostMapping("/attendance")
    @ResponseStatus(HttpStatus.CREATED)
    public AttendanceResponse attendance(@Valid @RequestBody AttendanceRequest request, Authentication auth) {
        return service.uploadAttendance(request, auth.getName());
    }

    @PostMapping("/marks")
    @ResponseStatus(HttpStatus.CREATED)
    public MarkResponse marks(@Valid @RequestBody MarkRequest request, Authentication auth) {
        return service.uploadMark(request, auth.getName());
    }

    @PostMapping("/assignments")
    @ResponseStatus(HttpStatus.CREATED)
    public AssignmentResponse assignments(@Valid @RequestBody AssignmentRequest request, Authentication auth) {
        return service.uploadAssignment(request, auth.getName());
    }

    @GetMapping("/{userId}/attendance")
    public List<AttendanceResponse> facultyAttendance(@PathVariable String userId) {
        return service.facultyAttendance(userId);
    }
}
