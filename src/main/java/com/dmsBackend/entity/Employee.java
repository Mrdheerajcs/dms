package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

@Entity
@Data // Lombok will generate getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Optional: Generates a constructor with all fields
public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "employee_Id")
    private String employeeId;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "isActive")
    private boolean isActive;

    @Column(name = "otp")
    private int otp;

    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    @ManyToOne
    @JoinColumn(name = "department_master_id")
    private DepartmentMaster department;

    @ManyToOne
    @JoinColumn(name = "department_master_branch_Master_Id")
    private BranchMaster branch;

    // Role is nullable to allow registration without a role,
    // but later can be updated when assigning a role after registration.
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = true) // Role is nullable by default
    private RoleMaster role;


    @ManyToOne
    @JoinColumn(name = "created_by_id") // New field to track who created the record
    private Employee createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by_id") // New field to track who updated the record
    private Employee updatedBy;

    // Get authorities based on role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Check if role is not null and provide authorities accordingly
        if (this.role != null && this.role.getRole() != null) {
            return List.of(new SimpleGrantedAuthority(this.role.getRole()));
        }
        // If role is null, return an empty list (no authorities)
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActive; // Assuming isActive indicates if the account is enabled
    }
}
