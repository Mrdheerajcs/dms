package com.dmsBackend.repository;

import com.dmsBackend.entity.DocumentHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader,Integer> {
    List<DocumentHeader> findByIsApprovedFalse();
    List<DocumentHeader> findByIsApprovedTrue();

    @Query(value = "SELECT * FROM document_header WHERE id = :id AND is_Approved = :isApproved", nativeQuery = true)
    Optional<DocumentHeader> findByIdAndStatus(@Param("id") Integer id, @Param("isApproved") Boolean isApproved);

}
