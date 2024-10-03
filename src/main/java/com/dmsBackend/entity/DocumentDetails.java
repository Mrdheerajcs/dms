package com.dmsBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class DocumentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String DocName;

    @Column(nullable = false)
    private String path;  // Path where file is saved

    @Column(name = "created_on", nullable = false, updatable = false)
    private Timestamp createdOn;

    @Column(name = "updated_on")
    private Timestamp updatedOn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_header_id", nullable = false)
    @JsonIgnoreProperties({"documentDetails"})
    private DocumentHeader documentHeader;

}
