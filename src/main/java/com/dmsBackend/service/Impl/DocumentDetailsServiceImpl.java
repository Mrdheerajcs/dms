package com.dmsBackend.service.Impl;


import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.DocumentDetailRepository;
import com.dmsBackend.service.DocumentDetailService;
import com.dmsBackend.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentDetailsServiceImpl implements DocumentDetailService {

    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    DocumentDetailRepository documentDetailRepository;

    @Override
    public DocumentDetails saveDocumentDetails(DocumentDetails documentDetails) {
        documentDetails.setCreatedOn(Helper.getCurrentTimeStamp());
        documentDetails.setUpdatedOn(Helper.getCurrentTimeStamp());
        return documentDetailRepository.save(documentDetails);
    }

    @Override
    public DocumentDetails updateDocumentDetails(DocumentDetails documentDetails, Integer id) {
        Optional<DocumentDetails> documentDetailsOptional = documentDetailRepository.findById(id);

        if (documentDetailsOptional.isPresent()) {
            DocumentDetails documentDetails2 = documentDetailsOptional.get();
            documentDetails2.setPath(documentDetails.getPath());
            documentDetails2.setDocumentHeader(documentDetails.getDocumentHeader());
            documentDetails2.setCategory(documentDetails.getCategory());
            documentDetails2.setYear(documentDetails.getYear());
            documentDetails2.setType(documentDetails.getType());
            documentDetails2.setEmployee(documentDetails.getEmployee());
            documentDetails2.setDepartment(documentDetails.getDepartment());
            documentDetails2.setBranch(documentDetails.getBranch());
            documentDetails2.setUpdatedOn(Helper.getCurrentTimeStamp());

            return documentDetailRepository.save(documentDetails2);
        } else {
            throw new ResourceNotFoundException("DocumentDetail not found for ", "Id", id);
        }
    }

    @Override
    public void deleteByIdDocumentDetails(Integer id) {
      this.documentDetailRepository.deleteById(id);
    }

    @Override
    public List<DocumentDetails> findAllDocumentDetails() {
        return documentDetailRepository.findAll();
    }

    @Override
    public Optional<DocumentDetails> findDocumentDetailsById(Integer id) {
        return documentDetailRepository.findById(id);
    }
}
