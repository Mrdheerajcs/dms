package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse> updateEmployeeStatus(@PathVariable Integer id, @RequestParam boolean isActive) {
        employeeService.updateEmployeeStatus(id, isActive);
        return ResponseEntity.ok(new ApiResponse("Employee status updated successfully.", true));
    }


    @PutMapping("/employee/{email}/role")
    public ResponseEntity<?> updateEmployeeRole(@PathVariable String email, @RequestBody Map<String, String> requestBody) {
        String roleName = requestBody.get("roleName");

        if (roleName == null || roleName.isEmpty()) {
            return ResponseEntity.badRequest().body("Role name must not be null or empty.");
        }

        try {
            // Find the role by name and get its ID
            Integer roleId = roleService.findRoleByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid role name: " + roleName))
                    .getId();

            // Update the employee role by email
            employeeService.updateEmployeeRole(email, roleId);
            return ResponseEntity.ok("Role updated successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body("Error updating role: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating role: " + e.getMessage());
        }
    }

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
}
