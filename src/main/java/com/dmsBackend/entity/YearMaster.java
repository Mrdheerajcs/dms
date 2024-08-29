package com.dmsBackend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class YearMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name="createdOn")
    private Timestamp createdOn;
    @Column(name = "updatedOn")
    private Timestamp updatedOn;
}
