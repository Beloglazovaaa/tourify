package org.example.tourist.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Cart {

    private List<TourPackage> cartItems = new ArrayList<>();
    private Integer totalPrice = 0;

    // Конструктор
    public Cart() {}

    // Геттеры и Сеттеры
    public List<TourPackage> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<TourPackage> cartItems) {
        this.cartItems = cartItems;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    // Методы добавления и удаления тура
    public void addItem(TourPackage item) {
        cartItems.add(item);
        totalPrice += item.getPrice();
    }

    public void removeItem(Long id) {
        TourPackage item = cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (item != null) {
            cartItems.remove(item);
            totalPrice -= item.getPrice();
        }
    }

    public void clearCart() {
        cartItems.clear();
        totalPrice = 0;
    }
}
