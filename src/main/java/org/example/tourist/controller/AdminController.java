package org.example.tourist.controller;

import org.example.tourist.models.User;
import org.example.tourist.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String updateUserRole(@RequestParam Long userId, @RequestParam String newRole) {
        // Обновляем роль пользователя через сервис
        userService.updateUserRole(userId, newRole);
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
    public String deleteUser(@RequestParam Long userId) {
        // Удаляем пользователя через сервис
        userService.deleteUser(userId);
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
    public String statisticsPage() {
        return "admin/statistics";  // Название HTML шаблона для страницы статистики
    }
}