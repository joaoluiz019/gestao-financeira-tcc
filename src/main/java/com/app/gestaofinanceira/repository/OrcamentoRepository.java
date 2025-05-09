package com.app.gestaofinanceira.repository;

import com.app.gestaofinanceira.model.Orcamento;
import com.app.gestaofinanceira.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

public interface OrcamentoRepository extends JpaRepository<Orcamento, UUID> {

    @Query("SELECT o FROM Orcamento o WHERE o.mes = :mes AND o.usuario = :usuario")
    Optional<Orcamento> buscarPorMesAnoEUsuario(@Param("mes") String mes, @Param("usuario") Usuario usuario);

    List<Orcamento> findByUsuario(Usuario usuario);
}
