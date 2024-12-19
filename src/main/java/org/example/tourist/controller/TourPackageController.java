package org.example.tourist.controller;

import org.example.tourist.models.User;
import org.example.tourist.models.Cart;
import org.example.tourist.models.Review;
import org.example.tourist.models.TourPackage;
import org.example.tourist.services.ReviewService;
import org.example.tourist.services.TourPackageService;
import org.example.tourist.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Контроллер для работы с туристическими пакетами.
 * Обрабатывает запросы для отображения, поиска, создания, редактирования, удаления и добавления отзывов к турпакетам.
 */
@Controller
@RequestMapping("/tour-packages")
public class TourPackageController {

    private final Cart cart;
    private final TourPackageService tourPackageService;
    private final UserService userService;
    private final ReviewService reviewService;

    public TourPackageController(Cart cart, TourPackageService tourPackageService, UserService userService, ReviewService reviewService) {
        this.cart = cart;
        this.tourPackageService = tourPackageService;
        this.userService = userService;
        this.reviewService = reviewService;
    }

    /**
     * Показать страницу с туристическими пакетами.
     * Доступно для пользователей, агентов и администраторов.
     *
     * @param model модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-packages"
     */
    @GetMapping
    public String tourPackagesPage(Model model, Principal principal) {
        List<TourPackage> tourPackages = tourPackageService.getAllTourPackages();
        Map<Long, Boolean> canDeleteMap = tourPackages.stream()
                .collect(Collectors.toMap(TourPackage::getId,
                        tourPackage -> tourPackageService.canDeleteTourPackage(tourPackage.getId())));

        // Добавляем данные корзины
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("tourPackages", tourPackages);
        model.addAttribute("canDeleteMap", canDeleteMap);
        return "tour-packages";
    }

    /**
     * Поиск туристических пакетов по названию.
     * Доступно для пользователей, агентов и администраторов.
     *
     * @param name название для поиска
     * @param model модель для представления
     * @param sort поле для сортировки
     * @param direction направление сортировки
     * @return название представления "tour-packages" с результатами поиска
     */
    @GetMapping("/search")
    public String searchTourPackages(
            @RequestParam(required = false) String name,
            @RequestParam(required = false, defaultValue = "name") String sort,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            Model model) {

        List<TourPackage> items = tourPackageService.searchTourPackages(name, sort, direction);

        // Создаем карту canDeleteMap для результатов поиска
        Map<Long, Boolean> canDeleteMap = items.stream()
                .collect(Collectors.toMap(TourPackage::getId,
                        tourPackage -> tourPackageService.canDeleteTourPackage(tourPackage.getId())));

        // Добавляем данные в модель
        model.addAttribute("tourPackages", items);
        model.addAttribute("canDeleteMap", canDeleteMap); // Добавляем canDeleteMap
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Поиск Туристических Пакетов | Tourify");
        return "tour-packages";
    }

    /**
     * Показать страницу создания нового туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param model модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT')")
    public String createTourPackagePage(Model model, Principal principal) {
        model.addAttribute("tourPackage", new TourPackage());
        return "tour-package-form";
    }

    /**
     * Создать новый туристический пакет.
     * Доступно только для администраторов и агентов.
     *
     * @param tourPackage объект туристического пакета из формы
     * @param principal текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT')")
    public String createTourPackage(@ModelAttribute TourPackage tourPackage, Principal principal) {
        tourPackageService.addTourPackage(tourPackage);
        return "redirect:/tour-packages";
    }

    /**
     * Удалить туристический пакет по ID.
     * Доступно только для администраторов.
     *
     * @param id ID туристического пакета
     * @param principal текущий аутентифицированный пользователь
     * @param model модель для представления
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteTourPackage(@PathVariable Long id, Principal principal, Model model) {
        boolean canDelete = tourPackageService.canDeleteTourPackage(id); // Проверка на активные бронирования
        if (canDelete) {
            tourPackageService.deleteTourPackage(id);
            return "redirect:/tour-packages"; // После удаления возвращаем на страницу пакетов
        } else {
            // Получаем обновленный список турпакетов
            List<TourPackage> tourPackages = tourPackageService.getAllTourPackages();
            Map<Long, Boolean> canDeleteMap = tourPackages.stream()
                    .collect(Collectors.toMap(TourPackage::getId,
                            tourPackage -> tourPackageService.canDeleteTourPackage(tourPackage.getId())));

            // Добавляем данные в модель для отображения ошибки
            model.addAttribute("tourPackages", tourPackages);
            model.addAttribute("canDeleteMap", canDeleteMap);
            model.addAttribute("cartItems", cart.getCartItems());
            model.addAttribute("totalPrice", cart.getTotalPrice());
            model.addAttribute("errorMessage", "Невозможно удалить турпакет, так как для него есть активные бронирования.");
            return "tour-packages"; // Возвращаем на страницу с ошибкой
        }
    }

    /**
     * Показать страницу редактирования туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id ID туристического пакета
     * @param model модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT')")
    public String editTourPackagePage(@PathVariable Long id, Model model, Principal principal) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("tourPackage", tourPackage);
        return "tour-package-form";
    }

    /**
     * Обновить данные туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id ID туристического пакета
     * @param tourPackage обновленный объект пакета из формы
     * @param availability флаг доступности пакета
     * @param principal текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_AGENT')")
    public String editTourPackage(@PathVariable Long id, @ModelAttribute TourPackage tourPackage,
                                  @RequestParam(required = false) Boolean availability, Principal principal) {
        tourPackage.setAvailability(availability != null && availability);
        tourPackageService.updateTourPackage(id, tourPackage);
        return "redirect:/tour-packages";
    }

    /**
     * Проверка, имеет ли пользователь определенную роль.
     *
     * @param principal текущий аутентифицированный пользователь
     * @param role роль для проверки
     * @return true, если пользователь имеет указанную роль, иначе false
     */
    private boolean hasRole(Principal principal, String role) {
        if (principal == null) {
            return false;
        }
        Authentication authentication = (Authentication) principal;
        Object principalObj = authentication.getPrincipal();
        if (!(principalObj instanceof UserDetails)) {
            return false;
        }
        UserDetails userDetails = (UserDetails) principalObj;
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals(role));
    }

    /**
     * Просмотр деталей туристического пакета.
     *
     * @param id ID туристического пакета
     * @param model модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-details" с деталями пакета и отзывами
     */
    @GetMapping("/{id}")
    public String viewTourPackage(@PathVariable Long id, Model model, Principal principal) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("tourPackage", tourPackage);

        // Получаем отзывы для данного турпакета
        List<Review> reviews = reviewService.getReviewsByTourPackage(tourPackage);
        model.addAttribute("reviews", reviews);

        return "tour-package-details"; // Шаблон страницы деталей тура, где есть секция с отзывами
    }

    /**
     * Обработчик для добавления отзыва к туристическому пакету.
     *
     * @param tourPackageId ID туристического пакета
     * @param rating оценка пакета
     * @param comment комментарий пользователя
     * @param principal текущий аутентифицированный пользователь
     * @return перенаправление на страницу с деталями пакета
     */
    @PostMapping("/addReview")
    @PreAuthorize("isAuthenticated()")
    public String addReview(@RequestParam Long tourPackageId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Principal principal) {

        // Находим соответствующий турпакет
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);
        if (tourPackage == null) {
            return "redirect:/error/404"; // Перенаправление на страницу ошибки
        }

        // Находим текущего авторизованного пользователя
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Создаем и сохраняем отзыв
        Review review = new Review(user, tourPackage, rating, comment);
        reviewService.saveReview(review);

        return "redirect:/tour-packages/" + tourPackageId;
    }

    /**
     * Получить детали туристического пакета по ID.
     *
     * @param id ID туристического пакета
     * @param model модель для представления
     * @return название представления "tour-package-details" с деталями пакета
     */
    @GetMapping("/details/{id}")
    public String getTourPackageDetails(@PathVariable("id") Long id, Model model) {
        // Получаем пакет по ID
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);

        // Если пакет не найден, возвращаем страницу ошибки
        if (tourPackage == null) {
            return "error/404";
        }

        model.addAttribute("tourPackage", tourPackage);
        List<Review> reviews = reviewService.getReviewsByTourPackage(tourPackage);
        model.addAttribute("reviews", reviews);

        return "tour-package-details"; // Имя вашего шаблона
    }
}
