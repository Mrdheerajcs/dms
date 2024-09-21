package com.dmsBackend.repository;

import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleMasterRepository extends JpaRepository<RoleMaster, Integer> {

    // Finds a role by the role field
    Optional<RoleMaster> findByRole(String role);
}
