package com.dmsBackend.repository;

import com.dmsBackend.entity.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, Integer> {
}
