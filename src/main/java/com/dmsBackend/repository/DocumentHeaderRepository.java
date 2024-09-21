package com.dmsBackend.repository;

import com.dmsBackend.entity.DocApprovalStatus;
import com.dmsBackend.entity.DocumentHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentHeaderRepository extends JpaRepository<DocumentHeader, Integer> {

    // Find document by file number
    Optional<DocumentHeader> findByFileNo(String fileNo);

    // Find all documents by approval status
    List<DocumentHeader> findAllByApprovalStatus(DocApprovalStatus approvalStatus);

    List<DocumentHeader> findAllByEmployeeId(Integer employeeId);  // Get all documents for an employee

    // Find all documents by employee ID and approval status
    List<DocumentHeader> findAllByEmployeeIdAndApprovalStatus(Integer employeeId, DocApprovalStatus approvalStatus);

    // Find documents by employee ID, approval status, and a date range
    List<DocumentHeader> findAllByEmployeeIdAndApprovalStatusAndUpdatedOnBetween(
            Integer employeeId, DocApprovalStatus approvalStatus, Timestamp startDate, Timestamp endDate
    );



    // Count documents by approval status
    long countByApprovalStatus(DocApprovalStatus approvalStatus);

    // Count documents by employee ID and approval status
    long countByApprovalStatusAndEmployeeId(DocApprovalStatus approvalStatus, Integer employeeId);

    // Count total documents for a specific employee
    long countByEmployeeId(Integer employeeId);



    // Custom queries to count approved/rejected documents by employee who approved/rejected
    @Query("SELECT COUNT(d) FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus")
    long countByEmployeeByAndApprovalStatus(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    // Find all approved/rejected documents handled by a specific employee
    @Query("SELECT d FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus")
    List<DocumentHeader> findAllByEmployeeByAndApprovalStatus(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);

    // graph cout

    @Query("SELECT MONTH(d.createdOn), COUNT(d) FROM DocumentHeader d WHERE d.employeeBy.id = :employeeId AND d.approvalStatus = :approvalStatus GROUP BY MONTH(d.createdOn)")
    List<Object[]> countByEmployeeAndApprovalStatusGroupedByMonth(
            @Param("employeeId") Integer employeeId,
            @Param("approvalStatus") DocApprovalStatus approvalStatus);


}
