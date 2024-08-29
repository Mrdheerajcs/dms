package com.dmsBackend.controller;

import com.dmsBackend.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/uploadFile")
@CrossOrigin("http://localhost:3000")
public class FileUploadController {
    @Autowired
    UploadFileService uploadFileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(String path ,@RequestParam("file") MultipartFile file) throws IOException {
        String s = this.uploadFileService.uploadImage(path,file);
        return new ResponseEntity<>(s, HttpStatus.CREATED);
    }

    @GetMapping("/view/{filename:.+}")
    public InputStream viewFile(String path, String filename) throws FileNotFoundException {
        return uploadFileService.getResource(path,filename);
    }
}
