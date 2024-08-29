package com.dmsBackend.controller;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeHasRoleMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.service.EmployeeHasRoleService;
import com.dmsBackend.service.EmployeeService;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@RestController
@RequestMapping("/register")
@CrossOrigin("http://localhost:3000")
public class RegisterController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder; // This should be BCryptPasswordEncoder

    @Autowired
    private EmployeeHasRoleService employeeHasRoleService;

    @Autowired
    private RoleMasterService roleMasterService;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/newRegister")
    public ResponseEntity<?> registerUser(@RequestBody Employee employee) {
        // Generate password
        String generatedPassword = generatePassword(employee.getName(), employee.getMobile());

        // Encode the password before saving
//        String encodedPassword = passwordEncoder.encode(generatedPassword);
//        employee.setPassword(encodedPassword);
        employee.setPassword(generatedPassword);

        employee.setCreatedOn(Helper.getCurrentTimeStamp());
        employee.setUpdatedOn(Helper.getCurrentTimeStamp());

        try {
            // Save employee
            Employee savedEmployee = employeeService.save(employee);

            // Save roles and associate with employee
            for (RoleMaster role : employee.getRoles()) {
                RoleMaster savedRole = roleMasterService.saveRoleMaster(role);

                // Save EmployeeHasRole
                EmployeeHasRoleMaster employeeHasRole = new EmployeeHasRoleMaster();
                employeeHasRole.setEmployee(savedEmployee);
                employeeHasRole.setRole(savedRole);
                employeeHasRole.setDepartment(savedEmployee.getDepartment());
                employeeHasRole.setBranch(savedEmployee.getBranch());
                employeeHasRoleService.saved(employeeHasRole);
            }

            // Send email with the generated password
            sendPasswordEmail(employee.getEmail(), generatedPassword);

            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving employee");
        }
    }

    // Method to generate a password based on the username and first 4 digits of the mobile number
    private String generatePassword(String username, String mobile) {
        // Extract the first 4 letters of the username and convert them to uppercase
        String usernamePrefix = username.length() >= 4 ? username.substring(0, 4).toUpperCase() : username.toUpperCase();

        // Extract the last 4 digits of the mobile number
        String mobileSuffix = mobile.length() >= 4 ? mobile.substring(mobile.length() - 4) : mobile;

        // Combine the usernamePrefix and mobileSuffix
        return usernamePrefix + mobileSuffix;
    }


    // Method to send email with the generated password
    private void sendPasswordEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your New Account Details");
        message.setText("Dear Employee,\n\nYour account has been created. Here are your login details:\n\n" +
                "Email: " + email + "\n" +
                "your password is first 4 Upper latter in your username and last 4 digit in your Mobile Number.\n\n" +
                "username is: Arioraj and mobile number is: 92xxxx8953 \n\n" +
                "Password: ARIO8953.\n\n" +
//                "Password: " + password + "\n\n" +
                "Please change your password after logging in.\n\n" +
                "Best regards,\nCompany Team");

        mailSender.send(message);
    }
}



