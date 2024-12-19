package org.example.tourist.models;

import jakarta.persistence.*;
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
    private List<Review> reviews;

    /** Список бронирований, связанных с этим туристическим пакетом */
    @OneToMany(mappedBy = "tourPackages", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

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

    /**
     * Получает уникальный идентификатор туристического пакета.
     *
     * @return уникальный идентификатор туристического пакета
     */
    public Long getId() {
        return id;
    }

    /**
     * Устанавливает уникальный идентификатор туристического пакета.
     *
     * @param id уникальный идентификатор туристического пакета
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Получает название туристического пакета.
     *
     * @return название туристического пакета
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает название туристического пакета.
     *
     * @param name название туристического пакета
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Получает описание туристического пакета.
     *
     * @return описание туристического пакета
     */
    public String getDescription() {
        return description;
    }

    /**
     * Устанавливает описание туристического пакета.
     *
     * @param description описание туристического пакета
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Получает URL изображения туристического пакета.
     *
     * @return URL изображения туристического пакета
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Устанавливает URL изображения туристического пакета.
     *
     * @param imageUrl URL изображения туристического пакета
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Получает цену туристического пакета.
     *
     * @return цена туристического пакета
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Устанавливает цену туристического пакета.
     *
     * @param price цена туристического пакета
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * Получает доступность туристического пакета.
     *
     * @return доступность туристического пакета (true, если доступен, иначе false)
     */
    public Boolean getAvailability() {
        return availability;
    }

    /**
     * Устанавливает доступность туристического пакета.
     *
     * @param availability доступность туристического пакета
     */
    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    /**
     * Получает длительность туристического пакета.
     *
     * @return длительность туристического пакета в днях
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Устанавливает длительность туристического пакета.
     *
     * @param duration длительность туристического пакета в днях
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Получает список бронирований, связанных с этим туристическим пакетом.
     *
     * @return список бронирований
     */
    public List<Booking> getBookings() {
        return bookings;
    }

    /**
     * Устанавливает список бронирований, связанных с этим туристическим пакетом.
     *
     * @param bookings список бронирований
     */
    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    /**
     * Получает список отзывов, связанных с этим туристическим пакетом.
     *
     * @return список отзывов
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * Устанавливает список отзывов, связанных с этим туристическим пакетом.
     *
     * @param reviews список отзывов
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}

