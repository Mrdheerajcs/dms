package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Data
public class RoleMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "role")
    private String role;

    @Column(name="is_Active")
    private int isActive;

    @Column(name="createdOn")
    private Timestamp createdOn;
    @Column(name = "updatedOn")
    private Timestamp updatedOn;

    @ManyToMany(mappedBy = "roles")
    private Set<Employee> employees;
}
