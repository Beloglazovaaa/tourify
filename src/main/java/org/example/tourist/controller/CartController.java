package org.example.tourist.controller;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контроллер для управления действиями в корзине.
 * Позволяет добавлять, удалять и очищать туристические пакеты в корзине.
 */
@Controller
public class CartController {

    private final TourPackageService tourPackageService;
    private final Cart cart;

    @Autowired
    public CartController(TourPackageService tourPackageService, Cart cart) {
        this.tourPackageService = tourPackageService;
        this.cart = cart;
    }

    /**
     * Добавить туристический пакет в корзину.
     * Этот метод будет обрабатывать запросы на добавление пакета в корзину,
     * используя его ID.
     *
     * @param id ID туристического пакета, который нужно добавить
     * @return перенаправление на страницу с туристическими пакетами
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long id) {
        // Получаем туристический пакет по ID
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        // Добавляем пакет в корзину
        cart.addItem(tourPackage);
        // Перенаправляем на страницу с туристическими пакетами
        return "redirect:/tour-packages";
    }

    /**
     * Удалить туристический пакет из корзины.
     * Этот метод будет обрабатывать запросы на удаление пакета из корзины,
     * используя его ID.
     *
     * @param id ID туристического пакета, который нужно удалить
     * @return перенаправление на страницу с туристическими пакетами
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long id) {
        // Удаляем указанный пакет из корзины
        cart.removeItem(id);
        // Перенаправляем на страницу с туристическими пакетами
        return "redirect:/tour-packages";
    }

    /**
     * Очистить корзину.
     * Этот метод удаляет все элементы из корзины.
     *
     * @return перенаправление на страницу с туристическими пакетами
     */
    @PostMapping("/cart/clear")
    public String clearCart() {
        // Очищаем корзину
        cart.clearCart();
        // Перенаправляем на страницу с туристическими пакетами
        return "redirect:/tour-packages";
    }
}
