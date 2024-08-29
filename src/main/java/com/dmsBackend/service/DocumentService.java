package com.dmsBackend.service;



public interface DocumentService {

    public void saveDocument(Integer empId,String filePath, String title, String subject,
                             Integer documentHeaderCategoryId,
                             Integer documentHeaderYearId, Integer documentHeaderTypeId);
}
