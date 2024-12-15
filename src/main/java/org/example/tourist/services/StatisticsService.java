package org.example.tourist.services;

import org.example.tourist.repositories.BookingRepository;
import org.example.tourist.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public StatisticsService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    /**
     * Получить общее количество пользователей.
     */
    public long getTotalUsers() {
        return userRepository.count();
    }

    /**
     * Получить количество пользователей по ролям.
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
     * Получить статистику бронирований.
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
