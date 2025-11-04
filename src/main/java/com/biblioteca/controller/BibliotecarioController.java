package com.biblioteca.controller;

import com.biblioteca.domain.Bibliotecario;
import com.biblioteca.service.BibliotecarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bibliotecarios")
public class BibliotecarioController {

    @Autowired
    private BibliotecarioService service;

    @GetMapping
    public ResponseEntity<List<Bibliotecario>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bibliotecario> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Bibliotecario> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.buscarPorCpf(cpf));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Bibliotecario>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Bibliotecario>> buscarPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarPorNome(nome));
    }

    @GetMapping("/salario")
    public ResponseEntity<List<Bibliotecario>> buscarPorFaixaSalarial(
            @RequestParam Double min, @RequestParam Double max) {
        return ResponseEntity.ok(service.buscarPorFaixaSalarial(min, max));
    }

    @PostMapping
    public ResponseEntity<Bibliotecario> incluir(@Valid @RequestBody Bibliotecario bibliotecario) {
        Bibliotecario novo = service.incluir(bibliotecario);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bibliotecario> alterar(
            @PathVariable Long id,
            @Valid @RequestBody Bibliotecario bibliotecario) {
        return ResponseEntity.ok(service.alterar(id, bibliotecario));
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Bibliotecario> inativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.inativar(id));
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Bibliotecario> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(service.ativar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}