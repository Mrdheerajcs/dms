package com.dmsBackend.repository;

import com.dmsBackend.entity.Employee;
import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Find an employee by email
    Optional<Employee> findByEmail(String email);

    // Find employees who have no role (role is null)
    List<Employee> findByRoleIsNull();

    // Find an employee by ID and check if the role is null
    List<Employee> findByIdAndRoleIsNull(Integer id);

    // Find all employees who have a non-null role
    List<Employee> findAllByRoleIsNotNull();

    // Count employees with no role assigned
    long countByRoleIsNull();

    // Count employees with a non-null role assigned
    long countByRoleIsNotNull();

    // Count employees by a specific role
    long countByRole(RoleMaster roleMaster);

    // Find employees by a specific role
    List<Employee> findByRole(RoleMaster roleMaster);
}
