package org.example.tourist.services;

import org.example.tourist.repositories.BookingRepository;
import org.example.tourist.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для получения статистики пользователей и бронирований.
 * Включает методы для получения общего числа пользователей, статистики по ролям пользователей и статистики по бронированиям.
 */
@Service
public class StatisticsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    /**
     * Конструктор для инициализации сервисов {@link UserRepository} и {@link BookingRepository}.
     *
     * @param userRepository репозиторий пользователей
     * @param bookingRepository репозиторий бронирований
     */
    public StatisticsService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Получает общее количество пользователей в системе.
     *
     * @return общее количество пользователей
     */
    public long getTotalUsers() {
        return userRepository.count();
    }

    /**
     * Получает количество пользователей, сгруппированных по ролям.
     * Статистика возвращается в виде карты, где ключ — это имя роли, а значение — количество пользователей с этой ролью.
     *
     * @return карта с количеством пользователей по каждой роли
     */
    public Map<String, Long> getUsersByRole() {
        List<Object[]> result = userRepository.countUsersByRole();
        Map<String, Long> usersByRole = new HashMap<>();
        for (Object[] row : result) {
            usersByRole.put((String) row[0], (Long) row[1]);
        }
        return usersByRole;
    }

    /**
     * Получает статистику по бронированиям, сгруппированную по месяцам.
     * Статистика возвращается в виде карты, где ключ — это месяц (в виде строки), а значение — количество бронирований в этом месяце.
     *
     * @return карта с количеством бронирований по месяцам
     */
    public Map<String, Long> getBookingStats() {
        List<Object[]> result = bookingRepository.countBookingsPerMonth();
        Map<String, Long> bookingStats = new HashMap<>();

        for (Object[] row : result) {
            // Приводим Integer (месяц) к String
            String month = String.valueOf(row[0]);
            Long count = (Long) row[1];
            bookingStats.put(month, count);
        }
        return bookingStats;
    }
}
