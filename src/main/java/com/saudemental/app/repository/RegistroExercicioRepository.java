package com.saudemental.app.repository;

import com.saudemental.app.entity.RegistroExercicio;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.entity.Exercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistroExercicioRepository extends JpaRepository<RegistroExercicio, Long> {
    
    List<RegistroExercicio> findByUsuarioId(Long usuarioId);
    
    List<RegistroExercicio> findByUsuarioIdAndRealizado(Long usuarioId, Boolean realizado);
    
    List<RegistroExercicio> findByUsuarioAndData(Usuario usuario, LocalDate data);
    
    List<RegistroExercicio> findByUsuarioAndDataBetween(Usuario usuario, LocalDate dataInicio, LocalDate dataFim);
    
    List<RegistroExercicio> findByUsuarioIdAndData(Long usuarioId, LocalDate data);
    
    List<RegistroExercicio> findByUsuarioIdAndDataBetween(Long usuarioId, LocalDate dataInicio, LocalDate dataFim);
    
    Optional<RegistroExercicio> findByUsuarioAndExercicioAndData(Usuario usuario, Exercicio exercicio, LocalDate data);
    
    @Query("SELECT r FROM RegistroExercicio r WHERE r.usuario.id = :usuarioId AND r.realizado = true")
    List<RegistroExercicio> findExerciciosRealizadosByUsuario(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT COUNT(r) FROM RegistroExercicio r WHERE r.usuario.id = :usuarioId AND r.realizado = true AND r.data = :data")
    Long countExerciciosRealizadosNoDia(@Param("usuarioId") Long usuarioId, @Param("data") LocalDate data);
}