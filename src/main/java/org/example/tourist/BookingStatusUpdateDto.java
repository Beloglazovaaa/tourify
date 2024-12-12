package org.example.tourist;

import org.example.tourist.BookingStatus;

public class BookingStatusUpdateDto {

    private Long bookingId;
    private BookingStatus status;

    // Геттеры и Сеттеры
    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}

