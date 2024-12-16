package org.example.tourist.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody /*DTO для логина*/ Object loginRequest) {
        // Логика аутентификации, возвращаем токен или 401
        // Допустим, при успехе возвращаем JWT
        return ResponseEntity.ok("FakeJWTToken");
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "POST,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headAuth() {
        return ResponseEntity.ok().build();
    }
}
