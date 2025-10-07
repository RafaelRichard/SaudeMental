package com.saudemental.app.controller;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.repository.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/exercicios")
public class ExercicioWebController {
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @GetMapping
    public String listarExercicios(Model model) {
        try {
            List<Exercicio> exercicios = exercicioRepository.findAll();
            model.addAttribute("exercicios", exercicios);
            return "exercicios";
        } catch (Exception e) {
            System.err.println("Erro ao carregar exercícios: " + e.getMessage());
            return "exercicios";
        }
    }
}
