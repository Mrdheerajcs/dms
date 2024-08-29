package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class DocumentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String path;

    @Column(name="createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    @ManyToOne
    @JoinColumn(name = "document_header_id")
    private DocumentHeader documentHeader;

    @ManyToOne
    @JoinColumn(name = "document_header_category_master_id")
    private CategoryMaster category;

    @ManyToOne
    @JoinColumn(name = "document_header_year_master_id")
    private YearMaster year;

    @ManyToOne
    @JoinColumn(name = "document_header_type_master_id")
    private TypeMaster type;

    @ManyToOne
    @JoinColumn(name = "document_header_Employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "document_header_Employee_department_master_Id")
    private DepartmentMaster department;

    @ManyToOne
    @JoinColumn(name = "document_header_Employee_department_master_branch_Master_Id")
    private BranchMaster branch;
}
