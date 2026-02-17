package com.app.library.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String root() {
        return "Library API root. Use /api/...";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Library application";
    }
}
