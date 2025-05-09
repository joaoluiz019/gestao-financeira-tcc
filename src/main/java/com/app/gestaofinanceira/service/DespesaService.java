package com.app.gestaofinanceira.service;

import com.app.gestaofinanceira.model.Despesa;
import com.app.gestaofinanceira.model.Orcamento;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.DespesaRepository;
import com.app.gestaofinanceira.repository.OrcamentoRepository;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class DespesaService {

    @Autowired
    private DespesaRepository despesaRepository;
    @Autowired
    private OrcamentoRepository orcamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Despesa> listarTodas() {
        return despesaRepository.findAll();
    }

    public Optional<Despesa> buscarPorId(UUID id) {
        return despesaRepository.findById(id);
    }

    public Despesa salvar(Despesa despesa, UUID idUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findById(idUsuario);

        if (usuario.isPresent()) {
            despesa.setDataCriacao(LocalDateTime.now());

            String mes = despesa.getMes();

            Optional<Orcamento> optionalOrcamento = orcamentoRepository.buscarPorMesAnoEUsuario(mes, usuario.get());

            if (optionalOrcamento.isPresent()) {
                Orcamento orcamento = optionalOrcamento.get();
                float despesaAtualizada;
                float saldoAtualizado;
                float qtdAtingidaAtualizada;

                despesaAtualizada = orcamento.getDespesas() + despesa.getValor();

                if(orcamento.getSaldo() <= despesa.getValor()){
                    saldoAtualizado = despesa.getValor() - orcamento.getSaldo();
                }else{
                    saldoAtualizado = orcamento.getSaldo() - despesa.getValor();
                }

                qtdAtingidaAtualizada =  orcamento.getQtdAtingida() + despesa.getValor();

                orcamento.setDespesas(despesaAtualizada);
                orcamento.setSaldo(saldoAtualizado);
                orcamento.setQtdAtingida(qtdAtingidaAtualizada);
                orcamentoRepository.save(orcamento);
                despesa.setOrcamento(orcamento);
            } else {
                throw new IllegalArgumentException("Não existe um orçamento para este mês e usuário.");
            }
        } else {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return despesaRepository.save(despesa);
    }

    public void deletarPorId(UUID id) {
        despesaRepository.deleteById(id);
    }
}
