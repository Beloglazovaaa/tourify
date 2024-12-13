package org.example.tourist.controller;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/tour-packages")
public class TourPackageController {
    private final Cart cart;
    private final TourPackageService tourPackageService;

    public TourPackageController(Cart cart, TourPackageService tourPackageService) {
        this.cart = cart;
        this.tourPackageService = tourPackageService;
    }

    /**
     * Показать страницу с туристическими пакетами.
     * Доступно для пользователей, агентов и администраторов.
     *
     * @param model     модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-packages"
     */
    @GetMapping
    public String tourPackagesPage(Model model, Principal principal) {
        // Проверка прав доступа при необходимости (опционально)
        model.addAttribute("tourPackages", tourPackageService.getAllTourPackages());
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Туристические Пакеты | YourAgencyName");
        return "tour-packages";
    }

    /**
     * Поиск туристических пакетов по названию.
     * Доступно для пользователей, агентов и администраторов.
     *
     * @param name    название для поиска
     * @param model   модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-packages" с результатами поиска
     */
    @GetMapping("/search")
    public String searchTourPackages(@RequestParam String name, Model model, Principal principal) {
        List<TourPackage> items = tourPackageService.searchTourPackages(name);
        model.addAttribute("tourPackages", items);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Поиск Туристических Пакетов | YourAgencyName");
        return "tour-packages";
    }

    /**
     * Показать страницу создания нового туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param model     модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/create")
    public String createTourPackagePage(Model model, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            model.addAttribute("tourPackage", new TourPackage());
            return "tour-package-form";
        }
        return "redirect:/access-denied";
    }

    /**
     * Создать новый туристический пакет.
     * Доступно только для администраторов и агентов.
     *
     * @param tourPackage объект туристического пакета из формы
     * @param principal   текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/create")
    public String createTourPackage(@ModelAttribute TourPackage tourPackage, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            tourPackageService.addTourPackage(tourPackage);
            return "redirect:/tour-packages";
        }
        return "redirect:/access-denied";
    }

    /**
     * Удалить туристический пакет по ID.
     * Доступно только для администраторов.
     *
     * @param id        ID туристического пакета
     * @param principal текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/delete/{id}")
    public String deleteTourPackage(@PathVariable Long id, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN")) {
            tourPackageService.deleteTourPackage(id);
            return "redirect:/tour-packages";
        }
        return "redirect:/access-denied";
    }

    /**
     * Показать страницу редактирования туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id        ID туристического пакета
     * @param model     модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/edit/{id}")
    public String editTourPackagePage(@PathVariable Long id, Model model, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            TourPackage tourPackage = tourPackageService.getTourPackageById(id);
            model.addAttribute("tourPackage", tourPackage);
            return "tour-package-form";
        }
        return "redirect:/access-denied";
    }

    /**
     * Обновить данные туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id           ID туристического пакета
     * @param tourPackage  обновленный объект пакета из формы
     * @param availability флаг доступности пакета
     * @param principal    текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/edit/{id}")
    public String editTourPackage(@PathVariable Long id, @ModelAttribute TourPackage tourPackage,
                                  @RequestParam(required = false) Boolean availability, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            tourPackage.setAvailability(availability != null && availability);
            tourPackageService.updateTourPackage(id, tourPackage);
            return "redirect:/tour-packages";
        }
        return "redirect:/access-denied";
    }

    /**
     * Проверка, имеет ли пользователь определенную роль.
     *
     * @param principal текущий аутентифицированный пользователь
     * @param role      роль для проверки
     * @return true, если пользователь имеет указанную роль, иначе false
     */
    private boolean hasRole(Principal principal, String role) {
        if (principal == null) {
            return false;
        }
        Authentication authentication = (Authentication) principal;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals(role));
    }
}

