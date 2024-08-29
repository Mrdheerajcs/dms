package com.dmsBackend.repository;

import com.dmsBackend.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
    Employee findByEmail(String email);

    Employee findByName(String name);
}
