package com.dmsBackend.response;

import com.dmsBackend.entity.DocumentHeader;

import java.util.List;

public class DocumentSaveRequest {
    private DocumentHeader documentHeader;
    private List<String> filePaths;

    // Getters and setters
    public DocumentHeader getDocumentHeader() {
        return documentHeader;
    }

    public void setDocumentHeader(DocumentHeader documentHeader) {
        this.documentHeader = documentHeader;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<String> filePaths) {
        this.filePaths = filePaths;
    }
}
