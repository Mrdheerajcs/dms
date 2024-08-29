package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.RoleMasterRepository;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleMasterServiceImpl implements RoleMasterService {
    @Autowired
    RoleMasterRepository roleMasterRepository;
    @Override
    public RoleMaster saveRoleMaster(RoleMaster roleMaster) {
        roleMaster.setCreatedOn(Helper.getCurrentTimeStamp());
        roleMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        RoleMaster saveRoleMaster = this.roleMasterRepository.save(roleMaster);
        return saveRoleMaster;
    }

    @Override
    public RoleMaster updateRoleMaster(RoleMaster roleMaster, Integer id) {

        Optional<RoleMaster> roleMasterOptional = roleMasterRepository.findById(id);

        if (roleMasterOptional.isPresent()) {
            RoleMaster roleMaster1 = roleMasterOptional.get();
            roleMaster1.setRole(roleMaster.getRole());
            roleMaster1.setIsActive(roleMaster.getIsActive());
            roleMaster1.setUpdatedOn(Helper.getCurrentTimeStamp());

            return roleMasterRepository.save(roleMaster1);
        } else {
            throw new ResourceNotFoundException("RoleMaster not found for ", "Id", id);
        }
    }

    @Override
    public void deleteByIdRoleMaster(Integer id) {
        this.roleMasterRepository.deleteById(id);

    }

    @Override
    public List<RoleMaster> findAllRoleMaster() {
        return this.roleMasterRepository.findAll();
    }

    @Override
    public Optional<RoleMaster> findRoleMasterById(Integer id) {
       return this.roleMasterRepository.findById(id);
    }

    @Override
    public RoleMaster updateStatus(Integer id, Integer isActive) {
        RoleMaster roleMaster = roleMasterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", id));

        roleMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        roleMaster.setIsActive(isActive);
        return roleMasterRepository.save(roleMaster);
    }

}
