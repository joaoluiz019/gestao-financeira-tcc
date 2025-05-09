package com.app.gestaofinanceira.controller;

import com.app.gestaofinanceira.config.JwtUtil;
import com.app.gestaofinanceira.model.Orcamento;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import com.app.gestaofinanceira.service.OrcamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orcamentos")
public class OrcamentoController {

    @Autowired
    private final OrcamentoService orcamentoService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public OrcamentoController(OrcamentoService orcamentoService) {
        this.orcamentoService = orcamentoService;
    }

    @PostMapping
    public ResponseEntity<Orcamento> criar(@RequestBody Orcamento orcamento, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.obterDadosDoToken(jwtToken);
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        orcamento.setUsuario(usuario);
        orcamentoService.salvar(orcamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(orcamento);
    }

    @GetMapping
    public ResponseEntity<List<Orcamento>> listarTodos() {
        List<Orcamento> orcamentos = orcamentoService.listarTodos();
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orcamento> buscarPorId(@PathVariable UUID id) {
        return orcamentoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Orcamento>> buscarPorUsuario(@PathVariable Usuario usuario) {
        List<Orcamento> orcamentos = orcamentoService.buscarPorUsuario(usuario);
        return ResponseEntity.ok(orcamentos);
    }

    @GetMapping("/mes/{mesAno}/usuario")
    public ResponseEntity<Orcamento> buscarPorMesAnoEUsuario(@PathVariable String mesAno, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.obterDadosDoToken(jwtToken);
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return orcamentoService.buscarPorMesAnoEUsuario(mesAno, usuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Orcamento> atualizar(
            @PathVariable UUID id,
            @RequestBody Orcamento orcamentoAtualizado) {
        Orcamento orcamento = orcamentoService.atualizar(id, orcamentoAtualizado);
        return ResponseEntity.ok(orcamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        orcamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/atual")
    public List<Orcamento> buscarPorUsuarioAtual(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.obterDadosDoToken(jwtToken);
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return orcamentoService.buscarPorUsuario(usuario);
    }
}

