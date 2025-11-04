package com.biblioteca.controller;

import com.biblioteca.domain.Emprestimo;
import com.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoService service;

    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/leitor/{leitorId}")
    public ResponseEntity<List<Emprestimo>> listarPorLeitor(@PathVariable Long leitorId) {
        return ResponseEntity.ok(service.listarPorLeitor(leitorId));
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Emprestimo>> listarAtivos() {
        return ResponseEntity.ok(service.listarAtivos());
    }

    @GetMapping("/atrasados")
    public ResponseEntity<List<Emprestimo>> listarAtrasados() {
        return ResponseEntity.ok(service.listarAtrasados());
    }

    @PostMapping
    public ResponseEntity<Emprestimo> incluir(@Valid @RequestBody Emprestimo emprestimo) {
        Emprestimo novo = service.incluir(emprestimo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Emprestimo> alterar(
            @PathVariable Long id,
            @Valid @RequestBody Emprestimo emprestimo) {
        return ResponseEntity.ok(service.alterar(id, emprestimo));
    }

    @PatchMapping("/{id}/devolver")
    public ResponseEntity<Emprestimo> registrarDevolucao(@PathVariable Long id) {
        return ResponseEntity.ok(service.registrarDevolucao(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}