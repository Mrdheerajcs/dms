package com.dmsBackend.response;

import lombok.Data;

@Data
public class DashboardResponse {
    private long totalUser;
    private long totalDocument;
    private long pendingDocument;
    private long storageUsed;
    private long totalBranches;
    private long totalDepartment;
    private long totalRoles;
    private long documentType;
    private long annualYear;
    private long totalCategories;

    private long totalApprovedDocuments;
    private long totalRejectedDocuments;
    private long totalPendingDocuments;
    private long totalApprovedDocumentsById;
    private long totalRejectedDocumentsById;
    private long totalPendingDocumentsById;
    private long totalDocumentsById;

    private long totalNullEmployeeType;

    private long totalApprovedStatusDocById;
    private long totalRejectedStatusDocById;
}
