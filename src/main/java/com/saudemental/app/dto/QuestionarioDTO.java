package com.saudemental.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class QuestionarioDTO {
    
    @NotNull(message = "Humor geral é obrigatório")
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    private Integer humorGeral;
    
    @NotNull(message = "Nível de ansiedade é obrigatório")
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    private Integer nivelAnsiedade;
    
    @NotNull(message = "Qualidade do sono é obrigatória")
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    private Integer qualidadeSono;
    
    @NotNull(message = "Energia e disposição é obrigatória")
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    private Integer energiaDisposicao;
    
    @NotNull(message = "Concentração é obrigatória")
    @Min(value = 1, message = "Pontuação deve ser entre 1 e 5")
    @Max(value = 5, message = "Pontuação deve ser entre 1 e 5")
    private Integer concentracao;
    
    private String observacoes;
    
    // Construtores
    public QuestionarioDTO() {}
    
    public QuestionarioDTO(Integer humorGeral, Integer nivelAnsiedade, Integer qualidadeSono, 
                          Integer energiaDisposicao, Integer concentracao, String observacoes) {
        this.humorGeral = humorGeral;
        this.nivelAnsiedade = nivelAnsiedade;
        this.qualidadeSono = qualidadeSono;
        this.energiaDisposicao = energiaDisposicao;
        this.concentracao = concentracao;
        this.observacoes = observacoes;
    }
    
    // Getters e Setters
    public Integer getHumorGeral() {
        return humorGeral;
    }
    
    public void setHumorGeral(Integer humorGeral) {
        this.humorGeral = humorGeral;
    }
    
    public Integer getNivelAnsiedade() {
        return nivelAnsiedade;
    }
    
    public void setNivelAnsiedade(Integer nivelAnsiedade) {
        this.nivelAnsiedade = nivelAnsiedade;
    }
    
    public Integer getQualidadeSono() {
        return qualidadeSono;
    }
    
    public void setQualidadeSono(Integer qualidadeSono) {
        this.qualidadeSono = qualidadeSono;
    }
    
    public Integer getEnergiaDisposicao() {
        return energiaDisposicao;
    }
    
    public void setEnergiaDisposicao(Integer energiaDisposicao) {
        this.energiaDisposicao = energiaDisposicao;
    }
    
    public Integer getConcentracao() {
        return concentracao;
    }
    
    public void setConcentracao(Integer concentracao) {
        this.concentracao = concentracao;
    }
    
    public String getObservacoes() {
        return observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}