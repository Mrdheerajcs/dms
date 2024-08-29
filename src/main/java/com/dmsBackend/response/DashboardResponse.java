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
}
