package com.dmsBackend.repository;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BranchMasterRepository extends JpaRepository<BranchMaster,Integer> {

    List<BranchMaster> findByIsActive(Integer isActive);
}
