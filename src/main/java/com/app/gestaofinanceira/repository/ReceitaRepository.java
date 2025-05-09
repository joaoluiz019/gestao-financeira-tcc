package com.app.gestaofinanceira.repository;

import com.app.gestaofinanceira.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ReceitaRepository extends JpaRepository<Receita, UUID> {
}
