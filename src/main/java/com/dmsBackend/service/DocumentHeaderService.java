package com.dmsBackend.service;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;

import java.util.List;
import java.util.Optional;

public interface DocumentHeaderService {
    List<DocumentHeader>getAllPendingStatus();
    List<DocumentHeader>getAllApprovedStatus();
    DocumentHeader updateStatus(Integer id, Boolean isApproved);

    DocumentHeader createDocumentHeader(DocumentHeader documentHeader);

    DocumentHeader updateDocumentHeader(DocumentHeader documentHeader, Integer id);
    void deleteByIdDocumentHeader(Integer id);
    List<DocumentHeader> findAllDocumentHeader();
    Optional<DocumentHeader> findDocumentHeaderById(Integer id);

    Optional<DocumentHeader> getApprovedDocumentById(Integer id);

//    DocumentHeader getApprovedDocumentById(Integer docId);


}
