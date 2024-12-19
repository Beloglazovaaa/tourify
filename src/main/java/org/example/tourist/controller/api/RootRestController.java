package org.example.tourist.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")  // Все запросы будут начинаться с /api
public class RootRestController {

    /**
     * Обрабатывает GET-запросы на корень API (/api).
     * Возвращает приветственное сообщение.
     *
     * @return Приветственное сообщение
     */
    @GetMapping
    public ResponseEntity<String> home() {
        // Ответ с кодом 200 OK и сообщением
        return ResponseEntity.ok("Welcome to the Tourist API");
    }

    /**
     * Обрабатывает OPTIONS-запросы на корень API (/api).
     * Возвращает информацию о поддерживаемых методах для этого ресурса.
     *
     * @return HTTP-заголовки с разрешенными методами
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot() {
        // Создаем заголовки для ответа, указываем доступные методы
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,OPTIONS,HEAD");  // Разрешенные методы: GET, OPTIONS, HEAD
        return ResponseEntity.ok().headers(headers).build();  // Ответ с кодом 200 и заголовками
    }

    /**
     * Обрабатывает HEAD-запросы на корень API (/api).
     * Возвращает только метаданные без тела ответа.
     *
     * @return Пустой ответ с кодом 200 OK
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headRoot() {
        // Пустой ответ с кодом 200 OK
        return ResponseEntity.ok().build();
    }
}
