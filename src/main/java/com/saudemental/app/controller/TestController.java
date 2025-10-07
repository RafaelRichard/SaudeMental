package com.saudemental.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Spring Boot está funcionando!";
    }
    
    @GetMapping("/test-template")
    public String testTemplate() {
        return "test";
    }
}