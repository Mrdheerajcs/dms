package com.dmsBackend.controller;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.DocumentHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/DocumentHeader")
@CrossOrigin("http://localhost:3000")
public class DocumentHeaderController {

    @Autowired
    DocumentHeaderService documentHeaderService;

    @PostMapping("/save")
    public ResponseEntity<DocumentHeader> createDocumentHeader(@RequestBody DocumentHeader documentHeader) {
        DocumentHeader createdDocumentHeader = documentHeaderService.createDocumentHeader(documentHeader);
        return new ResponseEntity(createdDocumentHeader, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DocumentHeader> updateDocumentHeader(@PathVariable Integer id, @RequestBody DocumentHeader documentHeader) {
        try {
            DocumentHeader updateddocumentheader = this.documentHeaderService.updateDocumentHeader(documentHeader, id);
            return new ResponseEntity<>(updateddocumentheader, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DocumentHeader> deletebyIdDocumentHeader(@PathVariable Integer id) {
        this.documentHeaderService.deleteByIdDocumentHeader(id);
        return new ResponseEntity(new ApiResponse("DocumentHeader deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DocumentHeader>> findAllDocumentHeader() {
        List<DocumentHeader> allDocumentHeader = this.documentHeaderService.findAllDocumentHeader();
        return new ResponseEntity(allDocumentHeader, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<DocumentHeader> findByIdDocumentDetails(@PathVariable Integer id) {
        Optional<DocumentHeader> documentHeader1 = documentHeaderService.findDocumentHeaderById(id);
        return documentHeader1.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/doc/{id}")
    public ResponseEntity<DocumentHeader> getApprovedDocStatusById(@PathVariable Integer id) {
        Optional<DocumentHeader> approvedDocumentById = documentHeaderService.getApprovedDocumentById(id);
        return approvedDocumentById.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("updatestatus/{id}")
    public ResponseEntity<DocumentHeader> updateDocumentStatus(@PathVariable Integer id, @RequestBody DocumentHeader documentHeader) {
        try {
            DocumentHeader documentHeader1 = documentHeaderService.updateStatus(id,documentHeader.isApproved());
            return new ResponseEntity<>(documentHeader1, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllPendingStatus")
    public ResponseEntity<List<DocumentHeader>> pendingStatus() {
        List<DocumentHeader> allPendingStatus = this.documentHeaderService.getAllPendingStatus();
        return new ResponseEntity(allPendingStatus, HttpStatus.OK);
    }

    @GetMapping("/getAllApprovedStatus")
    public ResponseEntity<List<DocumentHeader>> ApprovedStatus() {
        List<DocumentHeader> allApprovedStatus = this.documentHeaderService.getAllApprovedStatus();
        return new ResponseEntity(allApprovedStatus, HttpStatus.OK);
    }
}
