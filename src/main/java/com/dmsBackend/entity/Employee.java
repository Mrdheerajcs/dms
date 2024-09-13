package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
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

    @Column(name = "email", unique = true) // Ensure email is unique
    private String email;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "employee_type")
    private EmployeeType employeeType; // Field for employee type (Admin/User)

    @Column(name = "isActive")
    private int isActive;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_roles",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleMaster> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole())) // Assuming RoleMaster has a 'getRole()' method
                .collect(Collectors.toList());
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
        return this.isActive == 1; // Assuming 1 means active, 0 means inactive
    }
}
