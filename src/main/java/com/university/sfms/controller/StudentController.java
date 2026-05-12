package com.university.sfms.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.university.sfms.dto.AcademicDtos.*;
import com.university.sfms.service.StudentViewService;

@RestController
@RequestMapping("/api/students/{userId}")
public class StudentController {
    private final StudentViewService service;

    public StudentController(StudentViewService service) {
        this.service = service;
    }

    @GetMapping("/attendance")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY') or (hasRole('STUDENT') and authentication.name == @academicService.user(#userId).email)")
    public List<AttendanceResponse> attendance(@PathVariable String userId) {
        return service.attendance(userId);
    }

    @GetMapping("/marks")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY') or (hasRole('STUDENT') and authentication.name == @academicService.user(#userId).email)")
    public List<MarkResponse> marks(@PathVariable String userId) {
        return service.marks(userId);
    }

    @GetMapping("/assignments")
    @PreAuthorize("hasAnyRole('SUPERADMIN','ADMIN','FACULTY','STUDENT')")
    public List<AssignmentResponse> assignments() {
        return service.assignments();
    }
}
