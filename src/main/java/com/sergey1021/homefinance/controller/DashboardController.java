package com.sergey1021.homefinance.controller;

import com.sergey1021.homefinance.dto.SummaryDTO;
import com.sergey1021.homefinance.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public SummaryDTO getSummary() {
        return dashboardService.getSummary();
    }
}
