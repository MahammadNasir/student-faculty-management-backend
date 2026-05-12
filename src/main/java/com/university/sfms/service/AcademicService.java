package com.university.sfms.service;

import com.university.sfms.dto.AcademicDtos.*;
import com.university.sfms.exception.ApiException;
import com.university.sfms.model.*;
import com.university.sfms.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AcademicService {
    private final DepartmentRepository departments;
    private final SubjectRepository subjects;
    private final UserRepository users;

    public AcademicService(DepartmentRepository departments, SubjectRepository subjects, UserRepository users) {
        this.departments = departments;
        this.subjects = subjects;
        this.users = users;
    }

    public List<DepartmentResponse> departments() {
        return departments.findAll().stream().map(this::toDepartment).toList();
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> subjects() {
        return subjects.findAll().stream().map(this::toSubject).toList();
    }

    @Transactional
    public DepartmentResponse saveDepartment(DepartmentRequest request) {
        Department d = departments.findByCode(request.code()).orElseGet(Department::new);
        d.setCode(request.code());
        d.setName(request.name());
        return toDepartment(departments.save(d));
    }

    @Transactional
    public SubjectResponse saveSubject(SubjectRequest request) {
        Subject subject = subjects.findByCode(request.code()).orElseGet(Subject::new);
        subject.setCode(request.code());
        subject.setName(request.name());
        subject.setSemester(request.semester());
        subject.setDepartment(department(request.departmentId()));
        if (request.facultyUserId() != null && !request.facultyUserId().isBlank()) {
            User faculty = user(request.facultyUserId());
            if (faculty.getRole().getName() != RoleName.FACULTY) throw new ApiException(HttpStatus.BAD_REQUEST, "Assigned user must be FACULTY");
            subject.setFaculty(faculty);
        }
        return toSubject(subjects.save(subject));
    }

    public Department department(Long id) {
        return departments.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Department not found"));
    }

    public Subject subject(Long id) {
        return subjects.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Subject not found"));
    }

    public User user(String userId) {
        return users.findByUserId(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public User userByEmail(String email) {
        return users.findByEmail(email).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private DepartmentResponse toDepartment(Department d) {
        return new DepartmentResponse(d.getId(), d.getCode(), d.getName());
    }

    public SubjectResponse toSubject(Subject s) {
        return new SubjectResponse(s.getId(), s.getCode(), s.getName(), s.getSemester(), s.getDepartment().getId(), s.getDepartment().getName(),
                s.getFaculty() == null ? null : s.getFaculty().getUserId(), s.getFaculty() == null ? null : s.getFaculty().getName());
    }
}
