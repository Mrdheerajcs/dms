package com.dmsBackend.service.Impl;

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
    private DocumentHeaderRepository documentHeaderRepository;

    @Override
    public DocumentHeader createDocumentHeader(DocumentHeader documentHeader) {
        return documentHeaderRepository.save(documentHeader);
    }

    @Override
    public DocumentHeader updateDocumentHeader(DocumentHeader documentHeader, Integer id) {
        DocumentHeader existingDocumentHeader = documentHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader", "id", id));

        existingDocumentHeader.setTitle(documentHeader.getTitle());
        existingDocumentHeader.setFileNo(documentHeader.getFileNo());
        existingDocumentHeader.setSubject(documentHeader.getSubject());
        existingDocumentHeader.setVersion(documentHeader.getVersion());
        existingDocumentHeader.setType(documentHeader.getType());
        existingDocumentHeader.setEmployee(documentHeader.getEmployee());
        existingDocumentHeader.setDepartment(documentHeader.getDepartment());
        existingDocumentHeader.setBranch(documentHeader.getBranch());
        existingDocumentHeader.setCategory(documentHeader.getCategory());
        existingDocumentHeader.setYear(documentHeader.getYear());
        existingDocumentHeader.setUpdatedOn(Helper.getCurrentTimeStamp());

        return documentHeaderRepository.save(existingDocumentHeader);
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
        return documentHeaderRepository.findByIdAndStatus(id, true);
    }

    @Override
    public List<DocumentHeader> getAllPendingStatus() {
        return documentHeaderRepository.findByIsApprovedFalse();
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

    @Override
    public DocumentHeader updateActiveStatus(Integer id, Integer isActive) {
        DocumentHeader documentHeader = documentHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader", "id", id));

        documentHeader.setIsActive(isActive);
        documentHeader.setUpdatedOn(Helper.getCurrentTimeStamp());
        return documentHeaderRepository.save(documentHeader);
    }

}
