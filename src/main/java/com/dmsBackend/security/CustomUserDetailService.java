package com.dmsBackend.security;


import com.dmsBackend.entity.Employee;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Employee employee = this.employeeRepository.findByEmail(name);
        if (employee == null) {
            throw new UsernameNotFoundException("User not found with email: " + name);
        }

        // Log the details
        System.out.println("User found: " + employee.getEmail());


        return new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                new ArrayList<>() // Assuming roles and authorities will be handled elsewhere
        );
    }

}
