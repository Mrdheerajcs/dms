package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.*;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.BranchMasterRepository;
import com.dmsBackend.repository.DepartmentMasterRepository;
import com.dmsBackend.repository.EmployeeHasRoleMasterRepository;
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
    private BranchMasterRepository branchMasterRepository;

    @Autowired
    private DepartmentMasterRepository departmentMasterRepository;

    @Autowired
    private EmployeeHasRoleMasterRepository employeeHasRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeIdGenerator employeeIdGenerator;

    @Override
    @Transactional
    public Employee save(Employee employee) {
        // Set timestamps
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        //employee.setEmployeeId(employeeIdGenerator.generateEmployeeId());

//        // Find and set branch
//        BranchMaster branchMaster = branchMasterRepository.findById(employee.getBranch().getId())
//                .orElseThrow(() -> new RuntimeException("Branch Not Found"));
//        employee.setBranch(branchMaster);
//
//        // Find and set department
//        DepartmentMaster department = departmentMasterRepository.findById(employee.getDepartment().getId())
//                .orElseThrow(() -> new RuntimeException("Department Not Found"));
//        employee.setDepartment(department);

        // Encode the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Save the role relationships
//        for (RoleMaster role : employee.getRoles()) {
//            EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
//            employeeHasRole.setEmployee(savedEmployee);
//            employeeHasRole.setRole(role);
//            employeeHasRole.setBranch(branchMaster);
//            employeeHasRole.setDepartment(department);
//            employeeHasRoleRepository.save(employeeHasRole);
//        }

        return savedEmployee;
    }

    @Override
    public Employee findByEmail(String email)
    {
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
        return employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("employee not found","Id",id));
    }

    @Override
    public void updateEmployeeStatus(Integer id, Integer isActive) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("employee", "id", id));

        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
        employee.setIsActive(isActive);
         employeeRepository.save(employee);
    }
}
