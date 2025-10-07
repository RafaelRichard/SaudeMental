package com.saudemental.app.repository;

import com.saudemental.app.entity.Questionario;
import com.saudemental.app.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuestionarioRepository extends JpaRepository<Questionario, Long> {
    
    List<Questionario> findByUsuarioId(Long usuarioId);
    
    List<Questionario> findByUsuarioAndData(Usuario usuario, LocalDate data);
    
    List<Questionario> findByUsuarioIdOrderByDataDesc(Long usuarioId);
    
    List<Questionario> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
    
    @Query("SELECT q FROM Questionario q WHERE q.usuario.id = :usuarioId ORDER BY q.data DESC")
    List<Questionario> findHistoricoQuestionariosByUsuario(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT AVG(q.pontuacaoTotal) FROM Questionario q WHERE q.usuario.id = :usuarioId")
    Double findMediaPontuacaoByUsuario(@Param("usuarioId") Long usuarioId);
}