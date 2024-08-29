package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class EmployeeHasRoleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "Employee_department_master_Id", nullable = false)
    private DepartmentMaster department;

    @ManyToOne
    @JoinColumn(name = "Employee_department_master_branch_Master_Id", nullable = false)
    private BranchMaster branch;


    @ManyToOne
    @JoinColumn(name = "role_master_id", nullable = false)
    private RoleMaster role;

}
