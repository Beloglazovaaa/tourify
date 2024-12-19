package org.example.tourist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для страницы аутентификации.
 * В данный момент поддерживает только страницу входа.
 */
@Controller
public class AuthController {

    /**
     * Отображает страницу входа.
     *
     * @return имя шаблона страницы входа
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";  // Название HTML шаблона для страницы логина
    }
}

