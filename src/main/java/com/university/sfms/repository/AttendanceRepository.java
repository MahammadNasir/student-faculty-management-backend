package com.university.sfms.repository;

import com.university.sfms.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentUserId(String userId);
    List<Attendance> findByFacultyUserId(String userId);
}
