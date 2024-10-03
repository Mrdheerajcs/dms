package com.dmsBackend.repository;

import com.dmsBackend.entity.YearMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearMasterRepository extends JpaRepository<YearMaster,Integer> {
}
