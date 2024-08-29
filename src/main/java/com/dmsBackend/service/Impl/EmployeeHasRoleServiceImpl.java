package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.EmployeeHasRoleMaster;
import com.dmsBackend.repository.EmployeeHasRoleMasterRepository;
import com.dmsBackend.service.EmployeeHasRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeHasRoleServiceImpl implements EmployeeHasRoleService {

    @Autowired
    EmployeeHasRoleMasterRepository employeeHasRoleMasterRepository;

    @Override
    public EmployeeHasRoleMaster saved(EmployeeHasRoleMaster employeeHasRoleMaster) {

        return employeeHasRoleMasterRepository.save(employeeHasRoleMaster);
    }
}
