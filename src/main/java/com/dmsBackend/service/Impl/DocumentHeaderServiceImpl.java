package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.entity.DocApprovalStatus;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.exception.ResourceConflictException;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.repository.CategoryMasterRepository;
import com.dmsBackend.repository.DocumentHeaderRepository;
import com.dmsBackend.repository.EmployeeRepository;
import com.dmsBackend.service.DocumentHeaderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentHeaderServiceImpl implements DocumentHeaderService {

    private final DocumentHeaderRepository documentHeaderRepository;
    private final CategoryMasterRepository categoryMasterRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DocumentHeaderServiceImpl(DocumentHeaderRepository documentHeaderRepository,
                                     CategoryMasterRepository categoryMasterRepository,
                                     EmployeeRepository employeeRepository) {
        this.documentHeaderRepository = documentHeaderRepository;
        this.categoryMasterRepository = categoryMasterRepository;
        this.employeeRepository = employeeRepository;
    }


    // =============================================== Document Post, Put & Delete Operations =================================================== //

    //Save Document
    @Override
    public DocumentHeader saveDocumentHeader(DocumentHeader documentHeader) {
        Optional<DocumentHeader> existingDocument = documentHeaderRepository.findByFileNo(documentHeader.getFileNo());
        if (existingDocument.isPresent()) {
            throw new ResourceConflictException("Document with fileNo " + documentHeader.getFileNo() + " already exists");
        }

        if (documentHeader.getCategoryMaster() != null && documentHeader.getCategoryMaster().getId() != null) {
            CategoryMaster categoryMaster = categoryMasterRepository.findById(documentHeader.getCategoryMaster().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMaster not found with id " + documentHeader.getCategoryMaster().getId()));
            documentHeader.setCategoryMaster(categoryMaster);
        } else {
            throw new IllegalArgumentException("CategoryMaster ID must not be null");
        }

        Timestamp now = new Timestamp(System.currentTimeMillis());
        documentHeader.setCreatedOn(now);
        documentHeader.setUpdatedOn(now);
        documentHeader.setActive(true);
        documentHeader.setApprovalStatus(DocApprovalStatus.PENDING);
        documentHeader.setRejectionReason(null);
        documentHeader.setApprovalStatusOn(null);
        documentHeader.setEmployeeBy(null);

        return documentHeaderRepository.save(documentHeader);
    }

    //Update Document
//    @Override
//    public DocumentHeader updateDocumentHeader(Integer id, DocumentHeader updatedDocument) {
//        DocumentHeader existingDocument = documentHeaderRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader not found with id " + id));
//
//        existingDocument.setFileNo(updatedDocument.getFileNo());
//        existingDocument.setTitle(updatedDocument.getTitle());
//        existingDocument.setSubject(updatedDocument.getSubject());
//        existingDocument.setVersion(updatedDocument.getVersion());
//        existingDocument.setCategoryMaster(updatedDocument.getCategoryMaster());
//        existingDocument.setUpdatedOn(new Timestamp(System.currentTimeMillis()));
//
//        return documentHeaderRepository.save(existingDocument);
//    }


    @Override
    public DocumentHeader updateDocumentHeader(DocumentHeader documentHeader) {
        // Check if the document exists before updating
        DocumentHeader existingDocument = documentHeaderRepository.findById(documentHeader.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found with id " + documentHeader.getId()));

        if (documentHeader.getCreatedOn() == null) {
            documentHeader.setCreatedOn(Timestamp.from(Instant.now()));  // Setting createdOn to the current timestamp
        }

        // Always update the updatedOn field to the current timestamp
        documentHeader.setUpdatedOn(Timestamp.from(Instant.now()));

        // Check for null relations like categoryMaster, employee, etc.
        if (documentHeader.getCategoryMaster() == null) {
            throw new IllegalArgumentException("CategoryMaster is required");
        }
        // Check if category exists
        if (documentHeader.getCategoryMaster() != null && documentHeader.getCategoryMaster().getId() != null) {
            CategoryMaster categoryMaster = categoryMasterRepository.findById(documentHeader.getCategoryMaster().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("CategoryMaster not found with id " + documentHeader.getCategoryMaster().getId()));
            existingDocument.setCategoryMaster(categoryMaster);
        } else {
            throw new IllegalArgumentException("CategoryMaster ID must not be null");
        }

        // Update fields
        existingDocument.setFileNo(documentHeader.getFileNo());
        existingDocument.setTitle(documentHeader.getTitle());
        existingDocument.setSubject(documentHeader.getSubject());
        existingDocument.setVersion(documentHeader.getVersion());
        existingDocument.setUpdatedOn(new Timestamp(System.currentTimeMillis()));

        // Return the updated document
        return documentHeaderRepository.save(existingDocument);
    }

    //Update Document Active Status (Soft Delete)
    @Override
    public DocumentHeader updateActiveStatus(Integer id, boolean isActive) {
        DocumentHeader documentHeader = findDocumentHeaderById(id);
        documentHeader.setActive(isActive);
        documentHeader.setUpdatedOn(new Timestamp(System.currentTimeMillis()));

        return documentHeaderRepository.save(documentHeader);
    }

    //Update Document Approval Status (Pending, Reject, Approve)
    @Override
    public DocumentHeader updateApprovalStatus(Integer id, DocApprovalStatus status, String rejectionReason, Integer employeeId) {
        DocumentHeader documentHeader = findDocumentHeaderById(id);

        documentHeader.setApprovalStatus(status);

        if (status == DocApprovalStatus.REJECTED) {
            documentHeader.setRejectionReason(rejectionReason);
        } else {
            documentHeader.setRejectionReason(null);
        }

        if (status != null) {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

            documentHeader.setEmployeeBy(employee);
            documentHeader.setApprovalStatusOn(new Timestamp(System.currentTimeMillis()));
        }

        return documentHeaderRepository.save(documentHeader);
    }

    //Delete Document
    @Override
    public void deleteByIdDocumentHeader(Integer id) {
        if (!documentHeaderRepository.existsById(id)) {
            throw new ResourceNotFoundException("DocumentHeader not found with id " + id);
        }
        documentHeaderRepository.deleteById(id);
    }

    // ========================================================= Document Get Operations ========================================================= //

    //Find Document By Document Id
    @Override
    public DocumentHeader findDocumentHeaderById(Integer id) {
        return documentHeaderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader", "id", id));
    }


    //Find All Document
    @Override
    public List<DocumentHeader> findAllDocumentHeaders() {
        return documentHeaderRepository.findAll();
    }

    //Find All Approved Document
    @Override
    public List<DocumentHeader> getAllApproved() {
        return documentHeaderRepository.findAllByApprovalStatus(DocApprovalStatus.APPROVED);
    }

    //Find All Reject Document
    @Override
    public List<DocumentHeader> getAllRejected() {
        return documentHeaderRepository.findAllByApprovalStatus(DocApprovalStatus.REJECTED);
    }

    //Find All Pending Document
    @Override
    public List<DocumentHeader> getAllPending() {
        return documentHeaderRepository.findAllByApprovalStatus(DocApprovalStatus.PENDING);
    }

    //========================================== USER ==========================================

    //Find All Document For User
    @Override
    public List<DocumentHeader> findAllDocumentHeadersByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeId(employeeId);
    }

    //Find All Approved Document For User
    @Override
    public List<DocumentHeader> getAllApprovedByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeIdAndApprovalStatus(employeeId, DocApprovalStatus.APPROVED);
    }

    //Find All Reject Document For User
    @Override
    public List<DocumentHeader> getAllRejectedByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeIdAndApprovalStatus(employeeId, DocApprovalStatus.REJECTED);
    }

    //Find All Pending Document For User
    @Override
    public List<DocumentHeader> getAllPendingByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeIdAndApprovalStatus(employeeId, DocApprovalStatus.PENDING);
    }

    //========================================== ADMIN ==========================================

    @Override
    public List<DocumentHeader> findAllRejectedByActionEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeByAndApprovalStatus(employeeId, DocApprovalStatus.REJECTED);
    }

    @Override
    public List<DocumentHeader> findAllApprovedByActionEmployeeId(Integer employeeId) {
        return documentHeaderRepository.findAllByEmployeeByAndApprovalStatus(employeeId, DocApprovalStatus.APPROVED);
    }

    // ========================================================= Document Count Operations ========================================================= //

    //Count All Approved Document
    @Override
    public long countApprovedDocuments() {
        return documentHeaderRepository.countByApprovalStatus(DocApprovalStatus.APPROVED);
    }

    //Count All Reject Document
    @Override
    public long countRejectedDocuments() {
        return documentHeaderRepository.countByApprovalStatus(DocApprovalStatus.REJECTED);
    }

    //Count All Pending Document
    @Override
    public long countPendingDocuments() {
        return documentHeaderRepository.countByApprovalStatus(DocApprovalStatus.PENDING);
    }

    //========================================== USER ==========================================

    //Count All Document By Employee ID (User)
    @Override
    public long countDocumentHeadersByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByEmployeeId(employeeId);
    }

    //Count All Approve Document By Employee ID (User)
    @Override
    public long countApprovedDocumentsByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByApprovalStatusAndEmployeeId(DocApprovalStatus.APPROVED, employeeId);
    }

    //Count All Reject Document By Employee ID (User)
    @Override
    public long countRejectedDocumentsByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByApprovalStatusAndEmployeeId(DocApprovalStatus.REJECTED, employeeId);
    }

    //Count All Pending Document By Employee ID (User)
    @Override
    public long countPendingDocumentsByEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByApprovalStatusAndEmployeeId(DocApprovalStatus.PENDING, employeeId);
    }

    //========================================== ADMIN ==========================================


    @Override
    public long countRejectedByActionEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByEmployeeByAndApprovalStatus(employeeId, DocApprovalStatus.REJECTED);
    }

    @Override
    public long countApprovedByActionEmployeeId(Integer employeeId) {
        return documentHeaderRepository.countByEmployeeByAndApprovalStatus(employeeId, DocApprovalStatus.APPROVED);
    }


    // =================================================== Document Count For Graph Operations =================================================== //

    @Override
    public Map<String, Object> countAllDocumentsByIdWithMonth(Integer employeeId, Timestamp startDate, Timestamp endDate) {
        List<DocumentHeader> approvedDocuments = documentHeaderRepository.findAllByEmployeeIdAndApprovalStatusAndUpdatedOnBetween(
                employeeId, DocApprovalStatus.APPROVED, startDate, endDate);

        List<DocumentHeader> rejectedDocuments = documentHeaderRepository.findAllByEmployeeIdAndApprovalStatusAndUpdatedOnBetween(
                employeeId, DocApprovalStatus.REJECTED, startDate, endDate);

        Map<String, Object> approvedDocumentsMap = groupDocumentsByMonth(approvedDocuments);
        Map<String, Object> rejectedDocumentsMap = groupDocumentsByMonth(rejectedDocuments);

        Map<String, Object> response = new HashMap<>();
        response.put("approvedDocuments", approvedDocumentsMap.get("totalDocumentsByMonth"));
        response.put("rejectedDocuments", rejectedDocumentsMap.get("totalDocumentsByMonth"));
        response.put("months", approvedDocumentsMap.get("months")); // both maps will have the same months list

        return response;
    }

    private Map<String, Object> groupDocumentsByMonth(List<DocumentHeader> documents) {
        // List of all 12 months in order
        List<String> allMonths = Arrays.asList("JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                "JUL", "AUG", "SEP", "OCT", "NOV", "DEC");

        // Count documents grouped by month
        Map<String, Long> monthCountMap = documents.stream()
                .collect(Collectors.groupingBy(doc -> {
                    LocalDateTime updatedOn = doc.getUpdatedOn().toLocalDateTime();
                    return updatedOn.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH).toUpperCase(); // ensure uppercase
                }, Collectors.counting()));

        // Prepare result lists
        List<Long> totalDocumentsByMonth = new ArrayList<>();

        // Ensure all months are present with their counts (or zero if absent)
        for (String month : allMonths) {
            totalDocumentsByMonth.add(monthCountMap.getOrDefault(month, 0L));
        }

        Map<String, Object> groupedResult = new HashMap<>();
        groupedResult.put("months", allMonths); // all 12 months
        groupedResult.put("totalDocumentsByMonth", totalDocumentsByMonth);

        return groupedResult;
    }

    @Override
    public Map<String, Object> getApprovalSummaryByEmployeeId(Integer employeeId) {
        List<Object[]> rejectedCounts = documentHeaderRepository.countByEmployeeAndApprovalStatusGroupedByMonth(employeeId, DocApprovalStatus.REJECTED);
        List<Object[]> approvedCounts = documentHeaderRepository.countByEmployeeAndApprovalStatusGroupedByMonth(employeeId, DocApprovalStatus.APPROVED);

        // Initialize month names
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

        // Initialize document counts for each month (12 months)
        int[] rejectedDocuments = new int[12];
        int[] approvedDocuments = new int[12];

        // Fill rejected document counts based on query results
        for (Object[] row : rejectedCounts) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            rejectedDocuments[month - 1] = count.intValue();  // month - 1 to convert to 0-based index
        }

        // Fill approved document counts based on query results
        for (Object[] row : approvedCounts) {
            Integer month = (Integer) row[0];
            Long count = (Long) row[1];
            approvedDocuments[month - 1] = count.intValue();  // month - 1 to convert to 0-based index
        }

        // Construct the response map
        Map<String, Object> response = new HashMap<>();
        response.put("months", months);
        response.put("rejectedDocuments", rejectedDocuments);
        response.put("approvedDocuments", approvedDocuments);

        return response;
    }


//    @Override
//    public DocumentHeader getDocumentHeaderById(Integer headerId) {
//        return documentHeaderRepository.findById(headerId)
//                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader not found for id: " + headerId));
//    }


}
