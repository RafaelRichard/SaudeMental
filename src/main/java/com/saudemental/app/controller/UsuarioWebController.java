package com.saudemental.app.controller;

import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @GetMapping
    public String listarUsuarios(Model model) {
        System.out.println(">>> Acessando página de usuários");
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            // Remover senhas por segurança antes de passar para a view
            usuarios.forEach(usuario -> {
                if (usuario.getSenha() != null) {
                    usuario.setSenha(null);
                }
            });
            model.addAttribute("usuarios", usuarios);
            System.out.println(">>> Encontrados " + usuarios.size() + " usuários");
            return "usuarios";
        } catch (Exception e) {
            System.err.println(">>> Erro ao carregar usuários: " + e.getMessage());
            e.printStackTrace();
            return "usuarios";
        }
    }
}