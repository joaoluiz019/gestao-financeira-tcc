package com.app.gestaofinanceira.service;

import com.app.gestaofinanceira.config.JwtUtil;
import com.app.gestaofinanceira.emum.Status;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UsuarioService {

    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String criar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);
        return jwtUtil.gerarTokenUsuario(usuario.getEmail());
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(UUID id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Transactional
    public Usuario atualizar(UUID id, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    usuario.setNome(usuarioAtualizado.getNome());
                    if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                        usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
                    }
                    usuario.setStatus(usuarioAtualizado.getStatus());
                    return usuarioRepository.save(usuario);
                })
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public void alterarStatus(UUID id, Status status) {
        usuarioRepository.findById(id)
                .ifPresent(usuario -> {
                    usuario.setStatus(status);
                    usuarioRepository.save(usuario);
                });
    }

    @Transactional
    public void deletar(UUID id) {
        usuarioRepository.deleteById(id);
    }

    public String autenticaUsuario(String email, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(senha, usuario.get().getSenha())) {
            return jwtUtil.gerarTokenUsuario(usuario.get().getEmail());
        }
        return null;
    }
}
