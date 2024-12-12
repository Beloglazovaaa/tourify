package org.example.tourist.repositories;


import org.example.tourist.models.TourPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
    List<TourPackage> findByAvailability(boolean availability);
    List<TourPackage> findByNameContaining(String name);
}
