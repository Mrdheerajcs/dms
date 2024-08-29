package com.dmsBackend.controller;

import com.dmsBackend.entity.BranchMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.BranchMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/branchmaster")
@CrossOrigin("http://localhost:3000")
public class BranchMasterController {

    @Autowired
    private BranchMasterService branchMasterService;


    @PostMapping("/save")
    public ResponseEntity<BranchMaster> createBranchMaster(@RequestBody BranchMaster branchMaster) {
        BranchMaster savebranchmaster = this.branchMasterService.saveBranchMaster(branchMaster);
        return new ResponseEntity<BranchMaster>(savebranchmaster, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<BranchMaster> updateBranch(@PathVariable Integer id, @RequestBody BranchMaster branchMaster) {
        try {
            BranchMaster updatedBranch = branchMasterService.updateBranchMaster(branchMaster, id);
            return new ResponseEntity<>(updatedBranch, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BranchMaster> deletebyIdBranchMaster(@PathVariable Integer id) {
        this.branchMasterService.deleteByIdBranchMaster(id);
        return new ResponseEntity(new ApiResponse("Branchmaster deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<BranchMaster>> findAllBranchMaster() {
        List<BranchMaster> allBranchMaster = this.branchMasterService.findAllBranchMaster();
        return new ResponseEntity(allBranchMaster, HttpStatus.OK);
    }
    

    @GetMapping("/findById/{id}")
    public ResponseEntity<BranchMaster> findByIdBranchMaster(@PathVariable Integer id) {
        Optional<BranchMaster> branchMaster = branchMasterService.findBranchMasterById(id);
        return branchMaster.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));


    }
}