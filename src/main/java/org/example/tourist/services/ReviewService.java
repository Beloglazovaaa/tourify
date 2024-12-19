package org.example.tourist.services;

import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.example.tourist.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с отзывами.
 * Включает методы для сохранения отзыва и получения отзывов по туристическому пакету.
 */
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * Конструктор для инициализации {@link ReviewRepository}.
     *
     * @param reviewRepository репозиторий для работы с отзывами
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Сохраняет новый отзыв в базе данных.
     *
     * @param review отзыв, который нужно сохранить
     */
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Получает список отзывов для конкретного туристического пакета.
     *
     * @param tourPackage туристический пакет, для которого необходимо получить отзывы
     * @return список отзывов для указанного туристического пакета
     */
    public List<Review> getReviewsByTourPackage(TourPackage tourPackage) {
        return reviewRepository.findByTourPackage(tourPackage);
    }
}
