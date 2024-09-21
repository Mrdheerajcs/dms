package com.dmsBackend.controller;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.RoleMaster;
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

import java.util.stream.Collectors;

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
            // Authenticate employee credentials (email and password)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Find employee by email with roles
            Employee employee = employeeService.findByEmail(authRequest.getEmail());
            if (employee == null) {
                return createErrorResponse("Employee not found.");
            }

            // Check if the employee has roles assigned
            if (employee.getRole() == null) {
                throw new IllegalStateException("Employee must have a role to log in.");
            }

            // Get the employee's primary role
            RoleMaster primaryRole = employee.getRole();
            String role = primaryRole != null ? primaryRole.getRole() : "Unknown";

            if ("Unknown".equals(role)) {
                return createErrorResponse("Employee's role is not found.");
            }

            // Send OTP for login as part of 2FA
            return sendOtpForLogin(employee, authRequest.getEmail(), role);
        } catch (BadCredentialsException e) {
            logger.error("Invalid login attempt for user {}: {}", authRequest.getEmail(), e.getMessage());
            return createErrorResponse("Invalid username or password.");
        }
    }
    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpRequest otpRequest) {
        // Validate the OTP entered by the employee
        boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());

        if (!isValid) {
            return createErrorResponse("Invalid OTP.");
        }

        // Fetch employee details after OTP validation
        Employee employee = employeeService.findByEmail(otpRequest.getEmail());

        RoleMaster role = employee.getRole();

        if (role == null) {
            return createErrorResponse("Employee's role not found.");
        }

        String roles = role.getRole(); // Get the role name

        if (roles.isEmpty()) {
            return createErrorResponse("Employee's role is empty.");
        }


        // Generate a JWT token with the employee's email, roles, name, and ID
        String token = jwtUtil.generateToken(employee.getEmail(), roles, employee.getName(), employee.getId());

        // Clear OTP after successful validation
        otpService.clearOtp(otpRequest.getEmail());

        // Return the JWT token and employee details to the frontend
        return ResponseEntity.ok(new AuthResponse(token, "OTP verified successfully.", roles, employee.getName(), employee.getId()));
    }

    // Helper method to handle OTP sending logic
    private ResponseEntity<?> sendOtpForLogin(Employee employee, String email, String roles) {
        // Generate OTP
        String otp = otpService.generateOtp(email);

        // Send OTP via email
        emailService.sendOtp(email, otp);

        String message = "OTP sent to email for roles: " + roles;

        // Return a response to the frontend with a success message
        return ResponseEntity.ok(new AuthResponse(null, message, roles, employee.getName(), employee.getId()));
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
        private String token;
        private String message;
        private String roles;
        private String name;
        private Integer id;

        public AuthResponse(String token, String message, String roles, String name, Integer id) {
            this.token = token;
            this.message = message;
            this.roles = roles;
            this.name = name;
            this.id = id;
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

        public String getRoles() {
            return roles;
        }

        public void setRoles(String roles) {
            this.roles = roles;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }
}
