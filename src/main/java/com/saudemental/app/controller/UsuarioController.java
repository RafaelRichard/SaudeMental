package com.saudemental.app.controller;

import com.saudemental.app.dto.UsuarioDTO;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @PostMapping
    public ResponseEntity<?> criarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            // Verificar se o email já existe
            if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                return ResponseEntity.badRequest()
                    .body("Email já está em uso");
            }
            
            Usuario usuario = new Usuario(
                usuarioDTO.getNome(),
                usuarioDTO.getEmail(),
                usuarioDTO.getSenha()
            );
            
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            
            // Remover a senha do retorno por segurança
            usuarioSalvo.setSenha(null);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioSalvo);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao criar usuário: " + e.getMessage());
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            
            // Remover senhas por segurança
            usuarios.forEach(usuario -> usuario.setSenha(null));
            
            return ResponseEntity.ok(usuarios);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarUsuario(@PathVariable Long id) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(id);
            
            if (usuario.isPresent()) {
                Usuario usuarioEncontrado = usuario.get();
                usuarioEncontrado.setSenha(null); // Remover senha por segurança
                return ResponseEntity.ok(usuarioEncontrado);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar usuário: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        try {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
            
            if (usuarioExistente.isPresent()) {
                Usuario usuario = usuarioExistente.get();
                
                // Verificar se o email não está sendo usado por outro usuário
                if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
                    usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
                    return ResponseEntity.badRequest()
                        .body("Email já está em uso por outro usuário");
                }
                
                usuario.setNome(usuarioDTO.getNome());
                usuario.setEmail(usuarioDTO.getEmail());
                if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().trim().isEmpty()) {
                    usuario.setSenha(usuarioDTO.getSenha());
                }
                
                Usuario usuarioAtualizado = usuarioRepository.save(usuario);
                usuarioAtualizado.setSenha(null); // Remover senha por segurança
                
                return ResponseEntity.ok(usuarioAtualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarUsuario(@PathVariable Long id) {
        try {
            if (usuarioRepository.existsById(id)) {
                usuarioRepository.deleteById(id);
                return ResponseEntity.ok("Usuário deletado com sucesso");
            } else {
                return ResponseEntity.notFound().build();
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao deletar usuário: " + e.getMessage());
        }
    }
}