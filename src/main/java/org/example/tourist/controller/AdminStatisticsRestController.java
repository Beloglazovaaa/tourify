package org.example.tourist.controller;

import org.example.tourist.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsRestController {

    private final StatisticsService statisticsService;

    public AdminStatisticsRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        return ResponseEntity.ok(statisticsService.getTotalUsers());
    }

    @GetMapping("/users-by-role")
    public ResponseEntity<Map<String, Long>> getUsersByRole() {
        return ResponseEntity.ok(statisticsService.getUsersByRole());
    }

    @GetMapping("/booking-stats")
    public ResponseEntity<Map<String, Long>> getBookingStats() {
        return ResponseEntity.ok(statisticsService.getBookingStats());
    }
}
