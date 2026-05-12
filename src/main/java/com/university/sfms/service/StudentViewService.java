package com.university.sfms.service;

import com.university.sfms.dto.AcademicDtos.*;
import com.university.sfms.repository.AssignmentRepository;
import com.university.sfms.repository.AttendanceRepository;
import com.university.sfms.repository.MarkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class StudentViewService {
    private final AttendanceRepository attendance;
    private final MarkRepository marks;
    private final AssignmentRepository assignments;
    private final FacultyWorkService mapper;

    public StudentViewService(AttendanceRepository attendance, MarkRepository marks, AssignmentRepository assignments, FacultyWorkService mapper) {
        this.attendance = attendance;
        this.marks = marks;
        this.assignments = assignments;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> attendance(String studentUserId) {
        return attendance.findByStudentUserId(studentUserId).stream().map(mapper::toAttendance).toList();
    }

    @Transactional(readOnly = true)
    public List<MarkResponse> marks(String studentUserId) {
        return marks.findByStudentUserId(studentUserId).stream().map(mapper::toMark).toList();
    }

    public List<AssignmentResponse> assignments() {
        return assignments.findAll().stream().map(mapper::toAssignment).toList();
    }
}
