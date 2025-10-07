package com.saudemental.app.controller;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.entity.RegistroExercicio;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.ExercicioRepository;
import com.saudemental.app.repository.RegistroExercicioRepository;
import com.saudemental.app.repository.UsuarioRepository;
import com.saudemental.app.service.RegistroExercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/exercicios")
@CrossOrigin(origins = "*")
public class RegistroExercicioController {
    
    @Autowired
    private RegistroExercicioRepository registroExercicioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @Autowired
    private RegistroExercicioService registroExercicioService;
    
    @PostMapping("/{exercicioId}")
    public ResponseEntity<?> registrarExercicio(
            @PathVariable Long usuarioId, 
            @PathVariable Long exercicioId,
            @RequestParam(defaultValue = "true") Boolean realizado,
            @RequestParam(required = false) String observacoes) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            Optional<Exercicio> exercicio = exercicioRepository.findById(exercicioId);
            
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            if (exercicio.isEmpty()) {
                return ResponseEntity.badRequest().body("Exercício não encontrado");
            }
            
            // Verificar se já existe um registro para hoje
            LocalDate hoje = LocalDate.now();
            Optional<RegistroExercicio> registroExistente = registroExercicioRepository
                .findByUsuarioAndExercicioAndData(usuario.get(), exercicio.get(), hoje);
            
            RegistroExercicio registro;
            if (registroExistente.isPresent()) {
                // Atualizar registro existente
                registro = registroExistente.get();
                registro.setRealizado(realizado);
                if (observacoes != null) {
                    registro.setObservacoes(observacoes);
                }
            } else {
                // Criar novo registro
                registro = new RegistroExercicio(usuario.get(), exercicio.get(), realizado, observacoes);
            }
            
            RegistroExercicio registroSalvo = registroExercicioRepository.save(registro);
            return ResponseEntity.status(HttpStatus.CREATED).body(registroSalvo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao registrar exercício: " + e.getMessage());
        }
    }
    
    @GetMapping("/historico")
    public ResponseEntity<?> buscarHistoricoExercicios(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            List<RegistroExercicio> historico = registroExercicioRepository.findByUsuarioId(usuarioId);
            return ResponseEntity.ok(historico);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar histórico: " + e.getMessage());
        }
    }
    
    @GetMapping("/historico/realizados")
    public ResponseEntity<?> buscarExerciciosRealizados(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            List<RegistroExercicio> exerciciosRealizados = registroExercicioRepository
                .findExerciciosRealizadosByUsuario(usuarioId);
            return ResponseEntity.ok(exerciciosRealizados);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar exercícios realizados: " + e.getMessage());
        }
    }
    
    @GetMapping("/hoje")
    public ResponseEntity<?> buscarExerciciosHoje(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            LocalDate hoje = LocalDate.now();
            List<RegistroExercicio> exerciciosHoje = registroExercicioRepository
                .findByUsuarioIdAndData(usuarioId, hoje);
                
            return ResponseEntity.ok(exerciciosHoje);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar exercícios de hoje: " + e.getMessage());
        }
    }
    
    @GetMapping("/estatisticas/hoje")
    public ResponseEntity<?> buscarEstatisticasHoje(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            LocalDate hoje = LocalDate.now();
            Long exerciciosRealizadosHoje = registroExercicioRepository
                .countExerciciosRealizadosNoDia(usuarioId, hoje);
                
            return ResponseEntity.ok(java.util.Map.of(
                "data", hoje,
                "exerciciosRealizados", exerciciosRealizadosHoje
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar estatísticas: " + e.getMessage());
        }
    }
    
    @GetMapping("/estatisticas")
    public ResponseEntity<?> buscarEstatisticasGerais(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            Map<String, Object> stats = registroExercicioService.obterEstatisticas(usuarioId);
            return ResponseEntity.ok(stats);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar estatísticas: " + e.getMessage());
        }
    }
}