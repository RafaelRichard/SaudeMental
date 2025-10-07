package com.saudemental.app.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registros_exercicios")
public class RegistroExercicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercicio_id", nullable = false)
    private Exercicio exercicio;
    
    @Column(nullable = false)
    private LocalDate data;
    
    @Column(nullable = false)
    private Boolean realizado;
    
    @Column(length = 500)
    private String observacoes;
    
    // Construtores
    public RegistroExercicio() {
        this.data = LocalDate.now();
        this.realizado = false;
    }
    
    public RegistroExercicio(Usuario usuario, Exercicio exercicio, Boolean realizado) {
        this();
        this.usuario = usuario;
        this.exercicio = exercicio;
        this.realizado = realizado;
    }
    
    public RegistroExercicio(Usuario usuario, Exercicio exercicio, Boolean realizado, String observacoes) {
        this(usuario, exercicio, realizado);
        this.observacoes = observacoes;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Exercicio getExercicio() {
        return exercicio;
    }
    
    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
    }
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public Boolean getRealizado() {
        return realizado;
    }
    
    public void setRealizado(Boolean realizado) {
        this.realizado = realizado;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}