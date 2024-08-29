package com.dmsBackend.controller;

import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.entity.RoleMaster;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.RoleMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/RoleMaster")
@CrossOrigin("http://localhost:3000")
public class RoleMasterController {
    @Autowired
    RoleMasterService roleMasterService;

    @PostMapping("/save")
    public ResponseEntity<RoleMaster> createRoleMaster(@RequestBody RoleMaster roleMaster){
        RoleMaster saveroleMaster = this.roleMasterService.saveRoleMaster(roleMaster);
        return new ResponseEntity<RoleMaster>(saveroleMaster, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<RoleMaster> updateRoleMaster(@PathVariable Integer id, @RequestBody RoleMaster roleMaster) {
        try {
            RoleMaster updaterolemaster = this.roleMasterService.updateRoleMaster(roleMaster, id);
            return new ResponseEntity<>(updaterolemaster, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RoleMaster> deletebyIdRoleMaster(@PathVariable Integer id) {
        this.roleMasterService.deleteByIdRoleMaster(id);
        return new ResponseEntity(new ApiResponse("RoleMaster deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RoleMaster>> findAllRoleMaster() {
        List<RoleMaster> allRoleMaster = this.roleMasterService.findAllRoleMaster();
        return new ResponseEntity(allRoleMaster, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<RoleMaster> findByIdRoleMaster(@PathVariable Integer id) {
        Optional<RoleMaster> roleMaster = roleMasterService.findRoleMasterById(id);
        return roleMaster.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("updatestatus/{id}")
    public ResponseEntity<RoleMaster> updateRoleStatus(@PathVariable Integer id, @RequestBody RoleMaster roleMaster) {
        try {
            RoleMaster roleMaster1 = roleMasterService.updateStatus(id,roleMaster.getIsActive());
            return new ResponseEntity<>(roleMaster1, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
