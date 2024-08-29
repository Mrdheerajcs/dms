package com.dmsBackend.repository;

import com.dmsBackend.entity.DocumentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentDetailRepository extends JpaRepository<DocumentDetails,Integer> {
}
