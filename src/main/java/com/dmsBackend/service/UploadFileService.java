package com.dmsBackend.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@Service
public interface UploadFileService {
    String uploadImage(String path, MultipartFile file)throws IOException;
    InputStream getResource(String path, String fileName)throws FileNotFoundException;
}
