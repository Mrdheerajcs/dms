package com.dmsBackend.controller;

import com.dmsBackend.entity.CategoryMaster;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.entity.YearMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.YearMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/YearMaster")
@CrossOrigin("http://localhost:3000")
public class YearMasterController {
    @Autowired
    YearMasterService yearMasterService;

    @PostMapping("/save")
    public ResponseEntity<YearMaster> createYearMaster(@RequestBody YearMaster yearMaster) {
        YearMaster saveyearmaster = this.yearMasterService.saveYearMaster(yearMaster);
        return new ResponseEntity<YearMaster>(saveyearmaster, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<YearMaster> updateYearMaster(@PathVariable Integer id, @RequestBody YearMaster yearMaster) {
        try {
            YearMaster updatedyearMaster = yearMasterService.updateYearMaster(yearMaster, id);
            return new ResponseEntity<>(updatedyearMaster, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<YearMaster> deletebyIdYearMaster(@PathVariable Integer id) {
        this.yearMasterService.deleteByIdYearMaster(id);
        return new ResponseEntity(new ApiResponse("YearMaster deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<YearMaster>> findAllYearMaster() {
        List<YearMaster> allYearMasterMaster = this.yearMasterService.findAllYearMaster();
        return new ResponseEntity(allYearMasterMaster, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<YearMaster> findByIdYearMaster(@PathVariable Integer id) {
        Optional<YearMaster> yearMaster = yearMasterService.findYearMasterById(id);
        return yearMaster.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }

    @PutMapping("updatestatus/{id}")
    public ResponseEntity<YearMaster> updateRoleStatus(@PathVariable Integer id, @RequestBody YearMaster yearMaster) {
        try {
            YearMaster yearMaster1 = yearMasterService.updateStatus(id,yearMaster.getIsActive());
            return new ResponseEntity<>(yearMaster1, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
