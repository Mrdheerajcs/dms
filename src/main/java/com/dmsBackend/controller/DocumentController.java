package com.dmsBackend.controller;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.DocApprovalStatus;
import com.dmsBackend.entity.DocumentHeader;
import java.sql.Timestamp;

import com.dmsBackend.exception.ResourceConflictException;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.repository.CategoryMasterRepository;
import com.dmsBackend.response.DocumentSaveRequest;
import com.dmsBackend.service.DocumentDetailsService;
import com.dmsBackend.service.DocumentHeaderService;
import com.dmsBackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentHeaderService documentHeaderService;

    @Autowired
    private DocumentDetailsService documentDetailsService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CategoryMasterRepository categoryMasterRepository;

    // ================== DocumentHeader Operations ================== //

    // Create a new DocumentHeader
//    @PostMapping("/save")
//    public ResponseEntity<?> saveDocumentHeader(
//            @RequestBody DocumentHeader documentHeader,
//            @RequestParam("filePaths") List<String> filePaths) {
//        try {
//            // Save the document header
//            DocumentHeader savedDocumentHeader = documentHeaderService.saveDocumentHeader(documentHeader);
//
//            // Associate and save file details
//            documentDetailsService.saveFileDetails(savedDocumentHeader, filePaths);
//
//            return ResponseEntity.ok("Document saved successfully with files");
//        } catch (ResourceConflictException e) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the document");
//        }
//    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDocumentWithFiles(
            @RequestBody DocumentSaveRequest documentSaveRequest) { // Expect the full payload as a single object
        try {
            // Extract DocumentHeader and file paths from the request
            DocumentHeader documentHeader = documentSaveRequest.getDocumentHeader();
            List<String> filePaths = documentSaveRequest.getFilePaths();

            // Save DocumentHeader
            DocumentHeader savedDocumentHeader = documentHeaderService.saveDocumentHeader(documentHeader);

            // Save file details associated with the saved document header
            documentDetailsService.saveFileDetails(savedDocumentHeader, filePaths);

            return ResponseEntity.ok("Document and files saved successfully");

        } catch (ResourceConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving document and files");
        }
    }



    // Update an existing DocumentHeader
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDocumentHeader(@PathVariable Integer id,
                                                  @RequestBody DocumentHeader updatedDocument) {
        try {
            DocumentHeader documentHeader = documentHeaderService.updateDocumentHeader(id, updatedDocument);
            return ResponseEntity.ok(documentHeader);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()); // Return 404 if not found
        }
    }

    //get all enum
    @GetMapping("/docApprovalStatuses")
    public List<Map<String, String>> getAllDocApprovalStatuses() {
        return Arrays.stream(DocApprovalStatus.values())
                .map(status -> Map.of(status.name(), ""))
                .collect(Collectors.toList());
    }

    // Find a document by ID
    @GetMapping("/{id}")
    public ResponseEntity<DocumentHeader> getDocumentHeaderById(@PathVariable Integer id) {
        DocumentHeader documentHeader = documentHeaderService.findDocumentHeaderById(id);
        return new ResponseEntity<>(documentHeader, HttpStatus.OK);
    }

    // Find all documents
    @GetMapping
    public ResponseEntity<List<DocumentHeader>> getAllDocumentHeaders() {
        List<DocumentHeader> documentHeaders = documentHeaderService.findAllDocumentHeaders();
        return new ResponseEntity<>(documentHeaders, HttpStatus.OK);
    }

    // Delete a document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentHeader(@PathVariable Integer id) {
        documentHeaderService.deleteByIdDocumentHeader(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update the approval status of a document
    @PatchMapping("/{id}/approval-status")
    public ResponseEntity<DocumentHeader> updateApprovalStatus(
            @PathVariable Integer id,
            @RequestParam("status") DocApprovalStatus status,
            @RequestParam(value = "rejectionReason", required = false) String rejectionReason,
            @RequestHeader("employeeId") Integer employeeId) {  // Extract employeeId from header

        // Call the service method to update approval status
        DocumentHeader updatedDocument = documentHeaderService.updateApprovalStatus(id, status, rejectionReason, employeeId);

        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }


    // Update the active status of a document
    @PatchMapping("/{id}/active-status")
    public ResponseEntity<DocumentHeader> updateActiveStatus(
            @PathVariable Integer id, @RequestParam("isActive") boolean isActive) {
        DocumentHeader updatedDocument = documentHeaderService.updateActiveStatus(id, isActive);
        return new ResponseEntity<>(updatedDocument, HttpStatus.OK);
    }

    // Get all approved documents
    @GetMapping("/approved")
    public ResponseEntity<List<DocumentHeader>> getAllApproved() {
        List<DocumentHeader> approvedDocuments = documentHeaderService.getAllApproved();
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }

    // Get all rejected documents
    @GetMapping("/rejected")
    public ResponseEntity<List<DocumentHeader>> getAllRejected() {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.getAllRejected();
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all pending documents
    @GetMapping("/pending")
    public ResponseEntity<List<DocumentHeader>> getAllPending() {
        List<DocumentHeader> pendingDocuments = documentHeaderService.getAllPending();
        return new ResponseEntity<>(pendingDocuments, HttpStatus.OK);
    }

    // Get all approved documents for a specific employee
    @GetMapping("/approved/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllApprovedByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> approvedDocuments = documentHeaderService.getAllApprovedByEmployeeId(employeeId);
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }

    // Get all rejected documents for a specific employee
    @GetMapping("/rejected/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllRejectedByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.getAllRejectedByEmployeeId(employeeId);
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all pending documents for a specific employee
    @GetMapping("/pending/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllPendingByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> pendingDocuments = documentHeaderService.getAllPendingByEmployeeId(employeeId);
        return new ResponseEntity<>(pendingDocuments, HttpStatus.OK);
    }

    // Get all documents for a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<DocumentHeader>> getAllDocumentHeadersByEmployeeId(@PathVariable Integer employeeId) {
        List<DocumentHeader> employeeDocuments = documentHeaderService.findAllDocumentHeadersByEmployeeId(employeeId);
        return new ResponseEntity<>(employeeDocuments, HttpStatus.OK);
    }

    // Get all rejected documents updated by an employee
    @GetMapping("/rejectedByEmp")
    public ResponseEntity<List<DocumentHeader>> getRejectedByEmployee(@RequestHeader("employeeId") Integer employeeId) {
        List<DocumentHeader> rejectedDocuments = documentHeaderService.findAllRejectedByActionEmployeeId(employeeId);
        return new ResponseEntity<>(rejectedDocuments, HttpStatus.OK);
    }

    // Get all approved documents updated by an employee
    @GetMapping("/approvedByEmp")
    public ResponseEntity<List<DocumentHeader>> getApprovedByEmployee(@RequestHeader("employeeId") Integer employeeId) {
        List<DocumentHeader> approvedDocuments = documentHeaderService.findAllApprovedByActionEmployeeId(employeeId);
        return new ResponseEntity<>(approvedDocuments, HttpStatus.OK);
    }


    //for graph
    @GetMapping("/documents-summary/{employeeId}")
    public ResponseEntity<Map<String, Object>> getDocumentsSummaryByEmployeeId(
            @PathVariable Integer employeeId,
            @RequestParam("startDate") Timestamp startDate,
            @RequestParam("endDate") Timestamp endDate) {
        Map<String, Object> result = documentHeaderService.countAllDocumentsByIdWithMonth(employeeId, startDate, endDate);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/document/summary/by/{employeeId}")
    public ResponseEntity<Map<String, Object>> getDocumentSummaryByEmployee(@PathVariable Integer employeeId) {
        Map<String, Object> summary = documentHeaderService.getApprovalSummaryByEmployeeId(employeeId);
        return ResponseEntity.ok(summary);
    }


    // ================== DocumentDetails (File Upload) Operations ================== //

//    @PostMapping("/upload")
//    public ResponseEntity<List<String>> uploadFiles(
//            @RequestParam("files") List<MultipartFile> files,
//            @RequestParam("category") String category) { // Add category parameter
//        try {
//            // Call service method to upload files with the category
//            List<String> filePaths = documentDetailsService.uploadFiles(files, category);
//            return ResponseEntity.ok(filePaths);  // Return the file paths
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonList(e.getMessage()));
//        }
//    }


    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("category") String category) {  // Add category parameter back
        try {
            // Call the service method to upload files with the category
            List<String> filePaths = documentDetailsService.uploadFiles(files, category);
            return ResponseEntity.ok(filePaths);  // Return the file paths
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonList("Error uploading files: " + e.getMessage()));
        }
    }


    // You can add more endpoints for retrieving DocumentDetails if needed
}
