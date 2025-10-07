package com.saudemental.app.service;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.entity.RegistroExercicio;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.ExercicioRepository;
import com.saudemental.app.repository.RegistroExercicioRepository;
import com.saudemental.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RegistroExercicioService {
    
    @Autowired
    private RegistroExercicioRepository registroRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    public RegistroExercicio registrarExercicio(Long usuarioId, Long exercicioId, Boolean realizado, String observacoes) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Exercicio exercicio = exercicioRepository.findById(exercicioId)
            .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));
        
        // Verificar se já existe registro para hoje
        LocalDate hoje = LocalDate.now();
        Optional<RegistroExercicio> registroExistente = 
            registroRepository.findByUsuarioAndExercicioAndData(usuario, exercicio, hoje);
        
        RegistroExercicio registro;
        if (registroExistente.isPresent()) {
            registro = registroExistente.get();
            registro.setRealizado(realizado);
            if (observacoes != null) {
                registro.setObservacoes(observacoes);
            }
        } else {
            registro = new RegistroExercicio(usuario, exercicio, realizado, observacoes);
        }
        
        return registroRepository.save(registro);
    }
    
    public List<RegistroExercicio> buscarHistorico(Long usuarioId) {
        return registroRepository.findByUsuarioId(usuarioId);
    }
    
    public List<RegistroExercicio> buscarExerciciosRealizados(Long usuarioId) {
        return registroRepository.findByUsuarioIdAndRealizado(usuarioId, true);
    }
    
    public List<RegistroExercicio> buscarPorData(Long usuarioId, LocalDate data) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return registroRepository.findByUsuarioAndData(usuario, data);
    }
    
    public List<RegistroExercicio> buscarEntreDatas(Long usuarioId, LocalDate inicio, LocalDate fim) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return registroRepository.findByUsuarioAndDataBetween(usuario, inicio, fim);
    }
    
    /**
     * Calcula a sequência (streak) de dias consecutivos com exercícios realizados
     */
    public int calcularStreak(Long usuarioId) {
        List<RegistroExercicio> exerciciosRealizados = buscarExerciciosRealizados(usuarioId);
        
        if (exerciciosRealizados.isEmpty()) {
            return 0;
        }
        
        LocalDate hoje = LocalDate.now();
        int streak = 0;
        LocalDate dataVerificacao = hoje;
        
        while (true) {
            LocalDate finalDataVerificacao = dataVerificacao;
            boolean temExercicioNodia = exerciciosRealizados.stream()
                .anyMatch(r -> r.getData().equals(finalDataVerificacao));
            
            if (temExercicioNodia) {
                streak++;
                dataVerificacao = dataVerificacao.minusDays(1);
            } else {
                break;
            }
        }
        
        return streak;
    }
    
    /**
     * Retorna estatísticas de exercícios realizados
     */
    public Map<String, Object> obterEstatisticas(Long usuarioId) {
        List<RegistroExercicio> todosRegistros = buscarHistorico(usuarioId);
        List<RegistroExercicio> realizados = buscarExerciciosRealizados(usuarioId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRegistros", todosRegistros.size());
        stats.put("totalRealizados", realizados.size());
        stats.put("totalNaoRealizados", todosRegistros.size() - realizados.size());
        stats.put("streak", calcularStreak(usuarioId));
        
        if (!todosRegistros.isEmpty()) {
            double percentualConclusao = (realizados.size() * 100.0) / todosRegistros.size();
            stats.put("percentualConclusao", Math.round(percentualConclusao * 100.0) / 100.0);
        } else {
            stats.put("percentualConclusao", 0.0);
        }
        
        return stats;
    }
}
