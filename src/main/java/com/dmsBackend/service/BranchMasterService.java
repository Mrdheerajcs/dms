package com.dmsBackend.service;

import com.dmsBackend.entity.BranchMaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BranchMasterService {
    BranchMaster saveBranchMaster(BranchMaster branchMaster);
    BranchMaster updateBranchMaster(BranchMaster branchMaster,Integer id);
     void deleteByIdBranchMaster(Integer id);
     List<BranchMaster> findAllBranchMaster();
     Optional<BranchMaster> findBranchMasterById(Integer id);
    BranchMaster findByIdBran(Integer id);



}
