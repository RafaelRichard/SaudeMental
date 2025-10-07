package com.saudemental.app.controller;

import com.saudemental.app.service.QuestionarioService;
import com.saudemental.app.service.RegistroExercicioService;
import com.saudemental.app.service.UsuarioService;
import com.saudemental.app.service.ExercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ExercicioService exercicioService;
    
    @Autowired
    private RegistroExercicioService registroService;
    
    @Autowired
    private QuestionarioService questionarioService;
    
    /**
     * Retorna estatísticas gerais do sistema
     */
    @GetMapping("/stats/geral")
    public ResponseEntity<Map<String, Object>> estatisticasGerais() {
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsuarios", usuarioService.contarUsuarios());
            stats.put("totalExercicios", exercicioService.contarExercicios());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Retorna dashboard completo de um usuário
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> dashboardUsuario(@PathVariable Long usuarioId) {
        try {
            if (usuarioService.buscarPorId(usuarioId).isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            Map<String, Object> dashboard = new HashMap<>();
            
            // Estatísticas de exercícios
            Map<String, Object> statsExercicios = registroService.obterEstatisticas(usuarioId);
            dashboard.put("exercicios", statsExercicios);
            
            // Estatísticas de questionários
            Map<String, Object> statsQuestionarios = questionarioService.obterEstatisticas(usuarioId);
            dashboard.put("questionarios", statsQuestionarios);
            
            // Streak atual
            dashboard.put("streakAtual", statsExercicios.get("streak"));
            
            // Verificar se preencheu questionário hoje
            dashboard.put("preencheuQuestionarioHoje", questionarioService.preencheuHoje(usuarioId));
            
            return ResponseEntity.ok(dashboard);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Erro ao buscar dashboard: " + e.getMessage());
        }
    }
}
