package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.BranchMasterRepository;
import com.dmsBackend.repository.DepartmentMasterRepository;
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

    @Autowired
    private DepartmentMasterRepository departmentMasterRepository;
    @Autowired
    private BranchMasterRepository branchMasterRepository;

//    @Override
//    @Transactional
//    public Employee save(Employee employee) {
//        Optional<Employee> existingEmployee = employeeRepository.findByEmail(employee.getEmail());
//
//        if (existingEmployee.isPresent()) {
//            throw new RuntimeException("Email is already in use.");
//        }
//
//        if (employee.getPassword() == null || employee.getPassword().isEmpty()) {
//            throw new RuntimeException("Password must not be null or empty.");
//        }
//        if (employee.getDepartment() == null) {
//            throw new RuntimeException("Department must not be null.");
//        }
//        if (employee.getBranch() == null) {
//            throw new RuntimeException("Branch must not be null.");
//        }
//
//        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
//        employee.setCreatedOn(Helper.getCurrentTimeStamp());
//        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
//        employee.setRole(null); // Ensure role is null during the creation process
//
//        return employeeRepository.save(employee);
//    }


    //create branch admin by system admin
    @Override
    @Transactional
    public Employee create(Employee employee) {
        // Set timestamps
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Generate and set employee ID if needed
        // employee.setEmployeeId(employeeIdGenerator.generateEmployeeId());

        // Find and set branch
        if (employee.getBranch() != null && employee.getBranch().getId() != null) {
            BranchMaster branchMaster = branchMasterRepository.findById(employee.getBranch().getId())
                    .orElseThrow(() -> new RuntimeException("Branch Not Found"));
            employee.setBranch(branchMaster);
        }

        employee.setActive(true);

        // Encode the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        return savedEmployee;
    }

    @Override
    @Transactional
    public Employee save(Employee employee) {
        // Set timestamps
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Generate and set employee ID if needed
        // employee.setEmployeeId(employeeIdGenerator.generateEmployeeId());

        // Find and set branch
        if (employee.getBranch() != null && employee.getBranch().getId() != null) {
            BranchMaster branchMaster = branchMasterRepository.findById(employee.getBranch().getId())
                    .orElseThrow(() -> new RuntimeException("Branch Not Found"));
            employee.setBranch(branchMaster);
        }

        // Find and set department
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            DepartmentMaster department = departmentMasterRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new RuntimeException("Department Not Found"));
            employee.setDepartment(department);
        }

        employee.setActive(true);

        // Encode the password before saving
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        // Save the employee
        Employee savedEmployee = employeeRepository.save(employee);

        // Save the role relationships if needed
        // for (RoleMaster role : employee.getRoles()) {
        //     EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
        //     employeeHasRole.setEmployee(savedEmployee);
        //     employeeHasRole.setRole(role);
        //     employeeHasRole.setBranch(branchMaster);
        //     employeeHasRole.setDepartment(department);
        //     employeeHasRoleRepository.save(employeeHasRole);
        // }

        return savedEmployee;
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

//    @Override
//    public void updateEmployeeRoleById(Integer id, Integer roleId) {
//        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
//
//        if (optionalEmployee.isEmpty()) {
//            throw new RuntimeException("Employee with ID " + id + " not found.");
//        }
//
//        Employee employee = optionalEmployee.get();
//        Optional<RoleMaster> optionalRole = roleMasterRepository.findById(roleId);
//
//        if (optionalRole.isEmpty()) {
//            throw new RuntimeException("Role with ID " + roleId + " not found.");
//        }
//
//        // Assign the role to the employee and update the employee
//        employee.setRole(optionalRole.get());
//        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
//
//        // Save the updated employee with the new role
//        employeeRepository.save(employee);
//    }
//
//    @Override
//    public void updateEmployeeRoleByEmail(String email, Integer roleId) {
//        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);
//
//        if (optionalEmployee.isEmpty()) {
//            throw new RuntimeException("Employee with email " + email + " not found.");
//        }
//
//        Employee employee = optionalEmployee.get();
//        Optional<RoleMaster> optionalRole = roleMasterRepository.findById(roleId);
//
//        if (optionalRole.isEmpty()) {
//            throw new RuntimeException("Role with ID " + roleId + " not found.");
//        }
//
//        // Assign the role to the employee and update the employee
//        employee.setRole(optionalRole.get());
//        employee.setUpdatedOn(Helper.getCurrentTimeStamp());
//
//        // Save the updated employee with the new role
//        employeeRepository.save(employee);
//    }


    @Override
    public void updateEmployeeRoleById(Integer id, Integer roleId) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee with ID " + id + " not found.");
        }

        Employee employee = optionalEmployee.get();
        Optional<RoleMaster> optionalRole = roleMasterRepository.findById(roleId);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role with ID " + roleId + " not found.");
        }

        RoleMaster newRole = optionalRole.get();

        // Check if the new role is 'admin' and if the employee's branch already has an admin
        if (newRole.getRole().equalsIgnoreCase("ADMIN")) {
            Optional<Employee> existingAdmin = employeeRepository.findByRoleAndBranch(newRole, employee.getBranch());
            if (existingAdmin.isPresent() && !existingAdmin.get().getId().equals(employee.getId())) {
                throw new RuntimeException("There is already an admin assigned to this branch.");
            }
        }

        // Assign the role to the employee and update the employee
        employee.setRole(newRole);
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Save the updated employee with the new role
        employeeRepository.save(employee);
    }

    @Override
    public void updateEmployeeRoleByEmail(String email, Integer roleId) {
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(email);

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee with email " + email + " not found.");
        }

        Employee employee = optionalEmployee.get();
        Optional<RoleMaster> optionalRole = roleMasterRepository.findById(roleId);

        if (optionalRole.isEmpty()) {
            throw new RuntimeException("Role with ID " + roleId + " not found.");
        }

        RoleMaster newRole = optionalRole.get();

        // Check if the new role is 'admin' and if the employee's branch already has an admin
        if (newRole.getRole().equalsIgnoreCase("ADMIN")) {
            Optional<Employee> existingAdmin = employeeRepository.findByRoleAndBranch(newRole, employee.getBranch());
            if (existingAdmin.isPresent() && !existingAdmin.get().getId().equals(employee.getId())) {
                throw new RuntimeException("There is already an admin assigned to this branch.");
            }
        }

        // Assign the role to the employee and update the employee
        employee.setRole(newRole);
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Save the updated employee with the new role
        employeeRepository.save(employee);
    }

    @Override
    public List<Employee> findEmployeesByBranch(BranchMaster branch) {
        return employeeRepository.findByBranch(branch);
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
