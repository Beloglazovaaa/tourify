package org.example.tourist.controller.api;

import org.example.tourist.BookingDto;
import org.example.tourist.BookingStatus;
import org.example.tourist.models.Booking;
import org.example.tourist.models.User;
import org.example.tourist.repositories.UserRepository;
import org.example.tourist.services.BookingService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.example.tourist.repositories.BookingRepository;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/bookings")
public class BookingRestController {

    private final BookingService bookingService;
    private final UserRepository userRepository;

    public BookingRestController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;

    }

    /**
     * Создать новое бронирование (POST).
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDto bookingDto, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        Booking booking = bookingService.createBooking(bookingDto, user);
        return ResponseEntity.status(201).body(booking);
    }


    /**
     * Получить список всех бронирований (GET).
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .body(bookings);
    }

    /**
     * HEAD для бронирований.
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headBookings() {
        return ResponseEntity.ok().build();
    }

    /**
     * OPTIONS для бронирований.
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsBookings() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Обновить статус бронирования (PUT).
     */
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Void> updateBookingStatus(@PathVariable Long bookingId, @RequestBody String status) {
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
        bookingService.updateBookingStatus(bookingId, newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Частично обновить статус бронирования (PATCH).
     */
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<Void> patchBookingStatus(@PathVariable Long bookingId, @RequestBody String status) {
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
        bookingService.updateBookingStatus(bookingId, newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Подтвердить бронирование (POST - действие).
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        bookingService.confirmBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Отменить бронирование (POST - действие).
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удалить бронирование (DELETE).
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        boolean success = bookingService.deleteBooking(bookingId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Получить бронирования текущего пользователя (GET).
     */
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS).cachePublic())
                .body(bookings);
    }
}

