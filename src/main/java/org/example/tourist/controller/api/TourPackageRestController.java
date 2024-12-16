package org.example.tourist.controller.api;

import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.example.tourist.models.User;
import org.example.tourist.services.ReviewService;
import org.example.tourist.services.TourPackageService;
import org.example.tourist.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tour-packages")
public class TourPackageRestController {

    private final TourPackageService tourPackageService;
    private final UserService userService;
    private final ReviewService reviewService;

    public TourPackageRestController(TourPackageService tourPackageService, UserService userService, ReviewService reviewService) {
        this.tourPackageService = tourPackageService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<TourPackage>> getAllTourPackages() {
        List<TourPackage> packages = tourPackageService.getAllTourPackages();
        return ResponseEntity.ok(packages);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> searchTourPackages(@RequestParam String name) {
        return ResponseEntity.ok(tourPackageService.searchTourPackages(name));
    }

    @PostMapping
    public ResponseEntity<Void> createTourPackage(@RequestBody TourPackage tourPackage, Principal principal) {
        // Проверка ролей - логика авторизации (admin/agent)
        // Если не хватает данных - возвращаем 403 или 401
        tourPackageService.addTourPackage(tourPackage);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTourPackage(@PathVariable Long id, @RequestBody TourPackage tourPackage,
                                                  @RequestParam(required = false) Boolean availability, Principal principal) {
        // Проверка ролей
        tourPackage.setAvailability(availability != null && availability);
        tourPackageService.updateTourPackage(id, tourPackage);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPackage(@PathVariable Long id, Principal principal) {
        // Проверка роли админа
        tourPackageService.deleteTourPackage(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getTourPackageDetails(@PathVariable Long id) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tourPackage);
    }

    /**
     * Добавление отзыва.
     */
    @PostMapping("/{tourPackageId}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable Long tourPackageId,
                                          @RequestParam int rating,
                                          @RequestParam String comment,
                                          Principal principal) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }

        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Review review = new Review(user, tourPackage, rating, comment);
        reviewService.saveReview(review);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/{tourPackageId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long tourPackageId) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reviewService.getReviewsByTourPackage(tourPackage));
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsTourPackages() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
        return ResponseEntity.ok().headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headTourPackages() {
        return ResponseEntity.ok().build();
    }
}
