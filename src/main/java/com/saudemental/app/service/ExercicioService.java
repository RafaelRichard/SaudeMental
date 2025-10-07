package com.saudemental.app.service;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.repository.ExercicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExercicioService {
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    public Exercicio criarExercicio(Exercicio exercicio) {
        return exercicioRepository.save(exercicio);
    }
    
    public List<Exercicio> listarTodos() {
        return exercicioRepository.findAll();
    }
    
    public Optional<Exercicio> buscarPorId(Long id) {
        return exercicioRepository.findById(id);
    }
    
    public List<Exercicio> buscarPorTipo(Exercicio.TipoExercicio tipo) {
        return exercicioRepository.findByTipo(tipo);
    }
    
    public List<Exercicio> buscarPorTitulo(String titulo) {
        return exercicioRepository.findByTituloContainingIgnoreCase(titulo);
    }
    
    public Exercicio atualizarExercicio(Long id, Exercicio exercicioAtualizado) {
        Exercicio exercicio = exercicioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Exercício não encontrado"));
        
        exercicio.setTitulo(exercicioAtualizado.getTitulo());
        exercicio.setDescricao(exercicioAtualizado.getDescricao());
        exercicio.setTipo(exercicioAtualizado.getTipo());
        
        return exercicioRepository.save(exercicio);
    }
    
    public void deletarExercicio(Long id) {
        if (!exercicioRepository.existsById(id)) {
            throw new RuntimeException("Exercício não encontrado");
        }
        exercicioRepository.deleteById(id);
    }
    
    public long contarExercicios() {
        return exercicioRepository.count();
    }
    
    public long contarPorTipo(Exercicio.TipoExercicio tipo) {
        return exercicioRepository.findByTipo(tipo).size();
    }
}
