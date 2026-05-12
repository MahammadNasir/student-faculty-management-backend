package com.university.sfms.repository;

import com.university.sfms.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByFacultyUserId(String userId);
}
