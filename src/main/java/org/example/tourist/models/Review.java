package org.example.tourist.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс, представляющий отзыв о туристическом пакете.
 * Содержит информацию о пользователе, который оставил отзыв, связанном с ним турпакете,
 * рейтинге, комментарии и дате отзыва.
 */
@Entity
@Table(name = "reviews")
public class Review {

    /** Уникальный идентификатор отзыва */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Связь с пользователем, оставившим отзыв */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Связь с туристическим пакетом, на который был оставлен отзыв */
    @ManyToOne
    @JoinColumn(name = "tour_package_id", nullable = false)
    private TourPackage tourPackage;

    /** Рейтинг от 1 до 5, поставленный пользователем */
    @Column(name = "rating", nullable = false)
    private int rating;

    /** Текстовый комментарий к отзыву (не более 2000 символов) */
    @Column(name = "comment", length = 2000)
    private String comment;

    /** Дата и время создания отзыва */
    @Column(name = "review_date", nullable = false)
    private LocalDateTime reviewDate;

    /**
     * Конструктор по умолчанию.
     * Используется для создания пустого объекта отзыва.
     */
    public Review() {}

    /**
     * Конструктор для инициализации отзыва с указанными параметрами.
     *
     * @param user пользователь, оставивший отзыв
     * @param tourPackage туристический пакет, на который оставлен отзыв
     * @param rating рейтинг от 1 до 5
     * @param comment текстовый комментарий
     */
    public Review(User user, TourPackage tourPackage, int rating, String comment) {
        this.user = user;
        this.tourPackage = tourPackage;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDateTime.now();  // Устанавливаем текущую дату и время отзыва
    }

    /**
     * Получает уникальный идентификатор отзыва.
     *
     * @return уникальный идентификатор отзыва
     */
    public Long getId() {
        return id;
    }

    /**
     * Получает пользователя, оставившего отзыв.
     *
     * @return объект пользователя
     */
    public User getUser() {
        return user;
    }

    /**
     * Получает туристический пакет, на который оставлен отзыв.
     *
     * @return объект туристического пакета
     */
    public TourPackage getTourPackage() {
        return tourPackage;
    }

    /**
     * Получает рейтинг отзыва.
     *
     * @return рейтинг от 1 до 5
     */
    public int getRating() {
        return rating;
    }

    /**
     * Получает текстовый комментарий отзыва.
     *
     * @return текстовый комментарий
     */
    public String getComment() {
        return comment;
    }

    /**
     * Получает дату и время создания отзыва.
     *
     * @return дата и время создания отзыва
     */
    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    /**
     * Устанавливает уникальный идентификатор отзыва.
     *
     * @param id уникальный идентификатор отзыва
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Устанавливает пользователя, оставившего отзыв.
     *
     * @param user объект пользователя
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Устанавливает туристический пакет, на который оставлен отзыв.
     *
     * @param tourPackage объект туристического пакета
     */
    public void setTourPackage(TourPackage tourPackage) {
        this.tourPackage = tourPackage;
    }

    /**
     * Устанавливает рейтинг отзыва.
     *
     * @param rating рейтинг от 1 до 5
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * Устанавливает текстовый комментарий отзыва.
     *
     * @param comment текстовый комментарий
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Устанавливает дату и время создания отзыва.
     *
     * @param reviewDate дата и время создания отзыва
     */
    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
