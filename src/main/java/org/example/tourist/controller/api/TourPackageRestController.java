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

    // Инжектируем сервисы для работы с турпакетами, отзывами и пользователями
    public TourPackageRestController(TourPackageService tourPackageService, UserService userService, ReviewService reviewService) {
        this.tourPackageService = tourPackageService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    /**
     * Получить список всех турпакетов.
     * Этот метод возвращает все доступные турпакеты.
     *
     * @return ResponseEntity с коллекцией турпакетов
     */
    @GetMapping
    public ResponseEntity<List<TourPackage>> getAllTourPackages() {
        List<TourPackage> packages = tourPackageService.getAllTourPackages();
        return ResponseEntity.ok(packages);
    }

    /**
     * Поиск турпакетов по имени.
     * Этот метод позволяет искать турпакеты по названию с возможностью сортировки.
     * По умолчанию сортировка по имени в порядке возрастания.
     *
     * @param name - имя турпакета для поиска
     * @param sort - поле для сортировки (по умолчанию "name")
     * @param direction - направление сортировки (по умолчанию "asc")
     * @return ResponseEntity с найденными турпакетами
     */
    @GetMapping("/search")
    public ResponseEntity<List<TourPackage>> searchTourPackages(
            @RequestParam String name,
            @RequestParam(defaultValue = "name") String sort,  // Значение по умолчанию "name"
            @RequestParam(defaultValue = "asc") String direction) {  // Значение по умолчанию "asc"
        return ResponseEntity.ok(tourPackageService.searchTourPackages(name, sort, direction));
    }

    /**
     * Создание нового турпакета.
     * Этот метод позволяет администратору или агенту создать новый турпакет.
     * В случае успешного создания возвращается статус 201 Created.
     *
     * @param tourPackage - объект нового турпакета
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с кодом 201 Created
     */
    @PostMapping
    public ResponseEntity<Void> createTourPackage(@RequestBody TourPackage tourPackage, Principal principal) {
        // Проверка ролей пользователя - только администратор или агент могут создавать турпакеты
        // Если роль не подходит, возвращаем ошибку 403 или 401
        tourPackageService.addTourPackage(tourPackage);
        return ResponseEntity.status(201).build();
    }

    /**
     * Обновление информации о турпакете.
     * Этот метод позволяет обновить информацию о турпакете, включая доступность.
     * Если параметр доступности не указан, используется текущее значение.
     *
     * @param id - идентификатор турпакета для обновления
     * @param tourPackage - обновленные данные турпакета
     * @param availability - новое состояние доступности (если задано)
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с кодом 204 No Content
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTourPackage(@PathVariable Long id, @RequestBody TourPackage tourPackage,
                                                  @RequestParam(required = false) Boolean availability, Principal principal) {
        // Проверка ролей пользователя - только администратор или агент могут обновлять турпакеты
        tourPackage.setAvailability(availability != null && availability);
        tourPackageService.updateTourPackage(id, tourPackage);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удаление турпакета.
     * Этот метод позволяет администратору удалить турпакет.
     * Если турпакет не найден, возвращается ошибка 404 Not Found.
     *
     * @param id - идентификатор турпакета для удаления
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с кодом 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTourPackage(@PathVariable Long id, Principal principal) {
        // Проверка роли админа - только администратор может удалять турпакеты
        tourPackageService.deleteTourPackage(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получить подробную информацию о турпакете.
     * Этот метод позволяет получить полную информацию о конкретном турпакете по его идентификатору.
     * Если турпакет не найден, возвращается ошибка 404 Not Found.
     *
     * @param id - идентификатор турпакета
     * @return ResponseEntity с детальной информацией о турпакете
     */
    @GetMapping("/{id}")
    public ResponseEntity<TourPackage> getTourPackageDetails(@PathVariable Long id) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tourPackage);
    }

    /**
     * Добавление отзыва для турпакета.
     * Этот метод позволяет пользователю добавить отзыв для конкретного турпакета.
     * В случае успешного добавления возвращается статус 201 Created.
     *
     * @param tourPackageId - идентификатор турпакета
     * @param rating - рейтинг отзыва
     * @param comment - текст отзыва
     * @param principal - текущий аутентифицированный пользователь
     * @return ResponseEntity с кодом 201 Created
     */
    @PostMapping("/{tourPackageId}/reviews")
    public ResponseEntity<Void> addReview(@PathVariable Long tourPackageId,
                                          @RequestParam int rating,
                                          @RequestParam String comment,
                                          Principal principal) {
        // Получаем турпакет по ID, если он не найден - возвращаем 404
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }

        // Получаем текущего пользователя, чтобы привязать отзыв к нему
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Создаем новый объект отзыва и сохраняем его через сервис
        Review review = new Review(user, tourPackage, rating, comment);
        reviewService.saveReview(review);
        return ResponseEntity.status(201).build();
    }

    /**
     * Получить список отзывов для турпакета.
     * Этот метод возвращает все отзывы для конкретного турпакета по его ID.
     * Если турпакет не найден, возвращается ошибка 404.
     *
     * @param tourPackageId - идентификатор турпакета
     * @return ResponseEntity с коллекцией отзывов для турпакета
     */
    @GetMapping("/{tourPackageId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long tourPackageId) {
        // Получаем турпакет по ID, если он не найден - возвращаем 404
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);
        if (tourPackage == null) {
            return ResponseEntity.notFound().build();
        }
        // Возвращаем список всех отзывов для турпакета
        return ResponseEntity.ok(reviewService.getReviewsByTourPackage(tourPackage));
    }

    /**
     * OPTIONS-запрос для турпакетов.
     * Этот метод возвращает список всех доступных HTTP-методов для ресурса "/api/tour-packages".
     *
     * @return ResponseEntity с заголовком Allow, указывающим доступные методы
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsTourPackages() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * HEAD-запрос для турпакетов.
     * Этот метод возвращает только заголовки ответа без тела, полезен для получения метаданных.
     *
     * @return ResponseEntity без тела, только с HTTP-заголовками
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headTourPackages() {
        return ResponseEntity.ok().build();
    }
}

