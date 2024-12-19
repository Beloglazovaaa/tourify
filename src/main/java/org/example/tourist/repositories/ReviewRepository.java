package org.example.tourist.repositories;

import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Репозиторий для работы с сущностью {@link Review}.
 * Содержит методы для выполнения операций с отзывами, связанных с туристическими пакетами.
 */
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Находит все отзывы, связанные с указанным туристическим пакетом.
     *
     * @param tourPackage туристический пакет, для которого необходимо найти отзывы
     * @return список отзывов для указанного туристического пакета
     */
    List<Review> findByTourPackage(TourPackage tourPackage);

}
