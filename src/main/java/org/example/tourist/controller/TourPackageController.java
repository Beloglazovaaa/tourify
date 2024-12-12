package org.example.tourist.controller;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
     *
     * @param model модель для представления
     * @return название представления "tour-packages"
     */
    @GetMapping
    public String tourPackagesPage(Model model) {
        model.addAttribute("tourPackages", tourPackageService.getAllTourPackages());
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Туристические Пакеты | YourAgencyName");
        return "tour-packages";
    }

    /**
     * Поиск туристических пакетов по названию.
     *
     * @param name  название для поиска
     * @param model модель для представления
     * @return название представления "tour-packages" с результатами поиска
     */
    @GetMapping("/search")
    public String searchTourPackages(@RequestParam String name, Model model) {
        List<TourPackage> items = tourPackageService.searchTourPackages(name);
        model.addAttribute("tourPackages", items);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Поиск Туристических Пакетов | YourAgencyName");
        return "tour-packages";
    }

    /**
     * Показать страницу создания нового туристического пакета.
     *
     * @param model модель для представления
     * @return название представления "tour-package-form"
     */
    @GetMapping("/create")
    public String createTourPackagePage(Model model) {
        model.addAttribute("tourPackage", new TourPackage());
        return "tour-package-form";
    }

    /**
     * Создать новый туристический пакет.
     *
     * @param tourPackage объект туристического пакета из формы
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/create")
    public String createTourPackage(@ModelAttribute TourPackage tourPackage) {
        tourPackageService.addTourPackage(tourPackage);
        return "redirect:/tour-packages";
    }

    /**
     * Удалить туристический пакет по ID.
     *
     * @param id ID туристического пакета
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/delete/{id}")
    public String deleteTourPackage(@PathVariable Long id) {
        tourPackageService.deleteTourPackage(id);
        return "redirect:/tour-packages";
    }

    /**
     * Показать страницу редактирования туристического пакета.
     *
     * @param id    ID туристического пакета
     * @param model модель для представления
     * @return название представления "tour-package-form"
     */
    @GetMapping("/edit/{id}")
    public String editTourPackagePage(@PathVariable Long id, Model model) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("tourPackage", tourPackage);
        return "tour-package-form";
    }

    /**
     * Обновить данные туристического пакета.
     *
     * @param id             ID туристического пакета
     * @param tourPackage    обновленный объект пакета из формы
     * @param availability   флаг доступности пакета
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/edit/{id}")
    public String editTourPackage(@PathVariable Long id, @ModelAttribute TourPackage tourPackage, @RequestParam(required = false) Boolean availability) {
        tourPackage.setAvailability(availability != null && availability);
        tourPackageService.updateTourPackage(id, tourPackage);
        return "redirect:/tour-packages";
    }
}
