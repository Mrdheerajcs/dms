package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.EmployeeType;
import com.dmsBackend.security.EmailService;
import com.dmsBackend.security.JwtUtil;
import com.dmsBackend.security.OtpService;
import com.dmsBackend.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest authRequest) {
        logger.info("User {} is attempting to login.", authRequest.getEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Employee employee = employeeService.findByEmail(authRequest.getEmail());
            if (employee == null) {
                return createErrorResponse("Employee not found.");
            }

            // Check if the employee type is assigned
            if (employee.getEmployeeType() == null) {
                return createErrorResponse("Employee Type not assigned. Please contact Admin.");
            }

            return sendOtpForLogin(employee, authRequest.getEmail());
        } catch (BadCredentialsException e) {
            logger.error("Invalid login attempt for user {}: {}", authRequest.getEmail(), e.getMessage());
            return createErrorResponse("Invalid username or password.");
        }
    }

    private ResponseEntity<?> sendOtpForLogin(Employee employee, String email) {
        String otp = otpService.generateOtp(email);
        emailService.sendOtp(email, otp);

        String message = "OTP sent to " + employee.getEmployeeType().name() + " email.";
        return ResponseEntity.ok(new AuthResponse(null, message, employee.getEmployeeType(), employee.getName()));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());

        if (!isValid) {
            return createErrorResponse("Invalid OTP.");
        }

        Employee employee = employeeService.findByEmail(otpRequest.getEmail());
        if (employee == null) {
            return createErrorResponse("Employee not found.");
        }

        String token = jwtUtil.generateToken(employee.getEmail(), employee.getEmployeeType().name());
        otpService.clearOtp(otpRequest.getEmail()); // Clear OTP after successful validation

        return ResponseEntity.ok(new AuthResponse(token, "OTP verified successfully.", employee.getEmployeeType(), employee.getName()));
    }

    // Centralized error response method
    private ResponseEntity<ApiResponse> createErrorResponse(String message) {
        return ResponseEntity.badRequest().body(new ApiResponse("error", message, null));
    }

    // DTO classes

    public static class AuthRequest {
        @jakarta.validation.constraints.Email
        @jakarta.validation.constraints.NotBlank
        private String email;

        @jakarta.validation.constraints.NotBlank
        private String password;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class OtpRequest {
        @jakarta.validation.constraints.Email
        @jakarta.validation.constraints.NotBlank
        private String email;

        @jakarta.validation.constraints.NotBlank
        private String otp;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }

    public static class AuthResponse {
        private String token;  // JWT token for users
        private String message; // Optional message for response
        private EmployeeType employeeType; // Store the employee type
        private String name; // Store the employee name

        public AuthResponse(String token, String message, EmployeeType employeeType, String name) {
            this.token = token;
            this.message = message;
            this.employeeType = employeeType;
            this.name = name;
        }

        // Getters and Setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public EmployeeType getEmployeeType() {
            return employeeType;
        }

        public void setEmployeeType(EmployeeType employeeType) {
            this.employeeType = employeeType;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
