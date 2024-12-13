package org.example.tourist.controller;

import org.example.tourist.models.Booking;
import org.example.tourist.BookingStatus;
import org.example.tourist.services.BookingService;
import org.example.tourist.BookingDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * Создать новое бронирование.
     *
     * @param bookingDto DTO для создания бронирования
     * @param principal  информация о текущем пользователе
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление в зависимости от роли пользователя
     */
    @PostMapping("/create")
    public String createBooking(@ModelAttribute BookingDto bookingDto, Principal principal, RedirectAttributes redirectAttributes) {
        bookingService.createBooking(bookingDto);

        Authentication authentication = (Authentication) principal;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            redirectAttributes.addFlashAttribute("successMessage", "Бронирование успешно создано.");
            return "redirect:/bookings";
        } else if (userDetails.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"))) {
            redirectAttributes.addFlashAttribute("successMessage", "Бронирование успешно создано.");
            return "redirect:/tour-packages";
        }

        return "redirect:/";
    }

    /**
     * Показать страницу с бронированиями.
     *
     * @param model модель для представления
     * @return название представления "bookings"
     */
    @GetMapping
    public String bookingsPage(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        model.addAttribute("statuses", BookingStatus.values());
        return "bookings";
    }

    /**
     * Обновить статус бронирования из таблицы.
     *
     * @param bookingId ID бронирования
     * @param status    новый статус
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу бронирований
     */
    @PostMapping("/updateStatus")
    public String updateBookingStatusFromTable(@RequestParam Long bookingId, @RequestParam String status, RedirectAttributes redirectAttributes) {
        try {
            BookingStatus newStatus = BookingStatus.valueOf(status);
            bookingService.updateBookingStatus(bookingId, newStatus);
            redirectAttributes.addFlashAttribute("successMessage", "Статус бронирования обновлен.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Некорректный статус бронирования.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }

    /**
     * Подтвердить бронирование.
     *
     * @param id ID бронирования
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу бронирований
     */
    @PostMapping("/confirm/{id}")
    public String confirmBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.confirmBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Бронирование подтверждено.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }

    /**
     * Отменить бронирование.
     *
     * @param id ID бронирования
     * @param redirectAttributes атрибуты для перенаправления
     * @return перенаправление на страницу бронирований
     */
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookingService.cancelBooking(id);
            redirectAttributes.addFlashAttribute("successMessage", "Бронирование отменено.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/bookings";
    }
}
