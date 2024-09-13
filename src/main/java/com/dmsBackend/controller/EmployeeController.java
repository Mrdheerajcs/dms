package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeHasRoleMaster;
import com.dmsBackend.entity.EmployeeType;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.EmployeeHasRoleService;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    private EmployeeHasRoleService employeeHasRoleService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> registerUser(@Validated @RequestBody Employee employee) {
        try {
            // Validate employeeType
            if (employee.getEmployeeType() == null) {
                return ResponseEntity.badRequest().body(new ApiResponse("Employee type must be specified.", false));
            }

            // Save employee
            Employee savedEmployee = employeeService.save(employee);

            // Associate roles with employee
            if (employee.getRoles() != null) {
                for (RoleMaster role : employee.getRoles()) {
                    RoleMaster savedRole = roleService.saveRoleMaster(role);
                    EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
                    employeeHasRole.setEmployee(savedEmployee);
                    employeeHasRole.setRole(savedRole);
                    employeeHasRoleService.saved(employeeHasRole);
                }
            }

            return ResponseEntity.ok(new ApiResponse("Employee registered successfully.", true));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error saving employee: " + e.getMessage(), false));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteByIdEmployee(@PathVariable Integer id) {
        employeeService.deleteByIdEmployee(id);
        return ResponseEntity.ok(new ApiResponse("Employee deleted successfully.", true));
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> findAllEmployee() {
        List<Employee> allEmployees = employeeService.findAllEmployee();
        return ResponseEntity.ok(allEmployees);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Employee> findByIdEmployee(@PathVariable Integer id) {
        Optional<Employee> employee = employeeService.findEmployeeById(id);
        return employee.map(value -> ResponseEntity.ok(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity<ApiResponse> updateEmployeeStatus(@PathVariable Integer id, @RequestParam Integer isActive) {
        employeeService.updateEmployeeStatus(id, isActive);
        return ResponseEntity.ok(new ApiResponse("Employee status updated successfully.", true));
    }

    @PutMapping("/{id}/type")
    public ResponseEntity<Void> updateEmployeeType(@PathVariable Integer id, @RequestBody EmployeeType employeeType) {
        try {
            employeeService.updateEmployeeType(id, employeeType);
            return ResponseEntity.noContent().build(); // Return 204 No Content
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
