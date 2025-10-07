package com.saudemental.app.config;

import com.saudemental.app.entity.Exercicio;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.ExercicioRepository;
import com.saudemental.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar se já existem exercícios cadastrados
        if (exercicioRepository.count() == 0) {
            // Criar exercícios de exemplo
            criarExerciciosIniciais();
        }
        
        // Verificar se já existem usuários cadastrados
        if (usuarioRepository.count() == 0) {
            // Criar usuários de exemplo
            criarUsuariosIniciais();
        }
    }
    
    private void criarExerciciosIniciais() {
        // Exercícios de Respiração
        exercicioRepository.save(new Exercicio(
            "Respiração 4-7-8",
            "Inspire por 4 segundos, segure por 7 segundos e expire por 8 segundos. Repita 4 vezes.",
            Exercicio.TipoExercicio.RESPIRACAO
        ));
        
        exercicioRepository.save(new Exercicio(
            "Respiração Diafragmática",
            "Coloque uma mão no peito e outra no abdômen. Respire profundamente pelo nariz, fazendo o abdômen subir mais que o peito.",
            Exercicio.TipoExercicio.RESPIRACAO
        ));
        
        // Exercícios de Meditação
        exercicioRepository.save(new Exercicio(
            "Meditação Mindfulness - 5 minutos",
            "Sente-se confortavelmente e foque na respiração. Quando pensamentos surgirem, apenas observe e retorne a atenção à respiração.",
            Exercicio.TipoExercicio.MEDITACAO
        ));
        
        exercicioRepository.save(new Exercicio(
            "Escaneamento Corporal",
            "Deite-se e mentalmente 'escaneie' seu corpo dos pés à cabeça, observando sensações e relaxando cada parte.",
            Exercicio.TipoExercicio.MEDITACAO
        ));
        
        // Exercícios de Diário
        exercicioRepository.save(new Exercicio(
            "Gratidão Diária",
            "Escreva 3 coisas pelas quais você é grato hoje. Reflita sobre por que são importantes para você.",
            Exercicio.TipoExercicio.DIARIO
        ));
        
        exercicioRepository.save(new Exercicio(
            "Reflexão do Dia",
            "Escreva sobre como foi seu dia, incluindo emoções, desafios e conquistas. Seja honesto consigo mesmo.",
            Exercicio.TipoExercicio.DIARIO
        ));
        
        // Exercícios de Relaxamento
        exercicioRepository.save(new Exercicio(
            "Relaxamento Muscular Progressivo",
            "Tensione e relaxe cada grupo muscular do corpo, começando pelos pés e subindo até a cabeça.",
            Exercicio.TipoExercicio.RELAXAMENTO
        ));
        
        // Exercícios de Mindfulness
        exercicioRepository.save(new Exercicio(
            "Observação dos 5 Sentidos",
            "Identifique: 5 coisas que você vê, 4 que você ouve, 3 que você sente, 2 que você cheira, 1 que você prova.",
            Exercicio.TipoExercicio.MINDFULNESS
        ));
        
        System.out.println("Exercícios iniciais criados com sucesso!");
    }
    
    private void criarUsuariosIniciais() {
        // Usuários de teste
        usuarioRepository.save(new Usuario(
            "Rafael Richard de Almeida",
            "rafael.richard@email.com",
            "123456"
        ));
        
        System.out.println("Usuários iniciais criados com sucesso!");
    }
}