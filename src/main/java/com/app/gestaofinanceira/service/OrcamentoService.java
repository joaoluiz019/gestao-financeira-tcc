package com.app.gestaofinanceira.service;

import com.app.gestaofinanceira.model.Orcamento;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.OrcamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrcamentoService {

    private final OrcamentoRepository orcamentoRepository;

    public OrcamentoService(OrcamentoRepository orcamentoRepository) {
        this.orcamentoRepository = orcamentoRepository;
    }

    @Transactional
    public Orcamento salvar(Orcamento orcamento) {
        Optional<Orcamento> existente = orcamentoRepository.buscarPorMesAnoEUsuario(orcamento.getMes(), orcamento.getUsuario());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("O usuário já possui um orçamento para este mês.");
        }
        orcamento.setDespesas(0);
        orcamento.setReceitas(0);
        orcamento.setQtdAtingida(0);
        if (orcamento.getDataCriacao() == null) {
            orcamento.setDataCriacao(LocalDateTime.now());
        }
        orcamento.setMes(orcamento.getMes());
        return orcamentoRepository.save(orcamento);
    }

    public List<Orcamento> listarTodos() {
        return orcamentoRepository.findAll();
    }

    public Optional<Orcamento> buscarPorId(UUID id) {
        return orcamentoRepository.findById(id);
    }

    public List<Orcamento> buscarPorUsuario(Usuario usuario) {
        return orcamentoRepository.findByUsuario(usuario);
    }

    public Optional<Orcamento> buscarPorMesAnoEUsuario(String mesAno, Usuario usuario) {
        return orcamentoRepository.buscarPorMesAnoEUsuario(mesAno, usuario);
    }

    @Transactional
    public void deletar(UUID id) {
        orcamentoRepository.deleteById(id);
    }

    @Transactional
    public Orcamento atualizar(UUID id, Orcamento orcamentoAtualizado) {
        return orcamentoRepository.findById(id)
                .map(orcamento -> {
                    orcamento.setMes(orcamentoAtualizado.getMes());
                    orcamento.setSaldo(orcamentoAtualizado.getSaldo());
                    orcamento.setReceitas(orcamentoAtualizado.getReceitas());
                    orcamento.setDespesas(orcamentoAtualizado.getDespesas());
                    orcamento.setMetaGastos(orcamentoAtualizado.getMetaGastos());
                    orcamento.setQtdAtingida(orcamentoAtualizado.getQtdAtingida());
                    return orcamentoRepository.save(orcamento);
                })
                .orElseThrow(() -> new RuntimeException("Orçamento não encontrado"));
    }


}
