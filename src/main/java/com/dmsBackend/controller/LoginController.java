package com.dmsBackend.controller;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/login")
@CrossOrigin("http://localhost:3000")
public class LoginController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<?> loginUser(@RequestBody Employee loginEmployee, HttpSession session) throws NoSuchAlgorithmException {
        Employee employee = employeeService.findByEmail(loginEmployee.getEmail());
        if (employee != null && employee.getPassword().equals(hashPassword(loginEmployee.getPassword()))) {
            session.setAttribute("employee", employee);
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password");
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] bytes = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
