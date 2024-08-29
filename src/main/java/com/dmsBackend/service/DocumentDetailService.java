package com.dmsBackend.service;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.RoleMaster;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentDetailService {

    DocumentDetails saveDocumentDetails(DocumentDetails documentDetails);
    DocumentDetails updateDocumentDetails(DocumentDetails documentDetails,Integer id);
    void deleteByIdDocumentDetails(Integer id);
    List<DocumentDetails> findAllDocumentDetails();
    Optional<DocumentDetails> findDocumentDetailsById(Integer id);

}

