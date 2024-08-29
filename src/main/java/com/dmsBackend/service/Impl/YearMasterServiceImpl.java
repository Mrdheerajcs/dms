package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.YearMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.Helper;
import com.dmsBackend.repository.YearMasterRepository;
import com.dmsBackend.service.YearMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class YearMasterServiceImpl implements YearMasterService {
    @Autowired
    YearMasterRepository yearMasterRepository;

    @Override
    public YearMaster saveYearMaster(YearMaster yearMaster) {
        yearMaster.setCreatedOn(Helper.getCurrentTimeStamp());
        yearMaster.setUpdatedOn(Helper.getCurrentTimeStamp());
        return yearMasterRepository.save(yearMaster);
    }

    @Override
    public YearMaster updateYearMaster(YearMaster yearMaster, Integer id) {
        Optional<YearMaster> yearMasterOptional = yearMasterRepository.findById(id);

        if (yearMasterOptional.isPresent()) {
            YearMaster yearMaster1 = yearMasterOptional.get();
            yearMaster1.setName(yearMaster.getName());
            yearMaster1.setUpdatedOn(Helper.getCurrentTimeStamp());

            return yearMasterRepository.save(yearMaster1);
        } else {
            throw new ResourceNotFoundException("CategoryMaster not found for ", "Id", id);
        }
    }

    @Override
    public void deleteByIdYearMaster(Integer id) {
        this.yearMasterRepository.deleteById(id);
    }

    @Override
    public List<YearMaster> findAllYearMaster() {
        return yearMasterRepository.findAll();
    }

    @Override
    public Optional<YearMaster> findYearMasterById(Integer id) {
        return yearMasterRepository.findById(id);
    }

    @Override
    public YearMaster findByIdyear(Integer id) {
        return yearMasterRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("year not found","Id",id));
    }
}
