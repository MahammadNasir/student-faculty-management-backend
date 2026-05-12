package com.university.sfms.repository;

import com.university.sfms.model.Mark;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MarkRepository extends JpaRepository<Mark, Long> {
    List<Mark> findByStudentUserId(String userId);
    List<Mark> findByFacultyUserId(String userId);
}
