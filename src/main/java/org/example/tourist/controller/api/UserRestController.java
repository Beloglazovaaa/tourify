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
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePublic())
                .body(userService.getAllUsers());
    }

    /**
     * HEAD-запрос для получения метаданных о пользователях.
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headUsers() {
        return ResponseEntity.ok().build();
    }

    /**
     * OPTIONS-запрос для получения поддерживаемых методов.
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,POST,PUT,PATCH,DELETE,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * Обновить или установить роль пользователя (PUT для полного обновления или создания, если нужно).
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<Void> updateUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        userService.updateUserRole(userId, newRole);
        return ResponseEntity.noContent().build();
    }

    /**
     * Частично обновить роль пользователя (PATCH).
     */
    @PatchMapping("/{userId}/role")
    public ResponseEntity<Void> patchUserRole(@PathVariable Long userId, @RequestBody String newRole) {
        userService.updateUserRole(userId, newRole);
        return ResponseEntity.noContent().build();
    }

    /**
     * Удалить пользователя.
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
