package com.university.sfms.service;

import com.university.sfms.dto.BiodataDtos.*;
import com.university.sfms.exception.ApiException;
import com.university.sfms.model.*;
import com.university.sfms.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BiodataService {
    private final StudentBiodataRepository students;
    private final FacultyBiodataRepository faculty;
    private final AcademicService academics;

    public BiodataService(StudentBiodataRepository students, FacultyBiodataRepository faculty, AcademicService academics) {
        this.students = students;
        this.faculty = faculty;
        this.academics = academics;
    }

    @Transactional
    public StudentBiodataResponse saveStudent(StudentBiodataRequest request) {
        User user = academics.user(request.userId());
        if (user.getRole().getName() != RoleName.STUDENT) throw new ApiException(HttpStatus.BAD_REQUEST, "User must be STUDENT");
        StudentBiodata data = students.findByUserUserId(request.userId()).orElseGet(StudentBiodata::new);
        data.setUser(user);
        data.setFatherName(request.fatherName());
        data.setMotherName(request.motherName());
        data.setDob(request.dob());
        data.setGender(request.gender());
        data.setAddress(request.address());
        data.setPhone(request.phone());
        data.setEmergencyContact(request.emergencyContact());
        data.setBloodGroup(request.bloodGroup());
        data.setDepartment(academics.department(request.departmentId()));
        data.setYear(request.year());
        data.setSection(request.section());
        data.setImageUrl(request.imageUrl());
        return toStudent(students.save(data));
    }

    @Transactional(readOnly = true)
    public StudentBiodataResponse student(String userId) {
        return toStudent(students.findByUserUserId(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Student biodata not found")));
    }

    @Transactional
    public FacultyBiodataResponse saveFaculty(FacultyBiodataRequest request) {
        User user = academics.user(request.userId());
        if (user.getRole().getName() != RoleName.FACULTY) throw new ApiException(HttpStatus.BAD_REQUEST, "User must be FACULTY");
        FacultyBiodata data = faculty.findByUserUserId(request.userId()).orElseGet(FacultyBiodata::new);
        data.setUser(user);
        data.setQualification(request.qualification());
        data.setExperience(request.experience());
        data.setSpecialization(request.specialization());
        data.setDepartment(academics.department(request.departmentId()));
        data.setImageUrl(request.imageUrl());
        return toFaculty(faculty.save(data));
    }

    @Transactional(readOnly = true)
    public FacultyBiodataResponse faculty(String userId) {
        return toFaculty(faculty.findByUserUserId(userId).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Faculty biodata not found")));
    }

    private StudentBiodataResponse toStudent(StudentBiodata d) {
        return new StudentBiodataResponse(d.getUser().getUserId(), d.getUser().getName(), d.getFatherName(), d.getMotherName(), d.getDob(), d.getGender(),
                d.getAddress(), d.getPhone(), d.getEmergencyContact(), d.getBloodGroup(), d.getDepartment().getId(), d.getDepartment().getName(), d.getYear(), d.getSection(), d.getImageUrl());
    }

    private FacultyBiodataResponse toFaculty(FacultyBiodata d) {
        return new FacultyBiodataResponse(d.getUser().getUserId(), d.getUser().getName(), d.getQualification(), d.getExperience(), d.getSpecialization(),
                d.getDepartment().getId(), d.getDepartment().getName(), d.getImageUrl());
    }
}
