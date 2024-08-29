package com.dmsBackend.service.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class EmployeeIdGenerator {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public String generateEmployeeId() {
        // Retrieve the next value of the sequence
        Long sequenceValue = ((Number) entityManager.createNativeQuery("SELECT nextval('employee_id_seq')").getSingleResult()).longValue();
        // Combine the prefix "EMP" with the sequence number, formatted to 5 digits
        return "EMP" + String.format("%05d", sequenceValue);
    }
}
