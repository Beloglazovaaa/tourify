package org.example.tourist.models;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий пользователя в системе.
 * Каждый пользователь может иметь несколько ролей, отзывов и бронирований.
 */
@Entity
@Table(name = "users")
public class User {

    /** Уникальный идентификатор пользователя */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Имя пользователя */
    @Column(nullable = false, unique = true)
    private String username;

    /** Пароль пользователя */
    @Column(nullable = false)
    private String password;

    /** Множество ролей, назначенных пользователю */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",  // Таблица связи между пользователями и ролями
            joinColumns = @JoinColumn(name = "user_id"),  // Связь с пользователем
            inverseJoinColumns = @JoinColumn(name = "role_id")  // Связь с ролью
    )
    private Set<Role> roles = new HashSet<>();

    /** Множество отзывов, оставленных пользователем */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    /** Множество бронирований, сделанных пользователем */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта пользователя.
     */
    public User() {}

    /**
     * Конструктор для инициализации пользователя с заданным именем пользователя и паролем.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Устанавливает пароль пользователя.
     * Пароль должен быть закодирован перед установкой.
     *
     * @param password закодированный пароль пользователя
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Устанавливает множество ролей для пользователя.
     *
     * @param roles множество ролей, назначенных пользователю
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Добавляет отзыв к пользователю.
     *
     * @param review отзыв, который нужно добавить
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }

    /**
     * Удаляет отзыв из пользователя.
     *
     * @param review отзыв, который нужно удалить
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setUser(null);
    }

    /**
     * Добавляет бронирование к пользователю.
     *
     * @param booking бронирование, которое нужно добавить
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.setUser(this);
    }

    /**
     * Удаляет бронирование из пользователя.
     *
     * @param booking бронирование, которое нужно удалить
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.setUser(null);
    }
}
