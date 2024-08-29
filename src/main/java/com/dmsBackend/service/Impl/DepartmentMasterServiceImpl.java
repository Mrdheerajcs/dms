package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.BranchMasterRepository;
import com.dmsBackend.repository.DepartmentMasterRepository;
import com.dmsBackend.service.DepartmentMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentMasterServiceImpl implements DepartmentMasterService {
    @Autowired
    DepartmentMasterRepository departmentMasterRepository;
    @Autowired
    BranchMasterRepository branchMasterRepository;
    @Override
    public DepartmentMaster saveDepartmentMaster(DepartmentMaster departmentMaster) {
        BranchMaster branchMaster = branchMasterRepository.findById(departmentMaster.getBranch().getId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));
        departmentMaster.setBranch(branchMaster);

        departmentMaster.setCreatedOn(Helper.getCurrentTimeStamp());
        departmentMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        DepartmentMaster saveDepartmentMaster = departmentMasterRepository.save(departmentMaster);
        return saveDepartmentMaster;
    }

    @Override
    public DepartmentMaster updateDepartmentMaster(DepartmentMaster departmentMaster, Integer id) {
        Optional<DepartmentMaster> departmentMaster1 = departmentMasterRepository.findById(id);
        if(departmentMaster1.isPresent()){
            DepartmentMaster departmentMaster2 = departmentMaster1.get();
            departmentMaster2.setName(departmentMaster.getName());
            departmentMaster2.setBranch(departmentMaster.getBranch());
            departmentMaster2.setIsActive(departmentMaster.getIsActive());
            departmentMaster2.setUpdatedOn(Helper.getCurrentTimeStamp());

            BranchMaster branchMaster = branchMasterRepository.findById(departmentMaster.getBranch().getId())
                    .orElseThrow(() -> new RuntimeException("Branch not found"));
            departmentMaster2.setBranch(branchMaster);

            DepartmentMaster saveDepartmentMaster = departmentMasterRepository.save(departmentMaster2);
            return saveDepartmentMaster;
        }else {
            throw new ResourceNotFoundException("DepartmentMaster not found","Id",id);
        }
    }

    @Override
    public void deleteByIdDepartmentMaster(Integer id) {
         departmentMasterRepository.deleteById(id);

    }

    @Override
    public List<DepartmentMaster> findAllDepartmentMaster() {
        return departmentMasterRepository.findAll();
    }

    @Override
    public Optional<DepartmentMaster> findDepartmentMasterById(Integer id) {
        return departmentMasterRepository.findById(id);
    }

    @Override
    public DepartmentMaster findByIdDep(Integer id) {
        return departmentMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Department not found","Id",id));
    }

    @Override
    public DepartmentMaster updateStatusDepartment(Integer id, Integer isApproved) {
        DepartmentMaster departmentMaster = departmentMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DocumentHeader", "id", id));

        departmentMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        departmentMaster.setIsActive(isApproved);
        return departmentMasterRepository.save(departmentMaster);
    }

}
