package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeHasRoleMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.entity.EmployeeType; // Import EmployeeType
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.service.EmployeeHasRoleService;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin("http://localhost:3000")
public class RegisterController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmployeeHasRoleService employeeHasRoleService;

    @Autowired
    private RoleMasterService roleMasterService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/newRegister")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody Employee employee) {
        // Generate password
        String generatedPassword = generatePassword(employee.getName(), employee.getMobile());

        // Set the password
        employee.setPassword(generatedPassword);
        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        // Set EmployeeType to null by default
        employee.setEmployeeType(null); // Set EmployeeType to null initially

        try {
            // Save employee
            Employee savedEmployee = employeeService.save(employee);

            // Associate roles with employee
            for (RoleMaster role : employee.getRoles()) {
                RoleMaster savedRole = roleMasterService.saveRoleMaster(role);
                EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
                employeeHasRole.setEmployee(savedEmployee);
                employeeHasRole.setRole(savedRole);
                employeeHasRole.setDepartment(savedEmployee.getDepartment());
                employeeHasRole.setBranch(savedEmployee.getBranch());
                employeeHasRoleService.saved(employeeHasRole);
            }

            // Send email with the generated password
            sendPasswordEmail(employee.getEmail(), generatedPassword);

            return ResponseEntity.ok(new ApiResponse("Employee registered successfully.", true));
        } catch (RuntimeException e) {
            // Return specific error messages based on the exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(e.getMessage(), false));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error saving employee: " + e.getMessage(), false));
        }
    }


    // Method to generate a password based on the username and first 4 digits of the mobile number
    private String generatePassword(String username, String mobile) {
        String usernamePrefix = username.length() >= 4 ? username.substring(0, 4).toUpperCase() : username.toUpperCase();
        String mobileSuffix = mobile.length() >= 4 ? mobile.substring(mobile.length() - 4) : mobile;
        return usernamePrefix + mobileSuffix;
    }

    // Method to send email with the generated password
    private void sendPasswordEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your New Account Details");
        message.setText("Dear Employee,\n\nYour account has been created. Here are your login details:\n\n" +
                "Email: " + email + "\n" +
                "Password: " + password + "\n\n" +
                "Please change your password after logging in.\n\n" +
                "Best regards,\nCompany Team");
        mailSender.send(message);
    }

    // Add endpoint to update EmployeeType after registration
    @PutMapping("/{id}/type")
    public ResponseEntity<ApiResponse> updateEmployeeType(@PathVariable Integer id, @RequestBody EmployeeType employeeType) {
        try {
            Employee updatedEmployee = employeeService.updateEmployeeType(id, employeeType);
            return ResponseEntity.ok(new ApiResponse("Employee type updated successfully.", true));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error updating employee type", false));
        }
    }
}
