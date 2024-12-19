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

    // Геттеры и сеттеры

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TourPackage getTourPackage() {
        return tourPackage;
    }

    public void setTourPackage(TourPackage tourPackage) {
        this.tourPackage = tourPackage;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }
}
