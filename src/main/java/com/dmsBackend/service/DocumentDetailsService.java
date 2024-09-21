package com.dmsBackend.service;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentDetailsService {


//    List<String> uploadFiles(List<MultipartFile> files, Integer documentHeaderId);
    List<String> uploadFiles(List<MultipartFile> files, String category);

    void saveFileDetails(DocumentHeader documentHeader, List<String> filePaths);

//    void saveFileDetails(DocumentHeader documentHeader, List<String> filePaths);
}
