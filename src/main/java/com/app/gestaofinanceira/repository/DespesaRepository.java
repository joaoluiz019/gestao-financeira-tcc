package com.app.gestaofinanceira.repository;

import com.app.gestaofinanceira.model.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DespesaRepository extends JpaRepository<Despesa, UUID> {
}
