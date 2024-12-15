package org.example.tourist.controller;

import org.example.tourist.services.StatisticsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminStatisticsController {

    private final StatisticsService statisticsService;

    public AdminStatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Показать статистику сайта.
     */
    @GetMapping("/statistics")
    public String showStatisticsPage(Model model) {
        // Общее количество пользователей
        model.addAttribute("totalUsers", statisticsService.getTotalUsers());

        // Количество пользователей по ролям
        model.addAttribute("usersByRole", statisticsService.getUsersByRole());

        // Статистика бронирований
        model.addAttribute("bookingStats", statisticsService.getBookingStats());

        return "admin/statistics";
    }
}
