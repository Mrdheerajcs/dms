package com.dmsBackend.service;

import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.entity.TypeMaster;

import java.util.List;
import java.util.Optional;

public interface RoleMasterService {
    RoleMaster saveRoleMaster(RoleMaster roleMaster);
    RoleMaster updateRoleMaster(RoleMaster roleMaster,Integer id);
    void deleteByIdRoleMaster(Integer id);
    List<RoleMaster> findAllRoleMaster();
    Optional<RoleMaster> findRoleMasterById(Integer id);
    RoleMaster updateStatus(Integer id, Integer isActive);
}
