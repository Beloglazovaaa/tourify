package org.example.tourist.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

/**
 * Сервис для работы с бронированиями пользователей.
 * Предоставляет методы для создания, получения, обновления и удаления бронирований.
 */
@Service
public class BookingService {

    // Внедрение EntityManager для работы с управлением сущностями
    @PersistenceContext
    private EntityManager entityManager;

    private final BookingRepository bookingRepository;
    private final Cart cart;

    /**
     * Конструктор для инициализации сервисов и репозиториев.
     *
     * @param bookingRepository репозиторий для работы с сущностями Booking
     * @param cart корзина, содержащая добавленные туры для бронирования
     */
    @Autowired
    public BookingService(BookingRepository bookingRepository, Cart cart) {
        this.bookingRepository = bookingRepository;
        this.cart = cart;
    }

    /**
     * Создает новое бронирование для пользователя.
     * Бронирование включает выбранные туры из корзины.
     * После создания бронирования корзина очищается.
     *
     * @param bookingDto объект DTO, содержащий данные для создания бронирования
     * @param user текущий пользователь, который создает бронирование
     * @return объект {@link Booking}, представляющий созданное бронирование
     * @throws RuntimeException если корзина пуста и бронирование не может быть создано
     */
    @Transactional
    public Booking createBooking(BookingDto bookingDto, User user) {
        List<TourPackage> tourPackages = new ArrayList<>(cart.getCartItems());

        if (tourPackages.isEmpty()) {
            throw new RuntimeException("Корзина пуста! Невозможно создать бронирование.");
        }

        // Применяем merge для того, чтобы все объекты TourPackage стали отслеживаемыми в текущей сессии
        tourPackages = tourPackages.stream()
                .map(t -> entityManager.merge(t))  // Использование merge для обновления отсоединённых объектов
                .collect(Collectors.toList());

        // Создаем объект бронирования и устанавливаем его свойства
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setTourPackages(tourPackages);
        booking.setBookingDate(new Date());
        booking.setTotalAmount(bookingDto.getTotalAmount());
        booking.setStatus(BookingStatus.CREATED);

        // Сохраняем бронирование в базе данных
        bookingRepository.save(booking);

        // Очищаем корзину после оформления бронирования
        cart.clearCart();

        return booking;
    }

    /**
     * Получить все бронирования.
     *
     * @return список всех бронирований
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Получить бронирование по ID.
     *
     * @param id ID бронирования
     * @return объект {@link Booking}, соответствующий данному ID
     * @throws RuntimeException если бронирование с указанным ID не найдено
     */
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));
    }

    /**
     * Обновить статус существующего бронирования.
     *
     * @param id ID бронирования
     * @param status новый статус бронирования
     * @throws RuntimeException если бронирование с указанным ID не найдено или статус не может быть обновлен
     */
    public void updateBookingStatus(Long id, BookingStatus status) {
        Booking booking = getBookingById(id);
        booking.setStatus(status);
        bookingRepository.save(booking);
    }

    /**
     * Подтвердить бронирование.
     * Статус бронирования изменится на {@link BookingStatus#CONFIRMED}.
     *
     * @param id ID бронирования
     * @throws RuntimeException если бронирование уже подтверждено или отменено
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
     * Статус бронирования изменится на {@link BookingStatus#CANCELLED}.
     *
     * @param id ID бронирования
     * @throws RuntimeException если бронирование уже завершено или не может быть отменено
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
     * Удалить бронирование по ID.
     *
     * @param bookingId ID бронирования, которое нужно удалить
     * @return true, если удаление прошло успешно, иначе false
     * @throws RuntimeException если бронирование с указанным ID не найдено
     */
    @Transactional
    public boolean deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        // Очистка связей с турпакетами, прежде чем удалить
        booking.getTourPackages().clear();
        bookingRepository.delete(booking);
        return true;
    }

    /**
     * Получить все бронирования для конкретного пользователя.
     *
     * @param user пользователь, для которого нужно получить бронирования
     * @return список бронирований этого пользователя
     */
    public List<Booking> getBookingsByUser(User user) {
        return bookingRepository.findByUser(user);
    }
}
