package com.dmsBackend.repository;

import com.dmsBackend.entity.DepartmentMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentMasterRepository extends JpaRepository<DepartmentMaster,Integer> {

    List<DepartmentMaster> findByIsActive(Integer isActive);

    List<DepartmentMaster> findByBranchId(Integer branchId);
}
