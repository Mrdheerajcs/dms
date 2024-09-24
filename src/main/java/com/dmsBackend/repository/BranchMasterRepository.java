package com.dmsBackend.repository;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchMasterRepository extends JpaRepository<BranchMaster,Integer> {

    List<BranchMaster> findByIsActive(Integer isActive);
}
