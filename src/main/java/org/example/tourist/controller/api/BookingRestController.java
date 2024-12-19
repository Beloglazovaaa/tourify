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

    // Инжектируем сервисы для работы с бронированиями и пользователями
    public BookingRestController(BookingService bookingService, UserRepository userRepository) {
        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    /**
     * Создание нового бронирования.
     * Этот метод принимает объект DTO для бронирования и создает бронирование,
     * ассоциируя его с текущим пользователем (который аутентифицирован).
     * Возвращается статус 201 Created с объектом бронирования.
     *
     * @param bookingDto - данные для нового бронирования
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с созданным объектом бронирования
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDto bookingDto, Principal principal) {
        // Получаем имя текущего пользователя из principal
        String username = principal.getName();
        // Ищем пользователя в базе данных по имени
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        // Создаем новое бронирование через сервис
        Booking booking = bookingService.createBooking(bookingDto, user);
        // Возвращаем ответ с созданным бронированием и статусом 201
        return ResponseEntity.status(201).body(booking);
    }

    /**
     * Получить список всех бронирований.
     * Этот метод возвращает все бронирования в системе.
     * Ответ кэшируется на 60 секунд для оптимизации производительности.
     *
     * @return ResponseEntity с коллекцией всех бронирований
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .body(bookings);
    }

    /**
     * HEAD-запрос для бронирований.
     * Этот метод возвращает только заголовки ответа без тела, полезен для получения метаданных.
     *
     * @return ResponseEntity без тела, только с HTTP-заголовками
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headBookings() {
        return ResponseEntity.ok().build();
    }

    /**
     * OPTIONS-запрос для бронирований.
     * Этот метод возвращает список поддерживаемых HTTP-методов для ресурса "/api/bookings".
     *
     * @return ResponseEntity с заголовком Allow, указывающим доступные методы
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsBookings() {
        HttpHeaders headers = new HttpHeaders();
        // Устанавливаем заголовок Allow с перечнем доступных методов
        headers.add("Allow", "GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Обновить статус бронирования (PUT).
     * Этот метод позволяет полностью обновить статус существующего бронирования.
     *
     * @param bookingId - идентификатор бронирования
     * @param status - новый статус бронирования
     * @return ResponseEntity без тела (204 No Content), если обновление прошло успешно
     */
    @PutMapping("/{bookingId}/status")
    public ResponseEntity<Void> updateBookingStatus(@PathVariable Long bookingId, @RequestBody String status) {
        // Преобразуем строку статуса в перечисление
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
        // Обновляем статус через сервис
        bookingService.updateBookingStatus(bookingId, newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Частично обновить статус бронирования (PATCH).
     * Этот метод частично обновляет статус бронирования.
     *
     * @param bookingId - идентификатор бронирования
     * @param status - новый статус бронирования
     * @return ResponseEntity без тела (204 No Content), если обновление прошло успешно
     */
    @PatchMapping("/{bookingId}/status")
    public ResponseEntity<Void> patchBookingStatus(@PathVariable Long bookingId, @RequestBody String status) {
        // Преобразуем строку статуса в перечисление
        BookingStatus newStatus = BookingStatus.valueOf(status.toUpperCase());
        // Частично обновляем статус через сервис
        bookingService.updateBookingStatus(bookingId, newStatus);
        return ResponseEntity.noContent().build();
    }

    /**
     * Подтвердить бронирование.
     * Этот метод подтверждает бронирование, выполняя соответствующее действие.
     *
     * @param id - идентификатор бронирования
     * @return ResponseEntity без тела (204 No Content)
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id) {
        // Подтверждаем бронирование через сервис
        bookingService.confirmBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Отменить бронирование.
     * Этот метод отменяет бронирование, выполняя соответствующее действие.
     *
     * @param id - идентификатор бронирования
     * @return ResponseEntity без тела (204 No Content)
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        // Отменяем бронирование через сервис
        bookingService.cancelBooking(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удалить бронирование.
     * Этот метод удаляет бронирование по его идентификатору.
     * Возвращает статус 204, если удаление прошло успешно, или 404, если бронирование не найдено.
     *
     * @param bookingId - идентификатор бронирования
     * @return ResponseEntity с кодом 204 или 404
     */
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long bookingId) {
        // Удаляем бронирование через сервис и проверяем успех
        boolean success = bookingService.deleteBooking(bookingId);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    /**
     * Получить бронирования текущего пользователя.
     * Этот метод возвращает список бронирований текущего аутентифицированного пользователя.
     * Ответ кэшируется на 30 секунд для оптимизации производительности.
     *
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с коллекцией бронирований пользователя
     */
    @GetMapping("/my")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        // Получаем имя пользователя и находим его в базе
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
        // Получаем все бронирования пользователя
        List<Booking> bookings = bookingService.getBookingsByUser(user);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.SECONDS).cachePublic())
                .body(bookings);
    }
}


