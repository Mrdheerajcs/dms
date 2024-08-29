package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.BranchMasterRepository;
import com.dmsBackend.service.BranchMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchMasterServiceImpl implements BranchMasterService {

    @Autowired
    private BranchMasterRepository branchMasterRepository;
    @Override
    public BranchMaster saveBranchMaster(BranchMaster branchMaster) {
        branchMaster.setCreatedOn(Helper.getCurrentTimeStamp());
        branchMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        return branchMasterRepository.save(branchMaster);
    }

    @Override
    public BranchMaster updateBranchMaster(BranchMaster branchMaster, Integer id) {
        Optional<BranchMaster> branchMasterOptional = branchMasterRepository.findById(id);

        if (branchMasterOptional.isPresent()) {
            BranchMaster branchMaster1 = branchMasterOptional.get();
            branchMaster1.setName(branchMaster.getName());
            branchMaster1.setAddress(branchMaster.getAddress());
            branchMaster1.setUpdatedOn(Helper.getCurrentTimeStamp());

            return branchMasterRepository.save(branchMaster1);
        }else {
            throw new ResourceNotFoundException("BranchMaster not found for ","Id" ,id);
        }
    }
    @Override
    public void deleteByIdBranchMaster(Integer id) {
       branchMasterRepository.deleteById(id);
    }

    @Override
    public List<BranchMaster> findAllBranchMaster() {
        return branchMasterRepository.findAll();
    }

    @Override
    public Optional<BranchMaster> findBranchMasterById( Integer id) {
        return branchMasterRepository.findById(id);
    }

    @Override
    public BranchMaster findByIdBran(Integer id) {
        return branchMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Branch not found","Id",id));
    }
}
