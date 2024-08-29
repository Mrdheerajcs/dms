package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.DocumentHeaderRepository;
import com.dmsBackend.service.DocumentHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentHeaderServiceImpl implements DocumentHeaderService {
    @Autowired
    DocumentHeaderRepository documentHeaderRepository;

    public DocumentHeader createDocumentHeader(DocumentHeader documentHeader) {
        return documentHeaderRepository.save(documentHeader);
    }

    @Override
    public DocumentHeader updateDocumentHeader(DocumentHeader documentHeader, Integer id) {
        Optional<DocumentHeader> documentHeaderOptional = documentHeaderRepository.findById(id);

        if (documentHeaderOptional.isPresent()) {
            DocumentHeader documentHeader1 = documentHeaderOptional.get();
            documentHeader1.setTitle(documentHeader.getTitle());
            documentHeader1.setFileNo(documentHeader.getFileNo());
            documentHeader1.setSubject(documentHeader.getSubject());
            documentHeader1.setVersion(documentHeader.getVersion());
            documentHeader1.setType(documentHeader.getType());
            documentHeader1.setEmployee(documentHeader.getEmployee());
            documentHeader1.setDepartment(documentHeader.getDepartment());
            documentHeader1.setBranch(documentHeader.getBranch());
            documentHeader1.setCategory(documentHeader.getCategory());
            documentHeader1.setYear(documentHeader.getYear());
            documentHeader1.setUpdatedOn(Helper.getCurrentTimeStamp());

            return documentHeaderRepository.save(documentHeader1);
        } else {
            throw new ResourceNotFoundException("DocumentHeader not found for ", "Id", id);
        }
    }

    @Override
    public void deleteByIdDocumentHeader(Integer id) {
     documentHeaderRepository.deleteById(id);
    }

    @Override
    public List<DocumentHeader> findAllDocumentHeader() {
        return documentHeaderRepository.findAll();
    }

    @Override
    public Optional<DocumentHeader> findDocumentHeaderById(Integer id) {
        return documentHeaderRepository.findById(id);
    }

    @Override
    public Optional<DocumentHeader> getApprovedDocumentById(Integer id) {
        return documentHeaderRepository.findByIdAndStatus(id,true);
    }

//    @Override
//    public DocumentHeader getApprovedDocumentById(Integer docId) {
//        DocumentHeader appovedDocumentById = documentHeaderRepository.findAppovedDocumentById(docId, 1);
//        return appovedDocumentById;
//    }


    @Override
    public List<DocumentHeader> getAllPendingStatus() {
        List<DocumentHeader> byIsApprovedFalse = documentHeaderRepository.findByIsApprovedFalse();
        return byIsApprovedFalse;
    }

    @Override
    public List<DocumentHeader> getAllApprovedStatus() {
        return documentHeaderRepository.findByIsApprovedTrue();
    }

    @Override
    public DocumentHeader updateStatus(Integer id, Boolean isApproved) {
        DocumentHeader documentHeader = documentHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader", "id", id));

        documentHeader.setApproved(isApproved);
        documentHeader.setUpdatedOn(Helper.getCurrentTimeStamp());
        return documentHeaderRepository.save(documentHeader);
    }
}
