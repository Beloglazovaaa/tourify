package org.example.tourist.controller;


import org.example.tourist.services.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для административной панели.
 * Управление пользователями и статистика.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    // Внедрение сервиса пользователя для управления пользователями
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Страница управления пользователями.
     * Отображает список всех пользователей системы.
     *
     * @param model модель для передачи данных на страницу
     * @return имя шаблона страницы управления пользователями
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getManageUsersPage(Model model) {
        // Добавляем в модель всех пользователей для отображения в шаблоне
        model.addAttribute("users", userService.getAllUsers());
        return "admin/manage-users";  // Название HTML шаблона для страницы управления пользователями
    }

    /**
     * Обновление роли пользователя.
     * Этот метод обновляет роль пользователя на новую.
     *
     * @param userId   ID пользователя, роль которого нужно обновить
     * @param newRole  новая роль для пользователя
     * @return перенаправление на страницу управления пользователями
     */
    @PostMapping("/users/update-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateUserRole(@RequestParam Long userId, @RequestParam String newRole, Model model) {
        try {
            // Обновляем роль пользователя через сервис
            userService.updateUserRole(userId, newRole);
        } catch (IllegalArgumentException ex) {
            // Добавляем сообщение об ошибке и перенаправляем на страницу ошибок
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/400";  // Путь к вашей странице 400
        }
        // Перенаправляем обратно на страницу управления пользователями
        return "redirect:/admin/users";
    }

    /**
     * Удаление пользователя.
     * Этот метод удаляет пользователя из системы.
     *
     * @param userId ID пользователя, которого нужно удалить
     * @return перенаправление на страницу управления пользователями
     */
    @PostMapping("/users/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam Long userId, Model model) {
        try {
            // Удаляем пользователя через сервис
            userService.deleteUser(userId);
        } catch (IllegalArgumentException ex) {
            // Добавляем сообщение об ошибке и перенаправляем на страницу ошибок
            model.addAttribute("errorMessage", ex.getMessage());
            return "error/400";  // Путь к вашей странице 400
        } catch (DataIntegrityViolationException ex) {
            // Обработка ошибок нарушения целостности данных
            model.addAttribute("errorMessage", "Невозможно удалить пользователя из-за связанных данных.");
            return "error/500";  // Путь к вашей странице 500
        } catch (Exception ex) {
            // Обработка других исключений
            model.addAttribute("errorMessage", "Произошла ошибка: " + ex.getMessage());
            return "error/500";  // Путь к вашей странице 500
        }
        // Перенаправляем обратно на страницу управления пользователями
        return "redirect:/admin/users";
    }

    /**
     * Страница статистики.
     * Отображает статистику по пользователям и бронированиям.
     *
     * @return имя шаблона страницы статистики
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String statisticsPage() {
        return "admin/statistics";  // Название HTML шаблона для страницы статистики
    }
}
