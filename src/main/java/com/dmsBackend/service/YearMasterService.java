package com.dmsBackend.service;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.YearMaster;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface YearMasterService {
    YearMaster saveYearMaster(YearMaster yearMaster);
    YearMaster updateYearMaster(YearMaster yearMaster,Integer id);
    void deleteByIdYearMaster(Integer id);
    List<YearMaster> findAllYearMaster();
    Optional<YearMaster> findYearMasterById(Integer id);
    YearMaster findByIdyear(Integer id);


}
