package org.example.tourist.services;
import org.example.tourist.models.User;
import org.example.tourist.models.Booking;
import org.example.tourist.BookingStatus;
import org.example.tourist.models.TourPackage;
import org.example.tourist.repositories.BookingRepository;
import org.example.tourist.models.Cart;
import org.example.tourist.BookingDto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final Cart cart;

    @Autowired
    public BookingService(BookingRepository bookingRepository, Cart cart) {
        this.bookingRepository = bookingRepository;
        this.cart = cart;
    }

    /**
     * Создать новое бронирование.
     *
     * @param bookingDto DTO для создания бронирования
     * @throws RuntimeException если корзина пуста
     */
    public void createBooking(BookingDto bookingDto, User user) {
        List<TourPackage> tourPackages = new ArrayList<>(cart.getCartItems());
        if (tourPackages.isEmpty()) {
            throw new RuntimeException("Корзина пуста! Невозможно создать бронирование.");
        }
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTourPackages(tourPackages);
        booking.setBookingDate(new Date());
        booking.setTotalAmount(bookingDto.getTotalAmount());
        booking.setStatus(BookingStatus.CREATED); // Предполагаем, что BookingStatus.CREATED соответствует OrderStatus.СОЗДАН
        bookingRepository.save(booking);
        cart.clearCart();
    }


    /**
     * Получить все бронирования.
     *
     * @return список бронирований
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Получить бронирование по ID.
     *
     * @param id ID бронирования
     * @return бронирование
     * @throws RuntimeException если бронирование не найдено
     */
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));
    }

    /**
     * Обновить статус бронирования.
     *
     * @param id     ID бронирования
     * @param status новый статус
     * @throws RuntimeException если бронирование не найдено или статус не может быть обновлен
     */
    public void updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = getBookingById(id);
        // Добавьте логику проверки допустимых переходов статусов, если необходимо
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    /**
     * Подтвердить бронирование.
     *
     * @param id ID бронирования
     * @throws RuntimeException если бронирование не найдено или уже подтверждено/отменено
     */
    public void confirmBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() != BookingStatus.CREATED) {
            throw new RuntimeException("Невозможно подтвердить бронирование в текущем статусе");
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
    }

    /**
     * Отменить бронирование.
     *
     * @param id ID бронирования
     * @throws RuntimeException если бронирование не найдено или уже завершено
     */
    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Невозможно отменить завершенное бронирование");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    /**
     * Удалить бронирование по ID
     *
     * @param bookingId ID бронирования
     * @return true, если удаление успешно, иначе false
     */
    public boolean deleteBooking(Long bookingId) {
        if (bookingRepository.existsById(bookingId)) {
            bookingRepository.deleteById(bookingId);
            return true;
        }
        return false;
    }

}
