package org.example.tourist.repositories;


import org.example.tourist.models.Booking;
import org.example.tourist.BookingStatus;
import org.example.tourist.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(BookingStatus status);

    List<Booking> findByUser(User user);


    @Query("SELECT MONTH(b.bookingDate), COUNT(b) FROM Booking b GROUP BY MONTH(b.bookingDate)")
    List<Object[]> countBookingsPerMonth();


    // Метод для подсчета бронирований для конкретного турпакета
    @Query("SELECT COUNT(b) FROM Booking b JOIN b.tourPackages tp WHERE tp.id = :tourPackageId")
    long countBookingsByTourPackageId(Long tourPackageId);
}