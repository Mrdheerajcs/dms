package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.entity.Employee;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.DocumentDetailRepository;
import com.dmsBackend.repository.DocumentHeaderRepository;
import com.dmsBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DocumentServiceImpl  implements DocumentService{

    @Autowired
    private DocumentHeaderRepository documentHeaderRepository;

    @Autowired
    private DocumentDetailRepository documentDetailsRepository;

    @Autowired
    private CategoryMasterService categoryMasterService;

    @Autowired
    private YearMasterService yearMasterService;

    @Autowired
    private TypeMasterService typeMasterService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentMasterService departmentMasterService;

    @Autowired
    private BranchMasterService branchMasterService;

    public void saveDocument(Integer empId,String filePath, String title, String subject,
                              Integer documentHeaderCategoryId,
                             Integer documentHeaderYearId, Integer documentHeaderTypeId
                            ) {

        //get Employee  Detail
        Employee byIdEmp = this.employeeService.findByIdEmp(empId);


        // Save DocumentHeader
        DocumentHeader documentHeader = new DocumentHeader();
        documentHeader.setTitle(title);
        documentHeader.setFileNo("12345");
        documentHeader.setSubject(subject);
        documentHeader.setVersion("1.0");
        documentHeader.setApproved(true);
        documentHeader.setCreatedOn(Helper.getCurrentTimeStamp());
        documentHeader.setUpdatedOn(Helper.getCurrentTimeStamp());

        documentHeader.setCategory(categoryMasterService.findByIdCate(documentHeaderCategoryId));
        documentHeader.setYear(yearMasterService.findByIdyear(documentHeaderYearId));
        documentHeader.setType(typeMasterService.findByIdType(documentHeaderTypeId));
        documentHeader.setEmployee(employeeService.findByIdEmp(byIdEmp.getId()));
        documentHeader.setDepartment(byIdEmp.getDepartment());
        documentHeader.setBranch(byIdEmp.getBranch());

        documentHeaderRepository.save(documentHeader);

        // Save DocumentDetails
        DocumentDetails documentDetails = new DocumentDetails();
        documentDetails.setPath(filePath);
        documentDetails.setCreatedOn(Helper.getCurrentTimeStamp());
        documentDetails.setUpdatedOn(Helper.getCurrentTimeStamp());

        documentDetails.setDocumentHeader(documentHeader);
        documentDetails.setCategory(categoryMasterService.findByIdCate(documentHeaderCategoryId));
        documentDetails.setType(typeMasterService.findByIdType(documentHeaderTypeId));
        documentDetails.setEmployee(employeeService.findByIdEmp(byIdEmp.getId()));
        documentDetails.setYear(yearMasterService.findByIdyear(documentHeaderYearId));
        documentDetails.setDepartment(departmentMasterService.findByIdDep(byIdEmp.getDepartment().getId()));
        documentDetails.setBranch(branchMasterService.findByIdBran(byIdEmp.getBranch().getId()));

        documentDetailsRepository.save(documentDetails);
    }
}


