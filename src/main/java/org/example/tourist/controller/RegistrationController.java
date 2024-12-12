package org.example.tourist.controller;

import org.example.tourist.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Показать форму регистрации пользователя.
     *
     * @return название представления "register"
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    /**
     * Зарегистрировать нового пользователя.
     *
     * @param username имя пользователя
     * @param password пароль
     * @param role     роль пользователя
     * @return перенаправление на страницу логина
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               @RequestParam("role") String role) {
        userService.registerUser(username, password, role);
        return "redirect:/login";
    }

}

