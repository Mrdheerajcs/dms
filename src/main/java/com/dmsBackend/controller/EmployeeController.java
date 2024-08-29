package com.dmsBackend.controller;

import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeHasRoleMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.EmployeeHasRoleService;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Employee")

@CrossOrigin("http://localhost:3000")
public class EmployeeController {
    @Autowired
    EmployeeService employeeService;
    @Autowired
    private RoleMasterService roleService;

    @Autowired
    private EmployeeHasRoleService employeeHasRoleService;

    @PostMapping("/saved")
    public ResponseEntity<?> registerUser(@RequestBody Employee employee) {
        try {
            // Save employee
            Employee savedEmployee = employeeService.save(employee);

            // Save roles and associate with employee
            for (RoleMaster role : employee.getRoles()) {
                RoleMaster savedRole = roleService.saveRoleMaster(role);

                // Save EmployeeHasRole
                EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
                employeeHasRole.setEmployee(savedEmployee);
                employeeHasRole.setRole(savedRole);
                employeeHasRoleService.saved(employeeHasRole);
            }

            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving employee");
        }
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Employee> deletebyIdemployee(@PathVariable Integer id) {
        this.employeeService.deleteByIdEmployee(id);
        return new ResponseEntity(new ApiResponse("employee deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Employee>> findAllEmployee() {
        List<Employee> allEmployee = this.employeeService.findAllEmployee();
        return new ResponseEntity(allEmployee, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<Employee> findByIdEmployee(@PathVariable Integer id) {
        Optional<Employee> employee1 = employeeService.findEmployeeById(id);
        return employee1.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/updateStatusEmp")
    public ResponseEntity updateEmployeeStatus(Integer id, Integer isActive) {
        this.employeeService.updateEmployeeStatus(id,isActive);
        return new ResponseEntity(HttpStatus.OK);
    }



}
