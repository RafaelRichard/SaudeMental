package com.saudemental.app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

@Entity
@Table(name = "questionarios")
public class Questionario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDate data;
    
    // Questões básicas de ansiedade/depressão (escala 1-5)
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    @Column(name = "humor_geral", nullable = false)
    private Integer humorGeral;
    
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    @Column(name = "nivel_ansiedade", nullable = false)
    private Integer nivelAnsiedade;
    
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    @Column(name = "qualidade_sono", nullable = false)
    private Integer qualidadeSono;
    
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    @Column(name = "energia_disposicao", nullable = false)
    private Integer energiaDisposicao;
    
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    @Column(name = "concentracao", nullable = false)
    private Integer concentracao;
    
    @Column(name = "pontuacao_total", nullable = false)
    private Integer pontuacaoTotal;
    
    @Column(length = 1000)
    private String observacoes;
    
    // Construtores
    public Questionario() {
        this.data = LocalDate.now();
    }
    
    public Questionario(Usuario usuario, Integer humorGeral, Integer nivelAnsiedade, 
                       Integer qualidadeSono, Integer energiaDisposicao, Integer concentracao) {
        this();
        this.usuario = usuario;
        this.humorGeral = humorGeral;
        this.nivelAnsiedade = nivelAnsiedade;
        this.qualidadeSono = qualidadeSono;
        this.energiaDisposicao = energiaDisposicao;
        this.concentracao = concentracao;
        this.calcularPontuacaoTotal();
    }
    
    // Método para calcular pontuação total
    public void calcularPontuacaoTotal() {
        if (humorGeral != null && nivelAnsiedade != null && qualidadeSono != null 
            && energiaDisposicao != null && concentracao != null) {
            this.pontuacaoTotal = humorGeral + nivelAnsiedade + qualidadeSono + 
                                 energiaDisposicao + concentracao;
        }
    }
    
    // Método para avaliar nível geral
    public String getNivelGeral() {
        if (pontuacaoTotal == null) {
            return "Não avaliado";
        }
        
        if (pontuacaoTotal <= 10) {
            return "Baixo";
        } else if (pontuacaoTotal <= 15) {
            return "Moderado";
        } else if (pontuacaoTotal <= 20) {
            return "Alto";
        } else {
            return "Muito Alto";
        }
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
    
    public LocalDate getData() {
        return data;
    }
    
    public void setData(LocalDate data) {
        this.data = data;
    }
    
    public Integer getHumorGeral() {
        return humorGeral;
    }
    
    public void setHumorGeral(Integer humorGeral) {
        this.humorGeral = humorGeral;
        calcularPontuacaoTotal();
    }
    
    public Integer getNivelAnsiedade() {
        return nivelAnsiedade;
    }
    
    public void setNivelAnsiedade(Integer nivelAnsiedade) {
        this.nivelAnsiedade = nivelAnsiedade;
        calcularPontuacaoTotal();
    }
    
    public Integer getQualidadeSono() {
        return qualidadeSono;
    }
    
    public void setQualidadeSono(Integer qualidadeSono) {
        this.qualidadeSono = qualidadeSono;
        calcularPontuacaoTotal();
    }
    
    public Integer getEnergiaDisposicao() {
        return energiaDisposicao;
    }
    
    public void setEnergiaDisposicao(Integer energiaDisposicao) {
        this.energiaDisposicao = energiaDisposicao;
        calcularPontuacaoTotal();
    }
    
    public Integer getConcentracao() {
        return concentracao;
    }
    
    public void setConcentracao(Integer concentracao) {
        this.concentracao = concentracao;
        calcularPontuacaoTotal();
    }
    
    public Integer getPontuacaoTotal() {
        return pontuacaoTotal;
    }
    
    public void setPontuacaoTotal(Integer pontuacaoTotal) {
        this.pontuacaoTotal = pontuacaoTotal;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}