package com.dmsBackend.controller;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
@CrossOrigin("http://localhost:3000")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private RoleMasterService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JavaMailSender mailSender;

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeDetails) {
        try {
            // Find the existing employee
            Employee existingEmployee = employeeService.findById(id);
            if (existingEmployee == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with ID: " + id);
            }

            // Get the current authenticated user
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Employee currentEmployee = employeeService.findByEmail(currentUser.getUsername());

            if (currentEmployee == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Current user not found");
            }

            // Update employee details without changing role or password
            if (employeeDetails.getName() != null) {
                existingEmployee.setName(employeeDetails.getName());
            }
            if (employeeDetails.getEmail() != null) {
                existingEmployee.setEmail(employeeDetails.getEmail());
            }
            if (employeeDetails.getMobile() != null) {
                existingEmployee.setMobile(employeeDetails.getMobile());
            }
            if (employeeDetails.getBranch() != null) {
                existingEmployee.setBranch(employeeDetails.getBranch());
            }
            if (employeeDetails.getDepartment() != null) {
                existingEmployee.setDepartment(employeeDetails.getDepartment());
            }

            // Set updatedBy and updatedOn
            existingEmployee.setUpdatedBy(currentEmployee);
            existingEmployee.setUpdatedOn(Helper.getCurrentTimeStamp());

            // Save updated employee without role and password changes
            Employee updatedEmployee = employeeService.save(existingEmployee);

            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteByIdEmployee(@PathVariable Integer id) {
        try {
            employeeService.deleteByIdEmployee(id);
            return ResponseEntity.ok(new ApiResponse("Employee deleted successfully.", true));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("Error deleting employee: " + e.getMessage(), false));
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> findAllEmployee() {
        List<Employee> allEmployees = employeeService.findAllEmployee();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Employee> findByIdEmployee(@PathVariable Integer id) {
        Employee employee = employeeService.findById(id);
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<ApiResponse> updateEmployeeStatus(@PathVariable Integer id, @RequestBody Boolean isActive) {
        employeeService.updateEmployeeStatus(id, isActive);
        return ResponseEntity.ok(new ApiResponse("Employee status updated successfully.", true));
    }    //update role
//    @PutMapping("/employee/{identifier}/role")
//    public ResponseEntity<?> updateEmployeeRole(@PathVariable String identifier, @RequestBody Map<String, String> requestBody) {
//        String roleName = requestBody.get("roleName");
//
//        if (roleName == null || roleName.isEmpty()) {
//            return ResponseEntity.badRequest().body("Role name must not be null or empty.");
//        }
//
//        try {
//            // Find the role by name and get its ID
//            Integer roleId = roleService.findRoleByName(roleName)
//                    .orElseThrow(() -> new ResourceNotFoundException("Invalid role name: " + roleName))
//                    .getId();
//
//            Employee updatedEmployee;
//
//            // Check if the identifier is a valid integer (ID) or an email
//            if (identifier.matches("\\d+")) {
//                // Identifier is a numeric ID
//                Integer employeeId = Integer.parseInt(identifier);
//                employeeService.updateEmployeeRoleById(employeeId, roleId);
//                updatedEmployee = employeeService.findById(employeeId); // Get updated employee details
//            } else {
//                // Identifier is an email
//                employeeService.updateEmployeeRoleByEmail(identifier, roleId);
//                updatedEmployee = employeeService.findByEmail(identifier); // Get updated employee details
//            }
//
//            notifyUserRole(updatedEmployee.getEmail(), roleName);
//            return ResponseEntity.ok("Role updated successfully.");
//        } catch (ResourceNotFoundException e) {
//            return ResponseEntity.badRequest().body("Error updating role: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error updating role: " + e.getMessage());
//        }
//    }

    @PutMapping("/{identifier}/role")
    public ResponseEntity<?> updateEmployeeRole(@PathVariable String identifier, @RequestBody Map<String, String> requestBody) {
        String roleName = requestBody.get("roleName");

        // Validate the role name
        if (roleName == null || roleName.isEmpty()) {
            return ResponseEntity.badRequest().body("Error: Role name must not be null or empty.");
        }

        try {
            // Find the role by name and get its ID
            Integer roleId = roleService.findRoleByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid role name: " + roleName))
                    .getId();
            Employee updatedEmployee;

            // Check if the identifier is a valid integer (ID) or an email
            if (identifier.matches("\\d+")) {
                // Identifier is a numeric ID
                Integer employeeId = Integer.parseInt(identifier);
                employeeService.updateEmployeeRoleById(employeeId, roleId); // Update role by employee ID
                updatedEmployee = employeeService.findById(employeeId); // Get updated employee details
            } else {
                // Identifier is an email
                employeeService.updateEmployeeRoleByEmail(identifier, roleId); // Update role by email
                updatedEmployee = employeeService.findByEmail(identifier); // Get updated employee details
            }

            // Notify the user of the role change
            notifyUserRole(updatedEmployee.getEmail(), roleName);
            return ResponseEntity.ok("Success: Role updated successfully for employee with identifier: " + identifier + ".");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: An unexpected error occurred while updating the role. " + e.getMessage());
        }
    }

    //find by role name
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<Employee>> getEmployeesByRole(@PathVariable String roleName) {
        List<Employee> employees = employeeService.findEmployeesByRole(roleName);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/role-null/{id}")
    public ResponseEntity<List<Employee>> getEmployeesByRoleIsNullById(@PathVariable Integer id) {
        List<Employee> employees = employeeService.getEmployeesByRoleIsNullById(id);
        if (employees.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(employees);
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<Employee>> getEmployeesByBranch(@PathVariable("branchId") BranchMaster branch) {
        List<Employee> employees = employeeService.findEmployeesByBranch(branch);
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/role-is-null")
    public ResponseEntity<List<Employee>> getEmployeesByRoleIsNull() {
        List<Employee> employees = employeeService.getEmployeesByRoleIsNull();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/role-not-null")
    public ResponseEntity<List<Employee>> getAllWithoutNullRole() {
        List<Employee> employees = employeeService.getAllWithoutNullRole();
        return ResponseEntity.ok(employees);
    }


    //===========================role msg=============
    private void notifyUserRole(String email, String roleName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Role Assignment Notification");
        message.setText("Dear Employee,\n\n" +
                "We are pleased to inform you that your role has been successfully updated.\n\n" +
                "Assigned Role: " + roleName + "\n\n" +
                "Please log in to your account to review your updated role. For security purposes, we recommend changing your password after your next login.\n\n" +
                "Best regards,\n" +
                "The Company Team");

        // Try to send the email and handle any failures
        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error: Failed to send email notification to " + email + ". " + e.getMessage());
        }
    }

}
