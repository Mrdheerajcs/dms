package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.*;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.EmployeeRepository;
import com.dmsBackend.service.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Employee save(Employee employee) {
        // Check if email already exists
        if (employeeRepository.findByEmail(employee.getEmail()) != null) {
            throw new RuntimeException("Email is already in use.");
        }

        // Ensure that the password is set before encoding
        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            throw new RuntimeException("Password must not be null or empty.");
        }

        // Set timestamps
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Encode the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Ensure that department and branch are set (optional)
        if (employee.getDepartment() == null) {
            throw new RuntimeException("Department must not be null.");
        }
        if (employee.getBranch() == null) {
            throw new RuntimeException("Branch must not be null.");
        }

        // Save the employee
        return employeeRepository.save(employee);
    }


    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    @Override
    public void deleteByIdEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> findAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> findEmployeeById(Integer id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee findByIdEmp(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("employee not found", "Id", id));
    }

    @Override
    public void updateEmployeeStatus(Integer id, Integer isActive) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("employee", "id", id));

        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        employee.setIsActive(isActive);
        employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployeeType(Integer id, EmployeeType employeeType) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("employee", "id", id));

        employee.setEmployeeType(employeeType);
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        return employeeRepository.save(employee); // Return the updated employee
    }

}
