package com.dmsBackend.service.Impl;

import com.dmsBackend.service.UploadFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UploadFileServiceImpl implements UploadFileService {
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // File name
        String name = file.getOriginalFilename();
        // abc.png

        // Generate a random name for the file
        String randomId = UUID.randomUUID().toString();
        String fileName1 = randomId.concat(name.substring(name.lastIndexOf(".")));

        // Full path
        String filePath = path + File.separator + fileName1;

        // Create folder if not created
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // Copy file
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Return the full file path
        return filePath;
    }
        @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        //db logic to return inputstream
        return is;
    }
}
