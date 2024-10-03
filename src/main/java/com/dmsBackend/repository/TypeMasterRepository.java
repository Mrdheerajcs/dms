package com.dmsBackend.repository;

import com.dmsBackend.entity.TypeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMasterRepository extends JpaRepository<TypeMaster,Integer> {
}
