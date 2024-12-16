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
     * @param model     модель для представления
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
     * @param name    название для поиска
     * @param model   модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-packages" с результатами поиска
     */
    @GetMapping("/search")
    public String searchTourPackages(@RequestParam String name, Model model, Principal principal) {
        List<TourPackage> items = tourPackageService.searchTourPackages(name);
        model.addAttribute("tourPackages", items);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("totalPrice", cart.getTotalPrice());
        model.addAttribute("pageTitle", "Поиск Туристических Пакетов | Tourify");
        return "tour-packages";
    }

    /**
     * Показать страницу создания нового туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param model     модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/create")
    public String createTourPackagePage(Model model, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            model.addAttribute("tourPackage", new TourPackage());
            return "tour-package-form";
        }
        return "redirect:/access-denied";
    }

    /**
     * Создать новый туристический пакет.
     * Доступно только для администраторов и агентов.
     *
     * @param tourPackage объект туристического пакета из формы
     * @param principal   текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/create")
    public String createTourPackage(@ModelAttribute TourPackage tourPackage, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            tourPackageService.addTourPackage(tourPackage);
            return "redirect:/tour-packages";
        }
        return "redirect:/access-denied";
    }

    /**
     * Удалить туристический пакет по ID.
     * Доступно только для администраторов.
     *
     * @param id        ID туристического пакета
     * @param principal текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/delete/{id}")
    public String deleteTourPackage(@PathVariable Long id, Principal principal, Model model) {
        if (!hasRole(principal, "ROLE_ADMIN")) {  // Проверка роли
            return "redirect:/access-denied"; // Если нет нужной роли
        }

        boolean canDelete = tourPackageService.canDeleteTourPackage(id); // Проверка на активные бронирования
        if (canDelete) {
            tourPackageService.deleteTourPackage(id);
            return "redirect:/tour-packages"; // После удаления возвращаем на страницу пакетов
        } else {
            model.addAttribute("errorMessage", "Невозможно удалить турпакет, так как для него есть активные бронирования.");
            return "tour-packages"; // Если нельзя удалить, возвращаем на страницу с ошибкой
        }
    }




    /**
     * Показать страницу редактирования туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id        ID туристического пакета
     * @param model     модель для представления
     * @param principal текущий аутентифицированный пользователь
     * @return название представления "tour-package-form" или перенаправление при отсутствии прав
     */
    @GetMapping("/edit/{id}")
    public String editTourPackagePage(@PathVariable Long id, Model model, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            TourPackage tourPackage = tourPackageService.getTourPackageById(id);
            model.addAttribute("tourPackage", tourPackage);
            return "tour-package-form";
        }
        return "redirect:/access-denied";
    }

    /**
     * Обновить данные туристического пакета.
     * Доступно только для администраторов и агентов.
     *
     * @param id           ID туристического пакета
     * @param tourPackage  обновленный объект пакета из формы
     * @param availability флаг доступности пакета
     * @param principal    текущий аутентифицированный пользователь
     * @return перенаправление на страницу туристических пакетов или на страницу доступа запрещен
     */
    @PostMapping("/edit/{id}")
    public String editTourPackage(@PathVariable Long id, @ModelAttribute TourPackage tourPackage,
                                  @RequestParam(required = false) Boolean availability, Principal principal) {
        if (hasRole(principal, "ROLE_ADMIN") || hasRole(principal, "ROLE_AGENT")) {
            tourPackage.setAvailability(availability != null && availability);
            tourPackageService.updateTourPackage(id, tourPackage);
            return "redirect:/tour-packages";
        }
        return "redirect:/access-denied";
    }

    /**
     * Проверка, имеет ли пользователь определенную роль.
     *
     * @param principal текущий аутентифицированный пользователь
     * @param role      роль для проверки
     * @return true, если пользователь имеет указанную роль, иначе false
     */
    private boolean hasRole(Principal principal, String role) {
        if (principal == null) {
            return false;
        }
        Authentication authentication = (Authentication) principal;
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(r -> r.equals(role));
    }


    @GetMapping("/{id}")
    public String viewTourPackage(@PathVariable Long id, Model model, Principal principal) {
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);
        model.addAttribute("tourPackage", tourPackage);

        // Получаем отзывы для данного турпакета
        List<Review> reviews = reviewService.getReviewsByTourPackage(tourPackage);
        model.addAttribute("reviews", reviews);

        // Если нужно, передаем информацию о залогиненном пользователе
        // model.addAttribute("loggedInUser", currentUser);

        return "tour-package-details"; // Шаблон страницы деталей тура, где есть секция с отзывами
    }

    // Обработчик для добавления отзыва
    @PostMapping("/addReview")
    @PreAuthorize("isAuthenticated()")
    public String addReview(@RequestParam Long tourPackageId,
                            @RequestParam int rating,
                            @RequestParam String comment,
                            Principal principal) {

        // Находим соответствующий турпакет
        TourPackage tourPackage = tourPackageService.getTourPackageById(tourPackageId);

        // Находим текущего авторизованного пользователя (реализуйте логику получения User по principal)
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        // Создаем и сохраняем отзыв
        Review review = new Review(user, tourPackage, rating, comment);
        reviewService.saveReview(review);

        return "redirect:/tour-packages/" + tourPackageId;
    }

    @GetMapping("/details/{id}")
    public String getTourPackageDetails(@PathVariable("id") Long id, Model model) {
        // Получаем пакет по ID
        TourPackage tourPackage = tourPackageService.getTourPackageById(id);

        // Если пакет не найден, возвращаем страницу ошибки
        if (tourPackage == null) {
            return "error/404";
        }

        model.addAttribute("tourPackage", tourPackage);
        // Если нужно, можете добавить логику получения отзывов и других данных
        List<Review> reviews = reviewService.getReviewsByTourPackage(tourPackage);
        model.addAttribute("reviews", reviews);

        return "tour-package-details"; // Имя вашего шаблона
    }


}

