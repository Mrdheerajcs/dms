package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
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
    private RoleMasterService roleMasterService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/newRegister")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody Employee employee) {
        String generatedPassword = generatePassword(employee.getName(), employee.getMobile());

        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Email must not be null or empty.", false));
        }

        if (employee.getName() == null || employee.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Name must not be null or empty.", false));
        }

        if (employee.getMobile() == null || employee.getMobile().isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("Mobile number must not be null or empty.", false));
        }
        
        employee.setPassword(passwordEncoder.encode(generatedPassword));

        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        try {
            employee.setRole(null);
            Employee savedEmployee = employeeService.save(employee);

            try {
                sendPasswordEmail(employee.getEmail(), generatedPassword);
            } catch (MailException e) {
                // Handle email failure (optional)
                return ResponseEntity.ok(new ApiResponse("Employee registered, but email sending failed.", true));
            }

            return ResponseEntity.ok(new ApiResponse("Employee registered successfully, roles not assigned.", true));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), false));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Error registering employee: " + e.getMessage(), false));
        }
    }

    private String generatePassword(String username, String mobile) {
        String usernamePrefix = username.length() >= 4 ? username.substring(0, 4).toUpperCase() : username.toUpperCase();
        String mobileSuffix = mobile.length() >= 4 ? mobile.substring(mobile.length() - 4) : mobile;
        return usernamePrefix + mobileSuffix;
    }

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
}
