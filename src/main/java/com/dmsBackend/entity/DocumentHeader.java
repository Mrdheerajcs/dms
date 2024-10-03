package com.dmsBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DocumentHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(name = "file_no", nullable = false)
    private String fileNo; // Make sure this is not nullable

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String version;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryMaster categoryMaster;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = false)
    private Employee employee;

    @ManyToOne     // New ADd for Admin
    @JoinColumn(name = "approval_status_by_id", referencedColumnName = "id", nullable = true)
    private Employee employeeBy;

    @OneToMany(mappedBy = "documentHeader", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("documentHeader")  // Ignore back reference to prevent infinite loop
    private List<DocumentDetails> documentDetails;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @Column(name = "approval_status_on" , nullable = true)  //New Add for Admin
    private Timestamp approvalStatusOn;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @Enumerated(EnumType.STRING)  // Use STRING for better readability in the database
    @Column(name = "approval_status", nullable = false)
    private DocApprovalStatus approvalStatus;

    @Column(name = "rejection_reason")
    private String rejectionReason;
}
