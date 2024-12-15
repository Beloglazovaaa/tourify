// AdminController.java
package org.example.tourist.controller;

import org.example.tourist.models.User;
import org.example.tourist.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Отобразить страницу управления пользователями.
     */
    @GetMapping("/users")
    public String manageUsersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/manage-users";
    }


    /**
     * Обновить роль пользователя.
     */
    @PostMapping("/users/update-role")
    public String updateUserRole(@RequestParam Long userId, @RequestParam String newRole) {
        userService.updateUserRole(userId, newRole);
        return "redirect:/admin/users";
    }

    /**
     * Удалить пользователя.
     */
    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam Long userId) {
        userService.deleteUser(userId);
        return "redirect:/admin/users";
    }
}
