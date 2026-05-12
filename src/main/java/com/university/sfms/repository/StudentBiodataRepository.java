package com.university.sfms.repository;

import com.university.sfms.model.StudentBiodata;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentBiodataRepository extends JpaRepository<StudentBiodata, Long> {
    Optional<StudentBiodata> findByUserUserId(String userId);
}
