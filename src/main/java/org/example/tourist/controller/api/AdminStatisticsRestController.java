package org.example.tourist.controller.api;

import org.example.tourist.services.StatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
public class AdminStatisticsRestController {

    // Инжектируем сервис, который будет заниматься расчетом статистики
    private final StatisticsService statisticsService;

    // Конструктор контроллера для внедрения зависимости
    public AdminStatisticsRestController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Получение общего количества пользователей.
     * Этот эндпоинт возвращает общее количество пользователей в системе.
     * Возвращает HTTP-ответ со статусом 200 и значением общее количество пользователей.
     *
     * @return ResponseEntity с количеством пользователей
     */
    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        // Получаем количество пользователей из StatisticsService и возвращаем его в ответе
        return ResponseEntity.ok(statisticsService.getTotalUsers());
    }

    /**
     * Получение статистики пользователей по ролям.
     * Этот эндпоинт возвращает статистику по пользователям, сгруппированным по ролям.
     * Возвращает HTTP-ответ со статусом 200 и Map, где ключ — роль, а значение — количество пользователей с этой ролью.
     *
     * @return ResponseEntity с Map, содержащим статистику по ролям
     */
    @GetMapping("/users-by-role")
    public ResponseEntity<Map<String, Long>> getUsersByRole() {
        // Получаем статистику по ролям пользователей и возвращаем в ответе
        return ResponseEntity.ok(statisticsService.getUsersByRole());
    }

    /**
     * Получение статистики по бронированиям.
     * Этот эндпоинт возвращает статистику по бронированиям в системе, например, по статусам.
     * Возвращает HTTP-ответ со статусом 200 и Map, где ключ — статус бронирования, а значение — количество бронирований в этом статусе.
     *
     * @return ResponseEntity с Map, содержащим статистику по бронированиям
     */
    @GetMapping("/booking-stats")
    public ResponseEntity<Map<String, Long>> getBookingStats() {
        // Получаем статистику по бронированиям и возвращаем в ответе
        return ResponseEntity.ok(statisticsService.getBookingStats());
    }
}

