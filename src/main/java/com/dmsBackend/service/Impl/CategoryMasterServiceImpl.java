package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.CategoryMasterRepository;
import com.dmsBackend.service.CategoryMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CategoryMasterServiceImpl implements CategoryMasterService {
    @Autowired
    CategoryMasterRepository categoryMasterRepository;
    @Override
    public CategoryMaster savecategoryMaster(CategoryMaster categoryMaster) {

        categoryMaster.setCreatedOn(Helper.getCurrentTimeStamp());
        categoryMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        return categoryMasterRepository.save(categoryMaster);
    }

    @Override
    public CategoryMaster updateCategoryMaster(CategoryMaster categoryMaster, Integer id) {
        Optional<CategoryMaster> categoryMasterOptional = categoryMasterRepository.findById(id);

        if (categoryMasterOptional.isPresent()) {
            CategoryMaster categoryMaster1 = categoryMasterOptional.get();
            categoryMaster1.setName(categoryMaster.getName());
            categoryMaster1.setUpdatedOn(Helper.getCurrentTimeStamp());

            return categoryMasterRepository.save(categoryMaster1);
        }else {
            throw new ResourceNotFoundException("CategoryMaster not found for ","Id" ,id);
        }
    }

    @Override
    public void deleteByIdCategoryMaster(Integer id) {
        this.categoryMasterRepository.deleteById(id);

    }

    @Override
    public List<CategoryMaster> findAllCategoryMaster() {
        return categoryMasterRepository.findAll()   ;
    }

    @Override
    public Optional<CategoryMaster> findCategoryMasterById(Integer id) {
        return categoryMasterRepository.findById(id);
    }

    @Override
    public CategoryMaster findByIdCate(Integer id) {
        return categoryMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("CategoryMaster not found","Id",id));
    }
}
