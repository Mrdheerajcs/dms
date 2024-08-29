package com.dmsBackend.controller;
import com.dmsBackend.entity.Employee;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
@CrossOrigin("http://localhost:3000")
public class HomeController {
    @GetMapping
    public ResponseEntity<?> home(HttpSession session) {
        Employee employee = (Employee) session.getAttribute("employee");
        if (employee != null) {
            return ResponseEntity.ok("Welcome " + employee.getEmail());
        } else {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
