package org.example.tourist.repositories;

import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTourPackage(TourPackage tourPackage);

}
