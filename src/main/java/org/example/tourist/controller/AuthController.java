package org.example.tourist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
