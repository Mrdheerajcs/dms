package com.dmsBackend.repository;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, Integer> {
    List<DocumentDetails> findByDocumentHeader(DocumentHeader documentHeader);
    List<DocumentDetails> findByDocumentHeaderId(Long documentHeaderId);

    void deleteByDocumentHeader(DocumentHeader documentHeader);
}
