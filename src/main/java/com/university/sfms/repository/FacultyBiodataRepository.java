package com.university.sfms.repository;

import com.university.sfms.model.FacultyBiodata;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultyBiodataRepository extends JpaRepository<FacultyBiodata, Long> {
    Optional<FacultyBiodata> findByUserUserId(String userId);
}
