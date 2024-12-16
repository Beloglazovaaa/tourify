package org.example.tourist.controller.api;

import org.example.tourist.models.Cart;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.TourPackageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartRestController {

    private final TourPackageService tourPackageService;
    private final Cart cart;

    public CartRestController(TourPackageService tourPackageService, Cart cart) {
        this.tourPackageService = tourPackageService;
        this.cart = cart;
    }

    @GetMapping("/items")
    public ResponseEntity<Cart> getCartItems() {
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(@RequestParam Long id) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        cart.addItem(tourPackage);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {
        cart.removeItem(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        cart.clearCart();
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsCart() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,DELETE,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headCart() {
        return ResponseEntity.ok().build();
    }
}
