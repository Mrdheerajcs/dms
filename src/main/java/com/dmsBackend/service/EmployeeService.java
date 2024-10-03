package com.dmsBackend.service;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.Employee;
import jakarta.transaction.Transactional;

import java.util.List;

public interface EmployeeService {
    @Transactional
    Employee create(Employee employee);

    Employee save(Employee employee);
    Employee findByEmail(String email);
    Employee findById(Integer id);
    void deleteByIdEmployee(Integer id);
    List<Employee> findAllEmployee();
    void updateEmployeeStatus(Integer id, boolean isActive);
    void updateEmployeeRoleByEmail(String email, Integer roleId);

    void updateEmployeeRoleById(Integer id, Integer roleId);

    List<Employee> findEmployeesByBranch(BranchMaster branch);

    // New methods
    List<Employee> getEmployeesByRoleIsNullById(Integer id);
    List<Employee> findEmployeesByRole(String roleName);
    List<Employee> getEmployeesByRoleIsNull();
    List<Employee> getAllWithoutNullRole();
    long countEmployeesByRoleNull();
    long countEmployeesByRoleNotNull();
    long countEmployeesByRole(String roleName);
}
