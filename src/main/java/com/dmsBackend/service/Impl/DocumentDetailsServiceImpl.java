package com.dmsBackend.service.Impl;

import com.dmsBackend.entity.DocumentDetails;
import com.dmsBackend.entity.DocumentHeader;
import com.dmsBackend.repository.DocumentDetailsRepository;
import com.dmsBackend.repository.DocumentHeaderRepository;
import com.dmsBackend.service.DocumentDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentDetailsServiceImpl implements DocumentDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentDetailsServiceImpl.class);

    private final DocumentDetailsRepository documentDetailsRepository;
    private final DocumentHeaderRepository documentHeaderRepository;

    public DocumentDetailsServiceImpl(DocumentDetailsRepository documentDetailsRepository, DocumentHeaderRepository documentHeaderRepository) {
        this.documentDetailsRepository = documentDetailsRepository;
        this.documentHeaderRepository = documentHeaderRepository;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files, String category) {
        List<String> filePaths = new ArrayList<>();

        String year = String.valueOf(LocalDate.now().getYear());
        String month = String.format("%02d", LocalDate.now().getMonthValue());

        // Construct base directory based on year, month, and category
        String baseDir = "D:\\Dheeraj_Codes\\Backend\\Java\\Projects\\dms\\DocumentServer"
                + File.separator + year + File.separator + month + File.separator + category;

        // Create the directory if it doesn't exist
        File directory = new File(baseDir);
        if (!directory.exists() && !directory.mkdirs()) {
            logger.error("Failed to create directory: " + baseDir);
            throw new RuntimeException("Failed to create directory: " + baseDir);
        }

        // Iterate through each file and upload it
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                if (!file.getContentType().equalsIgnoreCase("application/pdf")) {
                    throw new RuntimeException("Only PDF files are allowed. Invalid file: " + file.getOriginalFilename());
                }

                try {
                    // Sanitize and rename the file to avoid collisions
                    String sanitizedFileName = System.currentTimeMillis() + "_"
                            + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9.]", "_");
                    String filePath = baseDir + File.separator + sanitizedFileName;
                    File serverFile = new File(filePath);

                    // Save file to disk
                    file.transferTo(serverFile);
                    filePaths.add(filePath);
                } catch (IOException e) {
                    logger.error("Failed to upload file: " + file.getOriginalFilename(), e);
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                }
            }
        }

        return filePaths;  // Return the paths of the uploaded files
    }


    @Override
    public void saveFileDetails(DocumentHeader documentHeader, List<String> filePaths) {
        for (String filePath : filePaths) {
            DocumentDetails documentDetails = new DocumentDetails();

            // Set document name and file path
            documentDetails.setDocName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            documentDetails.setPath(filePath);

            // Check if documentHeader is provided, otherwise handle accordingly
            if (documentHeader != null) {
                documentDetails.setDocumentHeader(documentHeader);
            }

            // Set timestamps for created and updated on
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            documentDetails.setUpdatedOn(currentTimestamp);
            documentDetails.setCreatedOn(currentTimestamp);

            // Save each file's details into the database
            documentDetailsRepository.save(documentDetails);
        }
    }


}
