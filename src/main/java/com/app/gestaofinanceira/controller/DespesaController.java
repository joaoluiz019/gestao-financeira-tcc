package com.app.gestaofinanceira.controller;

import com.app.gestaofinanceira.config.JwtUtil;
import com.app.gestaofinanceira.model.Despesa;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import com.app.gestaofinanceira.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/despesas")
public class DespesaController {

    @Autowired
    private DespesaService despesaService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Despesa> listarTodas() {
        return despesaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable UUID id) {
        Optional<Despesa> despesa = despesaService.buscarPorId(id);
        return despesa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public Despesa salvar(@RequestBody Despesa despesa, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.obterDadosDoToken(jwtToken);
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return despesaService.salvar(despesa, usuario.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        despesaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
