package org.example.tourist.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий корзину для бронирования туров.
 * Содержит список туров, добавленных в корзину, и общую сумму заказа.
 */
@Component
public class Cart {

    /** Список туров, добавленных в корзину */
    private List<TourPackage> cartItems = new ArrayList<>();

    /** Общая сумма туров в корзине */
    private Integer totalPrice = 0;

    /**
     * Конструктор по умолчанию.
     * Инициализирует пустую корзину.
     */
    public Cart() {}

    /**
     * Получает список туров, добавленных в корзину.
     *
     * @return список туров в корзине
     */
    public List<TourPackage> getCartItems() {
        return cartItems;
    }

    /**
     * Устанавливает список туров в корзине.
     *
     * @param cartItems список туров, которые нужно добавить в корзину
     */
    public void setCartItems(List<TourPackage> cartItems) {
        this.cartItems = cartItems;
    }

    /**
     * Получает общую сумму туров в корзине.
     *
     * @return общая сумма
     */
    public Integer getTotalPrice() {
        return totalPrice;
    }

    /**
     * Устанавливает общую сумму туров в корзине.
     *
     * @param totalPrice общая сумма, которая должна быть установлена
     */
    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * Добавляет тур в корзину и обновляет общую сумму.
     *
     * @param item тур, который добавляется в корзину
     */
    public void addItem(TourPackage item) {
        cartItems.add(item);
        totalPrice += item.getPrice();  // Обновляем общую сумму
    }

    /**
     * Удаляет тур из корзины по его уникальному идентификатору и обновляет общую сумму.
     *
     * @param id идентификатор тура, который нужно удалить
     */
    public void removeItem(Long id) {
        // Находим тур по ID
        TourPackage item = cartItems.stream()
                .filter(cartItem -> cartItem.getId().equals(id))
                .findFirst()
                .orElse(null);  // Если тур не найден, возвращаем null

        // Если тур найден, удаляем его и уменьшаем общую сумму
        if (item != null) {
            cartItems.remove(item);
            totalPrice -= item.getPrice();  // Обновляем общую сумму
        }
    }

    /**
     * Очищает корзину, удаляя все туры и сбрасывая общую сумму.
     */
    public void clearCart() {
        cartItems.clear();
        totalPrice = 0;  // Сбрасываем общую сумму
    }
}
