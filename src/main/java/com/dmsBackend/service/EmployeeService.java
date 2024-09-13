package com.dmsBackend.service;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeType;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee save(Employee employee);
    Employee findByEmail(String email);
    void deleteByIdEmployee(Integer id);
    List<Employee> findAllEmployee();
    Optional<Employee> findEmployeeById(Integer id);
    Employee findByIdEmp(Integer id);
    void updateEmployeeStatus(Integer id, Integer isActive);
    Employee updateEmployeeType(Integer id, EmployeeType employeeType); // New method to update employee type
}
