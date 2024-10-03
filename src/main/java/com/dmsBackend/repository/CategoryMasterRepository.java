package com.dmsBackend.repository;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.RoleMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMasterRepository extends JpaRepository<CategoryMaster,Integer> {

    List<CategoryMaster> findByActive(boolean active);
}
