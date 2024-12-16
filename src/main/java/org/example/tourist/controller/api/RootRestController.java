package org.example.tourist.controller.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RootRestController {

    @GetMapping
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Tourist API");
    }

    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<Void> optionsRoot() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Allow", "GET,OPTIONS,HEAD");
        return ResponseEntity.ok().headers(headers).build();
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<Void> headRoot() {
        return ResponseEntity.ok().build();
    }
}
