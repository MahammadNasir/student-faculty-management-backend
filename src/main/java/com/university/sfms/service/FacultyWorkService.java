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
public class FacultyWorkService {
    private final AttendanceRepository attendanceRepo;
    private final MarkRepository markRepo;
    private final AssignmentRepository assignmentRepo;
    private final AcademicService academics;

    public FacultyWorkService(AttendanceRepository attendanceRepo, MarkRepository markRepo, AssignmentRepository assignmentRepo, AcademicService academics) {
        this.attendanceRepo = attendanceRepo;
        this.markRepo = markRepo;
        this.assignmentRepo = assignmentRepo;
        this.academics = academics;
    }

    @Transactional
    public AttendanceResponse uploadAttendance(AttendanceRequest request, String facultyEmail) {
        User faculty = facultyByEmail(facultyEmail);
        User student = academics.user(request.studentUserId());
        if (student.getRole().getName() != RoleName.STUDENT) throw new ApiException(HttpStatus.BAD_REQUEST, "Attendance user must be STUDENT");
        Attendance a = new Attendance();
        a.setFaculty(faculty);
        a.setStudent(student);
        a.setSubject(academics.subject(request.subjectId()));
        a.setAttendanceDate(request.attendanceDate());
        a.setPresent(request.present());
        return toAttendance(attendanceRepo.save(a));
    }

    @Transactional
    public MarkResponse uploadMark(MarkRequest request, String facultyEmail) {
        User faculty = facultyByEmail(facultyEmail);
        User student = academics.user(request.studentUserId());
        if (student.getRole().getName() != RoleName.STUDENT) throw new ApiException(HttpStatus.BAD_REQUEST, "Marks user must be STUDENT");
        Mark m = new Mark();
        m.setFaculty(faculty);
        m.setStudent(student);
        m.setSubject(academics.subject(request.subjectId()));
        m.setExamType(request.examType());
        m.setScore(request.score());
        m.setMaxScore(request.maxScore());
        return toMark(markRepo.save(m));
    }

    @Transactional
    public AssignmentResponse uploadAssignment(AssignmentRequest request, String facultyEmail) {
        User faculty = facultyByEmail(facultyEmail);
        Assignment a = new Assignment();
        a.setFaculty(faculty);
        a.setSubject(academics.subject(request.subjectId()));
        a.setTitle(request.title());
        a.setDescription(request.description());
        a.setDueDate(request.dueDate());
        a.setFileUrl(request.fileUrl());
        return toAssignment(assignmentRepo.save(a));
    }

    public List<AttendanceResponse> facultyAttendance(String userId) {
        return attendanceRepo.findByFacultyUserId(userId).stream().map(this::toAttendance).toList();
    }

    public List<MarkResponse> facultyMarks(String userId) {
        return markRepo.findByFacultyUserId(userId).stream().map(this::toMark).toList();
    }

    public List<AssignmentResponse> assignments() {
        return assignmentRepo.findAll().stream().map(this::toAssignment).toList();
    }

    private User facultyByEmail(String email) {
        User user = academics.userByEmail(email);
        if (user.getRole().getName() != RoleName.FACULTY) throw new ApiException(HttpStatus.FORBIDDEN, "Faculty role required");
        return user;
    }

    public AttendanceResponse toAttendance(Attendance a) {
        return new AttendanceResponse(a.getId(), a.getStudent().getUserId(), a.getStudent().getName(), a.getSubject().getId(), a.getSubject().getName(), a.getFaculty().getUserId(), a.getAttendanceDate(), a.isPresent());
    }

    public MarkResponse toMark(Mark m) {
        return new MarkResponse(m.getId(), m.getStudent().getUserId(), m.getStudent().getName(), m.getSubject().getId(), m.getSubject().getName(), m.getExamType(), m.getScore(), m.getMaxScore());
    }

    public AssignmentResponse toAssignment(Assignment a) {
        return new AssignmentResponse(a.getId(), a.getSubject().getId(), a.getSubject().getName(), a.getFaculty().getUserId(), a.getTitle(), a.getDescription(), a.getDueDate(), a.getFileUrl());
    }
}
