package com.saudemental.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SaudeMentalApplication {

    public static void main(String[] args) {
        System.out.println(">>> Iniciando aplicação Saúde Mental...");
        SpringApplication.run(SaudeMentalApplication.class, args);
        System.out.println(">>> Aplicação iniciada com sucesso!");
    }
}