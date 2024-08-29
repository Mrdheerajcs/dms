package com.dmsBackend.controller;

import com.dmsBackend.service.DocumentDetailService;
import com.dmsBackend.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/uploadFile")
@CrossOrigin("http://localhost:3000")
public class UploadFileController {

    @Value("images/")
    private String path;

    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    DocumentDetailService documentDetailService;

    @PostMapping("/File")
    public ResponseEntity<String> uploadFile(@RequestParam("images") MultipartFile image) throws IOException {
        String fileName = this.uploadFileService.uploadImage(path, image);
        return new ResponseEntity<>(fileName, HttpStatus.OK);
    }

}