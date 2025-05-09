package com.app.gestaofinanceira.service;

import com.app.gestaofinanceira.model.Orcamento;
import com.app.gestaofinanceira.model.Receita;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.OrcamentoRepository;
import com.app.gestaofinanceira.repository.ReceitaRepository;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ReceitaService {

    @Autowired
    private ReceitaRepository receitaRepository;
    @Autowired
    private OrcamentoRepository orcamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Receita> listarTodas() {
        return receitaRepository.findAll();
    }

    public Optional<Receita> buscarPorId(UUID id) {
        return receitaRepository.findById(id);
    }

    public Receita salvar(Receita receita, UUID idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

        if (usuario.isPresent()) {
            receita.setDataCriacao(LocalDateTime.now());

            String mes = receita.getMes();

            Optional<Orcamento> optionalOrcamento = orcamentoRepository.buscarPorMesAnoEUsuario(mes, usuario.get());

            if (optionalOrcamento.isPresent()) {
                Orcamento orcamento = optionalOrcamento.get();
                float receitaAtualizada = orcamento.getReceitas() + receita.getValor();
                float saldoAtualizado = receita.getValor() + orcamento.getSaldo();
                orcamento.setReceitas(receitaAtualizada);
                orcamento.setSaldo(saldoAtualizado);
                orcamentoRepository.save(orcamento);
                receita.setOrcamento(orcamento);
            } else {
                throw new IllegalArgumentException("Não existe um orçamento para este mês e usuário.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }

        return receitaRepository.save(receita);
    }

    public void deletarPorId(UUID id) {
        receitaRepository.deleteById(id);
    }
}
