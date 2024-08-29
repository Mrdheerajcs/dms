package com.dmsBackend.controller;

import com.dmsBackend.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate OTP and send it
            String otp = otpService.generateOtp(authRequest.getEmail());
            emailService.sendOtp(authRequest.getEmail(), otp);

            return ResponseEntity.ok("OTP sent to your email.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Invalid username or password.");
        }
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpRequest otpRequest) {
        boolean isValid = otpService.validateOtp(otpRequest.getEmail(), otpRequest.getOtp());

        if (!isValid) {
            return ResponseEntity.status(401).body("Invalid OTP.");
        }

        // Load user details and generate JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(otpRequest.getEmail());
        String token = jwtUtil.generateToken(userDetails.getUsername());

        // Clear OTP after successful validation
        otpService.clearOtp(otpRequest.getEmail());

        return ResponseEntity.ok(new AuthResponse(token));
    }
}



// DTO classes
class AuthRequest {
    private String email;
    private String password;

    // getters and setters
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

class OtpRequest {
    private String email;
    private String otp;

    // getters and setters
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

class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    // getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
