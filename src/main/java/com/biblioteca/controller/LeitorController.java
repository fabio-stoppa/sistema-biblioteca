package com.biblioteca.controller;

import com.biblioteca.domain.Leitor;
import com.biblioteca.service.LeitorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/leitores")
public class LeitorController {

    @Autowired
    private LeitorService service;

    @GetMapping
    public ResponseEntity<List<Leitor>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leitor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Leitor> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }

    @GetMapping("/fidelidade/{fidelidade}")
    public ResponseEntity<List<Leitor>> buscarPorFidelidade(@PathVariable String fidelidade) {
        return ResponseEntity.ok(service.buscarPorFidelidade(fidelidade));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Leitor>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<Leitor> incluir(@Valid @RequestBody Leitor leitor) {
        Leitor novo = service.incluir(leitor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Leitor> alterar(
            @PathVariable Long id,
            @Valid @RequestBody Leitor leitor) {
        return ResponseEntity.ok(service.alterar(id, leitor));
    }

    @PatchMapping("/{id}/fidelidade")
    public ResponseEntity<Leitor> atualizarFidelidade(
            @PathVariable Long id,
            @RequestParam String novaFidelidade) {
        return ResponseEntity.ok(service.atualizarFidelidade(id, novaFidelidade));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}