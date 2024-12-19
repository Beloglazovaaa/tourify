package org.example.tourist.controller.api;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")  // Все запросы будут начинаться с /api/cart
public class CartRestController {

    private final TourPackageService tourPackageService;
    private final Cart cart;

    // Конструктор для внедрения зависимостей
    public CartRestController(TourPackageService tourPackageService, Cart cart) {
        this.tourPackageService = tourPackageService;
        this.cart = cart;
    }

    /**
     * Получить все элементы из корзины.
     * Ответ включает текущие элементы корзины.
     *
     * @return Ответ с корзиной
     */
    @GetMapping("/items")
    public ResponseEntity<Cart> getCartItems() {
        return ResponseEntity.ok(cart);  // Возвращаем текущие элементы корзины
    }

    /**
     * Добавить турпакет в корзину.
     * Принимает ID турпакета и добавляет его в корзину.
     *
     * @param id ID турпакета для добавления в корзину
     * @return Ответ с кодом 201 (создано)
     */
    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(@RequestParam Long id) {
        // Получаем турпакет по ID
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        // Добавляем турпакет в корзину
        cart.addItem(tourPackage);
        // Возвращаем ответ с кодом 201 (ресурс создан)
        return ResponseEntity.status(201).build();
    }

    /**
     * Удалить турпакет из корзины.
     * Принимает ID турпакета и удаляет его из корзины.
     *
     * @param id ID турпакета для удаления из корзины
     * @return Ответ с кодом 204 (без содержимого)
     */
    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        // Удаляем турпакет из корзины по ID
        cart.removeItem(id);
        // Возвращаем ответ с кодом 204 (удалено, без содержимого)
        return ResponseEntity.noContent().build();
    }

    /**
     * Очистить корзину.
     * Удаляет все элементы из корзины.
     *
     * @return Ответ с кодом 204 (без содержимого)
     */
    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        // Очищаем корзину
        cart.clearCart();
        // Возвращаем ответ с кодом 204 (очищено, без содержимого)
        return ResponseEntity.noContent().build();
    }

    /**
     * Обрабатывает OPTIONS-запросы на корзину.
     * Возвращает информацию о поддерживаемых методах для корзины.
     *
     * @return Ответ с заголовками, указывающими разрешенные методы
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsCart() {
        // Создаем заголовки с информацией о разрешенных методах
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,DELETE,OPTIONS,HEAD");  // Разрешенные методы
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Обрабатывает HEAD-запросы на корзину.
     * Возвращает только метаданные без тела ответа.
     *
     * @return Пустой ответ с кодом 200 OK
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headCart() {
        return ResponseEntity.ok().build();  // Пустой ответ с кодом 200 OK
    }
}

