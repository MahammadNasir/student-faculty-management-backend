package com.university.sfms.repository;

import com.university.sfms.model.RoleName;
import com.university.sfms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByUserId(String userId);
    List<User> findByRole_Name(RoleName roleName);
    long countByRole_Name(RoleName roleName);
}
