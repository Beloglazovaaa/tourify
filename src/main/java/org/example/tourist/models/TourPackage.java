package org.example.tourist.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий туристический пакет.
 * Содержит информацию о туре, такую как название, описание, цену, доступность, длительность,
 * а также связи с отзывами и бронированиями.
 */
@Entity
@Table(name = "tour_packages")
public class TourPackage {

    /** Уникальный идентификатор туристического пакета */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Название туристического пакета */
    private String name;

    /** Описание туристического пакета */
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    /** URL изображения для туристического пакета */
    private String imageUrl;

    /** Цена туристического пакета */
    private Integer price;

    /** Доступность туристического пакета (например, в наличии или нет) */
    private Boolean availability;

    /** Длительность туристического пакета в днях */
    private Integer duration;

    /** Список отзывов, связанных с этим туристическим пакетом */
    @OneToMany(mappedBy = "tourPackage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    /** Список бронирований, связанных с этим туристическим пакетом */
    @ManyToMany(mappedBy = "tourPackages")
    private List<Booking> bookings = new ArrayList<>();

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта туристического пакета.
     */
    public TourPackage() {}

    /**
     * Конструктор для инициализации туристического пакета с указанными параметрами.
     *
     * @param name название туристического пакета
     * @param description описание туристического пакета
     * @param imageUrl URL изображения туристического пакета
     * @param price цена туристического пакета
     * @param availability доступность туристического пакета
     * @param duration длительность туристического пакета
     */
    public TourPackage(String name, String description, String imageUrl, Integer price, Boolean availability, Integer duration) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.availability = availability;
        this.duration = duration;
    }

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Добавляет отзыв к туристическому пакету.
     *
     * @param review отзыв, который нужно добавить
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setTourPackage(this);
    }

    /**
     * Удаляет отзыв из туристического пакета.
     *
     * @param review отзыв, который нужно удалить
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setTourPackage(null);
    }

    /**
     * Добавляет бронирование к туристическому пакету.
     *
     * @param booking бронирование, которое нужно добавить
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.getTourPackages().add(this);
    }

    /**
     * Удаляет бронирование из туристического пакета.
     *
     * @param booking бронирование, которое нужно удалить
     */
    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.getTourPackages().remove(this);
    }
}
