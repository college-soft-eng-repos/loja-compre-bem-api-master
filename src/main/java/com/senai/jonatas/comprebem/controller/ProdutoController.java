package com.senai.jonatas.comprebem.controller;

import com.senai.jonatas.comprebem.dto.ProdutoRequestDTO;
import com.senai.jonatas.comprebem.dto.ProdutoResponseDTO;
import com.senai.jonatas.comprebem.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "api/v1/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    // Usa página zero e 20 itens por padrão.
    public Page<ProdutoResponseDTO> findAll(
            @RequestParam(defaultValue = "") String busca,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Impede que uma requisição retorne mais de 50 produtos.
        int tamanho = Math.min(size, 50);
        return produtoService.findAll(busca, PageRequest.of(page, tamanho));
    }

    @GetMapping("/{id}")
    public ProdutoResponseDTO findById(@PathVariable UUID id) {
        return produtoService.findById(id);
    }

    @GetMapping("/filtro")
    public List<ProdutoResponseDTO> findByFiltro(String filtro) {
        return produtoService.findByFiltro(filtro);
    }

    @PostMapping
    public ProdutoResponseDTO save(@Valid @RequestBody ProdutoRequestDTO produto) {
        return produtoService.save(produto);
    }

    @PutMapping("/{produtoId}")
    public ProdutoResponseDTO update(@PathVariable UUID produtoId, @Valid @RequestBody ProdutoRequestDTO produto) {
        return produtoService.update(produtoId, produto);
    }

    @DeleteMapping
    public void delete(UUID produtoId) {
        produtoService.delete(produtoId);
    }
}
