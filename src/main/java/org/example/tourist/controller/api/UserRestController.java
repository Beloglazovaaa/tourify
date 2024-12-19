package org.example.tourist.controller.api;

import org.example.tourist.models.User;
import org.example.tourist.services.UserService;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/admin/users")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить список всех пользователей.
     * Метод GET используется для извлечения всех пользователей системы.
     * Ответ кэшируется на 60 секунд для ускорения повторных запросов.
     *
     * @return список всех пользователей
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())  // Устанавливаем кэширование
                .body(userService.getAllUsers());  // Возвращаем список пользователей
    }

    /**
     * HEAD-запрос для получения метаданных о пользователях.
     * Этот метод используется для получения информации о ресурсе без извлечения его содержимого.
     *
     * @return метаданные о пользователях (в данном случае пустой ответ)
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headUsers() {
        return ResponseEntity.ok().build();  // Пустой ответ с кодом 200 OK
    }

    /**
     * OPTIONS-запрос для получения поддерживаемых методов для ресурса.
     * Этот метод возвращает заголовки, показывающие, какие HTTP-методы поддерживаются для ресурса.
     *
     * @return заголовки с поддерживаемыми методами
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD");  // Указываем поддерживаемые методы
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Обновить или установить роль пользователя.
     * Метод PUT используется для полного обновления ресурса, в данном случае — роли пользователя.
     * Если пользователя с таким ID нет, то будет создан новый.
     *
     * @param userId   ID пользователя для обновления
     * @param newRole  новая роль пользователя
     * @return статус 204 No Content (без содержимого)
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        userService.updateUserRole(userId, newRole);  // Обновляем роль пользователя
        return ResponseEntity.noContent().build();  // Возвращаем статус 204 No Content
    }

    /**
     * Частично обновить роль пользователя.
     * Метод PATCH используется для частичного обновления данных. Здесь это используется для обновления только роли.
     *
     * @param userId   ID пользователя
     * @param newRole  новая роль
     * @return статус 204 No Content
     */
    @PatchMapping("/{userId}/role")
    public ResponseEntity<Void> patchUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        userService.updateUserRole(userId, newRole);  // Частично обновляем роль пользователя
        return ResponseEntity.noContent().build();  // Возвращаем статус 204 No Content
    }

    /**
     * Удалить пользователя.
     * Метод DELETE используется для удаления пользователя по его ID.
     *
     * @param userId ID пользователя, которого необходимо удалить
     * @return статус 204 No Content (без содержимого)
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);  // Удаляем пользователя
        return ResponseEntity.noContent().build();  // Возвращаем статус 204 No Content
    }
}

