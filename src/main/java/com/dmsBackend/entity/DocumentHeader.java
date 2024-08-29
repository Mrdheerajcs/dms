package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Data
public class DocumentHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "fileNo")
    private String fileNo;

    @Column(name = "subject")
    private String subject;

    @Column(name = "version")
    private String version;

    @Column(name="createdOn")
    private Timestamp createdOn;
    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    @Column(name = "is_Approved")
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "category_master_id")
    private CategoryMaster category;

    @ManyToOne
    @JoinColumn(name = "year_master_id")
    private YearMaster year;

    @ManyToOne
    @JoinColumn(name = "type_master_id")
    private TypeMaster type;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "Employee_department_master_Id")
    private DepartmentMaster department;

    @ManyToOne
    @JoinColumn(name = "Employee_department_master_branch_Master_Id")
    private BranchMaster branch;



}
