package org.example.tourist.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")  // Все запросы для аутентификации будут начинаться с /api/auth
public class AuthRestController {

    /**
     * Метод для аутентификации пользователя.
     * При успешной аутентификации возвращается JWT токен.
     *
     * @param loginRequest DTO, содержащий данные для входа пользователя (логин, пароль)
     * @return JWT токен, если аутентификация успешна
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody /*DTO для логина*/ Object loginRequest) {
        // Логика аутентификации
        // Например, здесь вы должны проверить, существует ли пользователь с такими данными
        // Если аутентификация успешна, генерируется JWT и отправляется пользователю
        // В реальной ситуации необходимо заменить строку ниже на фактическую логику с JWT.

        return ResponseEntity.ok("FakeJWTToken"); // Возвращаем фейковый токен для примера
    }

    /**
     * OPTIONS-запрос для получения информации о поддерживаемых методах для /api/auth.
     * Возвращает заголовок Allow с допустимыми методами для этого ресурса.
     *
     * @return Ответ с заголовками, указывающими на поддерживаемые методы
     */
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "POST,OPTIONS,HEAD");  // Указываем разрешенные HTTP методы для этого ресурса
        return ResponseEntity.ok().headers(headers).build();
    }

    /**
     * HEAD-запрос для получения метаданных для ресурса /api/auth.
     * Не возвращает тело ответа, только статус.
     *
     * @return Пустой ответ с кодом 200 OK
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headAuth() {
        return ResponseEntity.ok().build();  // Пустой ответ с кодом 200 OK
    }
}

