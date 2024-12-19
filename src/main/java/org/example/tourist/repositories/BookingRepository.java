package org.example.tourist.repositories;

import org.example.tourist.models.Booking;
import org.example.tourist.BookingStatus;
import org.example.tourist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Booking}.
 * Содержит методы для выполнения операций с бронированиями, таких как поиск по статусу,
 * пользователю, а также методы для статистики по бронированиям.
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Находит все бронирования по статусу.
     *
     * @param status статус бронирования
     * @return список бронирований с заданным статусом
     */
    List<Booking> findByStatus(BookingStatus status);

    /**
     * Находит все бронирования, связанные с указанным пользователем.
     *
     * @param user пользователь, чьи бронирования нужно найти
     * @return список бронирований для указанного пользователя
     */
    List<Booking> findByUser(User user);

    /**
     * Выполняет запрос для подсчета количества бронирований по месяцам.
     * Возвращает список массивов, где первый элемент - месяц, второй - количество бронирований.
     *
     * @return список объектов, содержащих месяц и количество бронирований в этот месяц
     */
    @Query("SELECT MONTH(b.bookingDate), COUNT(b) FROM Booking b GROUP BY MONTH(b.bookingDate)")
    List<Object[]> countBookingsPerMonth();

    /**
     * Выполняет запрос для подсчета бронирований для конкретного туристического пакета.
     *
     * @param tourPackageId идентификатор туристического пакета
     * @return количество бронирований для указанного туристического пакета
     */
    @Query("SELECT COUNT(b) FROM Booking b JOIN b.tourPackages tp WHERE tp.id = :tourPackageId")
    long countBookingsByTourPackageId(Long tourPackageId);
}
