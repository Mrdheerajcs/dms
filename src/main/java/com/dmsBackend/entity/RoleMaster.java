package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class RoleMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role", nullable = false, unique = true)
    private String role;

    @Column(name = "is_Active")
    private Boolean isActive;

    @Column(name = "createdOn")
    private Timestamp createdOn;

    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    // Default no-argument constructor (required by JPA)
    public RoleMaster() {
    }

    // Constructor with role parameter
    public RoleMaster(String role) {
        this.role = role;
    }
}
