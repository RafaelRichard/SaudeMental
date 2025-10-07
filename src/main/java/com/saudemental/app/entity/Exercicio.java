package com.saudemental.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercicios")
public class Exercicio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Título é obrigatório")
    @Size(min = 2, max = 200, message = "Título deve ter entre 2 e 200 caracteres")
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(nullable = false, length = 1000)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoExercicio tipo;
    
    @OneToMany(mappedBy = "exercicio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RegistroExercicio> registros = new ArrayList<>();
    
    // Enum para tipos de exercício
    public enum TipoExercicio {
        RESPIRACAO("Respiração"),
        MEDITACAO("Meditação"),
        DIARIO("Diário"),
        RELAXAMENTO("Relaxamento"),
        MINDFULNESS("Mindfulness");
        
        private final String descricao;
        
        TipoExercicio(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
    }
    
    // Construtores
    public Exercicio() {}
    
    public Exercicio(String titulo, String descricao, TipoExercicio tipo) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public TipoExercicio getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoExercicio tipo) {
        this.tipo = tipo;
    }
    
    public List<RegistroExercicio> getRegistros() {
        return registros;
    }
    
    public void setRegistros(List<RegistroExercicio> registros) {
        this.registros = registros;
    }
}