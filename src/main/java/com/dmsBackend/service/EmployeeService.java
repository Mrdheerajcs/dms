package com.dmsBackend.service;

import com.dmsBackend.entity.Employee;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee findByEmail(String email);
    Employee findById(Integer id);
    void deleteByIdEmployee(Integer id);
    List<Employee> findAllEmployee();
    void updateEmployeeStatus(Integer id, boolean isActive);
    void updateEmployeeRole(String email, Integer roleId);

    // New methods
    List<Employee> getEmployeesByRoleIsNullById(Integer id);
    List<Employee> findEmployeesByRole(String roleName);
    List<Employee> getEmployeesByRoleIsNull();
    List<Employee> getAllWithoutNullRole();
    long countEmployeesByRoleNull();
    long countEmployeesByRoleNotNull();
    long countEmployeesByRole(String roleName);
}
