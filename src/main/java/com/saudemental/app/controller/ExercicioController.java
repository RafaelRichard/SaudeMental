package com.saudemental.app.controller;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.repository.ExercicioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercicios")
@CrossOrigin(origins = "*")
public class ExercicioController {
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @PostMapping
    public ResponseEntity<?> criarExercicio(@Valid @RequestBody Exercicio exercicio) {
        try {
            Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(exercicioSalvo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar exercício: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Exercicio>> listarExercicios() {
        try {
            List<Exercicio> exercicios = exercicioRepository.findAll();
            return ResponseEntity.ok(exercicios);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarExercicio(@PathVariable Long id) {
        try {
            Optional<Exercicio> exercicio = exercicioRepository.findById(id);
            
            if (exercicio.isPresent()) {
                return ResponseEntity.ok(exercicio.get());
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar exercício: " + e.getMessage());
        }
    }
    
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<?> buscarExerciciosPorTipo(@PathVariable String tipo) {
        try {
            Exercicio.TipoExercicio tipoExercicio = Exercicio.TipoExercicio.valueOf(tipo.toUpperCase());
            List<Exercicio> exercicios = exercicioRepository.findByTipo(tipoExercicio);
            return ResponseEntity.ok(exercicios);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body("Tipo de exercício inválido. Tipos válidos: " + 
                      java.util.Arrays.toString(Exercicio.TipoExercicio.values()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar exercícios: " + e.getMessage());
        }
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<?> buscarExerciciosPorTitulo(@RequestParam String titulo) {
        try {
            List<Exercicio> exercicios = exercicioRepository.findByTituloContainingIgnoreCase(titulo);
            return ResponseEntity.ok(exercicios);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar exercícios: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarExercicio(@PathVariable Long id, @Valid @RequestBody Exercicio exercicioAtualizado) {
        try {
            Optional<Exercicio> exercicioExistente = exercicioRepository.findById(id);
            
            if (exercicioExistente.isPresent()) {
                Exercicio exercicio = exercicioExistente.get();
                exercicio.setTitulo(exercicioAtualizado.getTitulo());
                exercicio.setDescricao(exercicioAtualizado.getDescricao());
                exercicio.setTipo(exercicioAtualizado.getTipo());
                
                Exercicio exercicioSalvo = exercicioRepository.save(exercicio);
                return ResponseEntity.ok(exercicioSalvo);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao atualizar exercício: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarExercicio(@PathVariable Long id) {
        try {
            if (exercicioRepository.existsById(id)) {
                exercicioRepository.deleteById(id);
                return ResponseEntity.ok("Exercício deletado com sucesso");
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao deletar exercício: " + e.getMessage());
        }
    }
}