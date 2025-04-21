package com.app.gestaofinanceira.service;

import com.app.gestaofinanceira.emum.Status;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public Usuario criaUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario buscaUsuarioPorId(UUID id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscaUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> buscaUsuarioPorNome(String nome) {
        return usuarioRepository.findByNome(nome);
    }

    public Usuario atualizaUsuario(UUID id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        throw new RuntimeException("Usuário não encontrado");
    }

    public void deletaUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        usuario.setStatus(Status.INATIVO);
        usuarioRepository.save(usuario);
    }
}
