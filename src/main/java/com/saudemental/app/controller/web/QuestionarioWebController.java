package com.saudemental.app.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questionarios")
public class QuestionarioWebController {
    
    @GetMapping
    public String listarQuestionarios() {
        return "questionarios";
    }
}
