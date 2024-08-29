package com.dmsBackend.controller;

import com.dmsBackend.service.DocumentService;
import com.dmsBackend.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/documents")
@CrossOrigin("http://localhost:3000")
public class DocumentSavedWithFileControlller {

    @Autowired
    private DocumentService documentService;

    @Autowired
    private UploadFileService uploadFileService;

    @PostMapping("/save/{empId}")
    public ResponseEntity<String> saveDocument(@RequestParam("file") MultipartFile file,
                                               @RequestParam("title") String title,
                                               @RequestParam("subject") String subject,
                                               @RequestParam("documentHeaderCategoryId") Integer documentHeaderCategoryId,
                                               @RequestParam("documentHeaderYearId") Integer documentHeaderYearId,
                                               @RequestParam("documentHeaderTypeId") Integer documentHeaderTypeId,
                                               @PathVariable("empId") Integer empId
                                              ) throws IOException {
        // Use a predefined path for file uploads
        String uploadPath = "images/";

        // Upload the file and get the full path
        String filePath = uploadFileService.uploadImage(uploadPath, file);

        // Call the service to save document details and header
        documentService.saveDocument(empId,filePath,title,subject,
                documentHeaderCategoryId,
                documentHeaderYearId, documentHeaderTypeId);

        return ResponseEntity.ok("Document details and header saved successfully.");
    }

}
