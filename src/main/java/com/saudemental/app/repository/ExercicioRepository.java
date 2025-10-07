package com.saudemental.app.repository;

import com.saudemental.app.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {
    
    List<Exercicio> findByTipo(Exercicio.TipoExercicio tipo);
    
    List<Exercicio> findByTituloContainingIgnoreCase(String titulo);
}