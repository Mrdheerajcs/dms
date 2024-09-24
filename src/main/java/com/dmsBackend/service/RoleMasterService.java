package com.dmsBackend.service;

import com.dmsBackend.entity.RoleMaster;

import java.util.List;
import java.util.Optional;

public interface RoleMasterService {

    RoleMaster saveRoleMaster(RoleMaster roleMaster);
    RoleMaster updateRoleMaster(RoleMaster roleMaster, Integer id);
    void deleteByIdRoleMaster(Integer id);
    List<RoleMaster> findAllRoleMaster();
    Optional<RoleMaster> findRoleMasterById(Integer id);
    List<RoleMaster> findAllActiveRoleMaster(boolean isActive);
    Optional<RoleMaster> findRoleByName(String name);

    RoleMaster updateStatus(Integer id, boolean isActive);


}
