package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.EmployeeRepository;
import com.dmsBackend.repository.RoleMasterRepository;
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

    @Autowired
    private RoleMasterRepository roleMasterRepository;

    @Override
    @Transactional
    public Employee save(Employee employee) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (existingEmployee.isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }

        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
            throw new RuntimeException("Password must not be null or empty.");
        }
        if (employee.getDepartment() == null) {
            throw new RuntimeException("Department must not be null.");
        }
        if (employee.getBranch() == null) {
            throw new RuntimeException("Branch must not be null.");
        }

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        employee.setRole(null); // Ensure role is null during the creation process

        return employeeRepository.save(employee);
    }

    @Override
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found with email: " + email));
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
    public Employee findById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found", "Id", id));
    }

    @Override
    public void updateEmployeeStatus(Integer id, boolean isActive) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
        employee.setActive(isActive);
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public void updateEmployeeRole(String email, Integer roleId) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee with email " + email + " not found.");
        }

        Employee employee = optionalEmployee.get();
        Optional<RoleMaster> optionalRole = roleMasterRepository.findById(roleId);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role with ID " + roleId + " not found.");
        }

        // Assign the role to the employee and update the employee
        employee.setRole(optionalRole.get());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Save the updated employee with the new role and return it
        employeeRepository.save(employee);
    }

    // New methods
    @Override
    public List<Employee> getEmployeesByRoleIsNullById(Integer id) {
        return employeeRepository.findByIdAndRoleIsNull(id);
    }

    @Override
    public List<Employee> getEmployeesByRoleIsNull() {
        return employeeRepository.findByRoleIsNull();
    }

    @Override
    public List<Employee> getAllWithoutNullRole() {
        return employeeRepository.findAllByRoleIsNotNull();
    }

    @Override
    public long countEmployeesByRoleNull() {
        return employeeRepository.countByRoleIsNull();
    }

    @Override
    public long countEmployeesByRoleNotNull() {
        return employeeRepository.countByRoleIsNotNull();
    }

    @Override
    public long countEmployeesByRole(String roleName) {
        RoleMaster role = roleMasterRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        return employeeRepository.countByRole(role);
    }

    @Override
    public List<Employee> findEmployeesByRole(String roleName) {
        RoleMaster role = roleMasterRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
        return employeeRepository.findByRole(role);
    }
}
