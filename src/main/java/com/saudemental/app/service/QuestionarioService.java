package com.saudemental.app.service;

import com.saudemental.app.dto.QuestionarioDTO;
import com.saudemental.app.entity.Questionario;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.QuestionarioRepository;
import com.saudemental.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionarioService {
    
    @Autowired
    private QuestionarioRepository questionarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Questionario salvarQuestionario(Long usuarioId, QuestionarioDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        Questionario questionario = new Questionario(
            usuario,
            dto.getHumorGeral(),
            dto.getNivelAnsiedade(),
            dto.getQualidadeSono(),
            dto.getEnergiaDisposicao(),
            dto.getConcentracao()
        );
        
        if (dto.getObservacoes() != null) {
            questionario.setObservacoes(dto.getObservacoes());
        }
        
        return questionarioRepository.save(questionario);
    }
    
    public List<Questionario> buscarHistorico(Long usuarioId) {
        return questionarioRepository.findHistoricoQuestionariosByUsuario(usuarioId);
    }
    
    public List<Questionario> buscarPorData(Long usuarioId, LocalDate data) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return questionarioRepository.findByUsuarioAndData(usuario, data);
    }
    
    public Map<String, Object> obterEstatisticas(Long usuarioId) {
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        List<Questionario> questionarios = questionarioRepository.findByUsuarioId(usuarioId);
        Double mediaPontuacao = questionarioRepository.findMediaPontuacaoByUsuario(usuarioId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestionarios", questionarios.size());
        stats.put("mediaPontuacao", mediaPontuacao != null ? mediaPontuacao : 0.0);
        
        // Classificar nível geral médio
        String nivelGeral = "Não avaliado";
        if (mediaPontuacao != null) {
            int mediaInt = mediaPontuacao.intValue();
            if (mediaInt <= 10) {
                nivelGeral = "Excelente";
            } else if (mediaInt <= 15) {
                nivelGeral = "Bom";
            } else if (mediaInt <= 20) {
                nivelGeral = "Moderado";
            } else {
                nivelGeral = "Necessita atenção";
            }
        }
        stats.put("nivelGeral", nivelGeral);
        
        // Calcular tendência (últimos 5 questionários)
        if (questionarios.size() >= 2) {
            List<Questionario> ultimos5 = questionarios.subList(
                Math.max(0, questionarios.size() - 5), questionarios.size()
            );
            
            double mediaPrimeiros = ultimos5.subList(0, ultimos5.size() / 2).stream()
                .mapToInt(Questionario::getPontuacaoTotal)
                .average()
                .orElse(0);
            
            double mediaUltimos = ultimos5.subList(ultimos5.size() / 2, ultimos5.size()).stream()
                .mapToInt(Questionario::getPontuacaoTotal)
                .average()
                .orElse(0);
            
            String tendencia;
            if (mediaUltimos < mediaPrimeiros - 2) {
                tendencia = "Melhorando";
            } else if (mediaUltimos > mediaPrimeiros + 2) {
                tendencia = "Piorando";
            } else {
                tendencia = "Estável";
            }
            stats.put("tendencia", tendencia);
        } else {
            stats.put("tendencia", "Dados insuficientes");
        }
        
        // Médias por categoria
        if (!questionarios.isEmpty()) {
            stats.put("mediaHumor", questionarios.stream()
                .mapToInt(Questionario::getHumorGeral).average().orElse(0));
            stats.put("mediaAnsiedade", questionarios.stream()
                .mapToInt(Questionario::getNivelAnsiedade).average().orElse(0));
            stats.put("mediaSono", questionarios.stream()
                .mapToInt(Questionario::getQualidadeSono).average().orElse(0));
            stats.put("mediaEnergia", questionarios.stream()
                .mapToInt(Questionario::getEnergiaDisposicao).average().orElse(0));
            stats.put("mediaConcentracao", questionarios.stream()
                .mapToInt(Questionario::getConcentracao).average().orElse(0));
        }
        
        return stats;
    }
    
    /**
     * Verifica se o usuário já preencheu o questionário hoje
     */
    public boolean preencheuHoje(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        List<Questionario> hoje = questionarioRepository.findByUsuarioAndData(usuario, LocalDate.now());
        return !hoje.isEmpty();
    }
}
