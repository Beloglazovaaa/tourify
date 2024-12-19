package org.example.tourist.controller;

import org.example.tourist.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для регистрации пользователей.
 * Обрабатывает запросы, связанные с регистрацией.
 */
@Controller
public class RegistrationController {

    private final UserService userService;

    // Внедрение сервиса пользователя для регистрации
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отображает страницу регистрации.
     *
     * @return имя шаблона страницы регистрации
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";  // Возвращает представление для страницы регистрации
    }

    /**
     * Регистрирует нового пользователя.
     * После регистрации пользователя перенаправляет на страницу логина.
     *
     * @param username имя пользователя
     * @param password пароль
     * @param role     роль пользователя (например, ROLE_USER)
     * @return перенаправление на страницу логина
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("role") String role) {
        userService.registerUser(username, password, role);
        return "redirect:/login";  // Перенаправляет на страницу логина после успешной регистрации
    }

}
