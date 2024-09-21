package com.dmsBackend.service;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.CategoryMaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface CategoryMasterService {
    CategoryMaster savecategoryMaster(CategoryMaster categoryMaster);
    CategoryMaster updateCategoryMaster(CategoryMaster categoryMaster,Integer id);
    void deleteByIdCategoryMaster(Integer id);
    List<CategoryMaster> findAllCategoryMaster();
    Optional<CategoryMaster> findCategoryMasterById(Integer id);
    CategoryMaster findByIdCate(Integer id);
    CategoryMaster updateStatus(Integer id, boolean  isActive);
}
