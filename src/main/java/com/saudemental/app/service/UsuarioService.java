package com.saudemental.app.service;

import com.saudemental.app.dto.UsuarioDTO;
import com.saudemental.app.entity.Usuario;
import com.saudemental.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso");
        }
        
        Usuario usuario = new Usuario(
            usuarioDTO.getNome(),
            usuarioDTO.getEmail(),
            usuarioDTO.getSenha() // TODO: Adicionar hash de senha futuramente
        );
        
        return usuarioRepository.save(usuario);
    }
    
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }
    
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
    
    public Usuario atualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        // Verificar se o email não está sendo usado por outro usuário
        if (!usuario.getEmail().equals(usuarioDTO.getEmail()) && 
            usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("Email já está em uso por outro usuário");
        }
        
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        
        if (usuarioDTO.getSenha() != null && !usuarioDTO.getSenha().trim().isEmpty()) {
            usuario.setSenha(usuarioDTO.getSenha()); // TODO: Hash
        }
        
        return usuarioRepository.save(usuario);
    }
    
    public void deletarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
    
    public long contarUsuarios() {
        return usuarioRepository.count();
    }
}
