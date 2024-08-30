package com.dmsBackend.controller;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.DocumentHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/DocumentHeader")
@CrossOrigin(origins = "http://localhost:3000")
public class DocumentHeaderController {

    @Autowired
    private DocumentHeaderService documentHeaderService;

    @PostMapping("/save")
    public ResponseEntity<DocumentHeader> createDocumentHeader(@RequestBody DocumentHeader documentHeader) {
        DocumentHeader createdDocumentHeader = documentHeaderService.createDocumentHeader(documentHeader);
        return new ResponseEntity<>(createdDocumentHeader, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DocumentHeader> updateDocumentHeader(@PathVariable Integer id, @RequestBody DocumentHeader documentHeader) {
        try {
            DocumentHeader updatedDocumentHeader = documentHeaderService.updateDocumentHeader(documentHeader, id);
            return new ResponseEntity<>(updatedDocumentHeader, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteByIdDocumentHeader(@PathVariable Integer id) {
        documentHeaderService.deleteByIdDocumentHeader(id);
        return new ResponseEntity<>(new ApiResponse("DocumentHeader deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DocumentHeader>> findAllDocumentHeader() {
        List<DocumentHeader> allDocumentHeader = documentHeaderService.findAllDocumentHeader();
        return new ResponseEntity<>(allDocumentHeader, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<DocumentHeader> findByIdDocumentHeader(@PathVariable Integer id) {
        return documentHeaderService.findDocumentHeaderById(id)
                .map(documentHeader -> new ResponseEntity<>(documentHeader, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/doc/{id}")
    public ResponseEntity<DocumentHeader> getApprovedDocStatusById(@PathVariable Integer id) {
        return documentHeaderService.getApprovedDocumentById(id)
                .map(documentHeader -> new ResponseEntity<>(documentHeader, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updatestatus/{id}")
    public ResponseEntity<DocumentHeader> updateDocumentStatus(@PathVariable Integer id, @RequestBody DocumentHeader documentHeader) {
        try {
            DocumentHeader updatedDocumentHeader = documentHeaderService.updateStatus(id, documentHeader.isApproved());
            return new ResponseEntity<>(updatedDocumentHeader, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllPendingStatus")
    public ResponseEntity<List<DocumentHeader>> getAllPendingStatus() {
        List<DocumentHeader> allPendingStatus = documentHeaderService.getAllPendingStatus();
        return new ResponseEntity<>(allPendingStatus, HttpStatus.OK);
    }

    @GetMapping("/getAllApprovedStatus")
    public ResponseEntity<List<DocumentHeader>> getAllApprovedStatus() {
        List<DocumentHeader> allApprovedStatus = documentHeaderService.getAllApprovedStatus();
        return new ResponseEntity<>(allApprovedStatus, HttpStatus.OK);
    }

    @PutMapping("update/status/active/{id}")
    public ResponseEntity<DocumentHeader> updateRoleStatus(@PathVariable Integer id, @RequestBody DocumentHeader documentHeader) {
        try {
            DocumentHeader documentHeaders1 = documentHeaderService.updateActiveStatus(id,documentHeader.getIsActive());
            return new ResponseEntity<>(documentHeaders1, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
