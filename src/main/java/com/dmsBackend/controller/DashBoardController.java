package com.dmsBackend.controller;

import com.dmsBackend.entity.DepartmentMaster;
import com.dmsBackend.response.DashboardResponse;
import com.dmsBackend.service.DashboardService;
import com.dmsBackend.service.DepartmentMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Dashboard")
@CrossOrigin("http://localhost:3000")
public class DashBoardController {
    @Autowired
    DashboardService dashboardService;

    @GetMapping("GetAllCountsForDashBoard")
    public ResponseEntity<DashboardResponse> getAllDashBoardCounts(@RequestParam String employeeId) {
        DashboardResponse dashboardResponse = this.dashboardService.getAllUsers(employeeId);
        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }

}
