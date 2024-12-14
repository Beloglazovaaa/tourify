package org.example.tourist.services;

import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.example.tourist.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * Сохранить новый отзыв.
     *
     * @param review отзыв для сохранения
     */
    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    /**
     * Получить список отзывов для конкретного туристического пакета.
     *
     * @param tourPackage туристический пакет
     * @return список отзывов
     */
    public List<Review> getReviewsByTourPackage(TourPackage tourPackage) {
        return reviewRepository.findByTourPackage(tourPackage);
    }
}
