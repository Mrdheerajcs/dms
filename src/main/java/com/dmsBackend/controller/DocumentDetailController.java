package com.dmsBackend.controller;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.exception.ResourceNotFoundException;
import com.dmsBackend.payloads.ApiResponse;
import com.dmsBackend.service.DocumentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/DocumentDetail")
@CrossOrigin("http://localhost:3000")
public class DocumentDetailController {

    @Autowired
    DocumentDetailService documentDetailService;




    @PostMapping("/save/{path}/")
    public ResponseEntity<DocumentDetails> createRoleMaster(@RequestBody DocumentDetails documentDetails){
        DocumentDetails savedocumentdetail = this.documentDetailService.saveDocumentDetails(documentDetails);
        return new ResponseEntity<DocumentDetails>(savedocumentdetail, HttpStatus.CREATED);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<DocumentDetails> updateDocumentDetail(@PathVariable Integer id, @RequestBody DocumentDetails documentDetails) {
        try {
            DocumentDetails updatedocumentdetail = this.documentDetailService.updateDocumentDetails(documentDetails, id);
            return new ResponseEntity<>(updatedocumentdetail, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DocumentDetails> deletebyIdDocumentDetails(@PathVariable Integer id) {
        this.documentDetailService.deleteByIdDocumentDetails(id);
        return new ResponseEntity(new ApiResponse("DocumentDetails deleted successfully", true), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<DocumentDetails>> findAllDocumentDetails() {
        List<DocumentDetails> allDocumentDetails = this.documentDetailService.findAllDocumentDetails();
        return new ResponseEntity(allDocumentDetails, HttpStatus.OK);
    }


    @GetMapping("/findById/{id}")
    public ResponseEntity<DocumentDetails> findByIdDocumentDetails(@PathVariable Integer id) {
        Optional<DocumentDetails> documentDetailsById = documentDetailService.findDocumentDetailsById(id);
        return documentDetailsById.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
