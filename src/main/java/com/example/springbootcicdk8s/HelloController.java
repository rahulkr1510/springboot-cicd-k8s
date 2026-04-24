package com.example.springbootcicdk8s;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String home() {
        return "Hello from Spring Boot CI/CD on Kubernetes!";
    }

    @GetMapping("/health")
    public String health() {
        return "UP";
    }
}