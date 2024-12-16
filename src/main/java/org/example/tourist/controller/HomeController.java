package org.example.tourist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    /**
     * Отображает страницу "Об авторе".
     * Страница доступна для всех пользователей.
     *
     * @param model модель для представления
     * @return имя шаблона страницы "about"
     */
    @GetMapping("/about")
    public String aboutPage(Model model) {
        // Добавляем данные в модель, если нужно
        model.addAttribute("pageTitle", "Об авторе | Tourify");
        return "about";
    }
}

