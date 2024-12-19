package org.example.tourist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {


    @GetMapping("/")
    public String home() {
        return "index";
    }
}
