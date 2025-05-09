package com.app.gestaofinanceira.controller;

import com.app.gestaofinanceira.config.JwtUtil;
import com.app.gestaofinanceira.model.Receita;
import com.app.gestaofinanceira.model.Usuario;
import com.app.gestaofinanceira.repository.UsuarioRepository;
import com.app.gestaofinanceira.service.ReceitaService;
import com.app.gestaofinanceira.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    @Autowired
    private ReceitaService receitaService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Receita> listarTodas() {
        return receitaService.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Receita> buscarPorId(@PathVariable UUID id) {
        Optional<Receita> receita = receitaService.buscarPorId(id);
        return receita.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/registrar")
    public Receita salvar(@RequestBody Receita receita, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtUtil.obterDadosDoToken(jwtToken);
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        return receitaService.salvar(receita, usuario.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable UUID id) {
        receitaService.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
