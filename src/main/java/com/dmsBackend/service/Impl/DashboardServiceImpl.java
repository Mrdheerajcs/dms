package com.dmsBackend.service.Impl;

import com.dmsBackend.repository.*;
import com.dmsBackend.response.DashboardResponse;
import com.dmsBackend.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    DocumentDetailRepository documentDetailRepository;

    @Autowired
    DocumentHeaderRepository documentHeaderRepository;

    @Autowired
    BranchMasterRepository branchMasterRepository;
    @Autowired
    CategoryMasterRepository categoryMasterRepository;

    @Autowired
    DepartmentMasterRepository departmentMasterRepository;

    @Autowired
    RoleMasterRepository roleMasterRepository;

    @Autowired
    TypeMasterRepository typeMasterRepository;

    @Autowired
    YearMasterRepository yearMasterRepository;

    @Override
    public DashboardResponse getAllUsers() {
        DashboardResponse dashboardResponse = new DashboardResponse();
        dashboardResponse.setTotalUser(employeeRepository.count());
        dashboardResponse.setTotalDocument(documentHeaderRepository.count());
        dashboardResponse.setPendingDocument(documentDetailRepository.count());
        dashboardResponse.setStorageUsed(documentDetailRepository.count());
        dashboardResponse.setTotalBranches(branchMasterRepository.count());
        dashboardResponse.setTotalDepartment(departmentMasterRepository.count());
        dashboardResponse.setTotalRoles(roleMasterRepository.count());
        dashboardResponse.setDocumentType(typeMasterRepository.count());
        dashboardResponse.setAnnualYear(yearMasterRepository.count());
        dashboardResponse.setTotalCategories(categoryMasterRepository.count());

        return dashboardResponse;
    }
}
