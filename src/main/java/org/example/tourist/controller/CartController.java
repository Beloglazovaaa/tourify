package org.example.tourist.controller;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
     *
     * @param id ID туристического пакета
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam Long id) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        cart.addItem(tourPackage);
        return "redirect:/tour-packages";
    }

    /**
     * Удалить туристический пакет из корзины.
     *
     * @param id ID туристического пакета
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam Long id) {
        cart.removeItem(id);
        return "redirect:/tour-packages";
    }

    /**
     * Очистить корзину.
     *
     * @return перенаправление на страницу туристических пакетов
     */
    @PostMapping("/cart/clear")
    public String clearCart() {
        cart.clearCart();
        return "redirect:/tour-packages";
    }
}
