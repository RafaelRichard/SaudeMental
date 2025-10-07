package com.saudemental.app.controller;

import com.saudemental.app.dto.QuestionarioDTO;
import com.saudemental.app.entity.Questionario;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.QuestionarioRepository;
import com.saudemental.app.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios/{usuarioId}/questionarios")
@CrossOrigin(origins = "*")
public class QuestionarioController {
    
    @Autowired
    private QuestionarioRepository questionarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping
    public ResponseEntity<?> salvarQuestionario(
            @PathVariable Long usuarioId, 
            @Valid @RequestBody QuestionarioDTO questionarioDTO) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);
            
            if (usuario.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            Questionario questionario = new Questionario(
                usuario.get(),
                questionarioDTO.getHumorGeral(),
                questionarioDTO.getNivelAnsiedade(),
                questionarioDTO.getQualidadeSono(),
                questionarioDTO.getEnergiaDisposicao(),
                questionarioDTO.getConcentracao()
            );
            
            if (questionarioDTO.getObservacoes() != null) {
                questionario.setObservacoes(questionarioDTO.getObservacoes());
            }
            
            Questionario questionarioSalvo = questionarioRepository.save(questionario);
            return ResponseEntity.status(HttpStatus.CREATED).body(questionarioSalvo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao salvar questionário: " + e.getMessage());
        }
    }
    
    @GetMapping("/historico")
    public ResponseEntity<?> buscarHistoricoQuestionarios(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            List<Questionario> historico = questionarioRepository
                .findHistoricoQuestionariosByUsuario(usuarioId);
            return ResponseEntity.ok(historico);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar histórico de questionários: " + e.getMessage());
        }
    }
    
    @GetMapping("/estatisticas")
    public ResponseEntity<?> buscarEstatisticas(@PathVariable Long usuarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            List<Questionario> questionarios = questionarioRepository.findByUsuarioId(usuarioId);
            Double mediaPontuacao = questionarioRepository.findMediaPontuacaoByUsuario(usuarioId);
            
            // Calcular estatísticas básicas
            int totalQuestionarios = questionarios.size();
            String nivelGeralMedio = "Não avaliado";
            
            if (mediaPontuacao != null) {
                int mediaInt = mediaPontuacao.intValue();
                if (mediaInt <= 10) {
                    nivelGeralMedio = "Baixo";
                } else if (mediaInt <= 15) {
                    nivelGeralMedio = "Moderado";
                } else if (mediaInt <= 20) {
                    nivelGeralMedio = "Alto";
                } else {
                    nivelGeralMedio = "Muito Alto";
                }
            }
            
            return ResponseEntity.ok(java.util.Map.of(
                "totalQuestionarios", totalQuestionarios,
                "mediaPontuacao", mediaPontuacao != null ? mediaPontuacao : 0.0,
                "nivelGeralMedio", nivelGeralMedio
            ));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar estatísticas: " + e.getMessage());
        }
    }
    
    @GetMapping("/{questionarioId}")
    public ResponseEntity<?> buscarQuestionario(
            @PathVariable Long usuarioId, 
            @PathVariable Long questionarioId) {
        try {
            if (!usuarioRepository.existsById(usuarioId)) {
                return ResponseEntity.badRequest().body("Usuário não encontrado");
            }
            
            Optional<Questionario> questionario = questionarioRepository.findById(questionarioId);
            
            if (questionario.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            // Verificar se o questionário pertence ao usuário
            if (!questionario.get().getUsuario().getId().equals(usuarioId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Questionário não pertence ao usuário");
            }
            
            return ResponseEntity.ok(questionario.get());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar questionário: " + e.getMessage());
        }
    }
}