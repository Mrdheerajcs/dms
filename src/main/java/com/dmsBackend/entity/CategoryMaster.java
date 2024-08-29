package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class CategoryMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;
    @Column(name = "created_on", updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;
}
