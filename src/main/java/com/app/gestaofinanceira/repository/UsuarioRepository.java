package com.app.gestaofinanceira.repository;

import com.app.gestaofinanceira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Usuario findByEmail(String email);
    List<Usuario> findByNome(String nome);
}
