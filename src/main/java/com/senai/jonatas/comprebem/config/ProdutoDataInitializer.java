package com.senai.jonatas.comprebem.config;

import com.senai.jonatas.comprebem.entity.Produto;
import com.senai.jonatas.comprebem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ProdutoDataInitializer implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    @Override
    public void run(String... args) {
        // Completa o catálogo até 60 produtos sem duplicar dados existentes.
        int quantidadeAtual = (int) produtoRepository.count();
        if (quantidadeAtual >= 60) {
            return;
        }

        List<Produto> produtos = new ArrayList<>();
        for (int index = quantidadeAtual + 1; index <= 60; index++) {
            Produto produto = new Produto();
            produto.setNome(index <= 10 ? "Cadeira de Escritório " + index : "Produto Compre Bem " + index);
            produto.setDescricao("Produto de qualidade para sua casa");
            produto.setPreco(BigDecimal.valueOf(49.90 + index));
            produto.setCategoria(index <= 10 ? "Móveis" : "Casa");
            produto.setImagem("https://example.com/produto-" + index + ".jpg");
            produto.setQuantidade(10);
            produtos.add(produto);
        }

        produtoRepository.saveAll(produtos);
    }
}