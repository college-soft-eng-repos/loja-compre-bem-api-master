package com.senai.jonatas.comprebem.service;

import com.senai.jonatas.comprebem.dto.ProdutoRequestDTO;
import com.senai.jonatas.comprebem.dto.ProdutoResponseDTO;
import com.senai.jonatas.comprebem.entity.Produto;
import com.senai.jonatas.comprebem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Page<ProdutoResponseDTO> findAll(String busca, Pageable pageable) {
        // Sem busca, consulta todos; com busca, filtra pelo nome.
        Page<Produto> produtos = busca.isBlank()
                ? produtoRepository.findAll(pageable)
                : produtoRepository.findByNomeContainingIgnoreCase(busca, pageable);

        return produtos.map(ProdutoResponseDTO::toDto);
    }

    public ProdutoResponseDTO findById(UUID id) {
        var produto = produtoRepository.findById(id)
            // Produto inexistente deve resultar em HTTP 404.
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto não encontrado"));

        return ProdutoResponseDTO.toDto(produto);
    }

    public List<ProdutoResponseDTO> findByFiltro(String filtro) {
        return produtoRepository.findByFiltro(filtro)
                .stream()
                .map(ProdutoResponseDTO::toDto)
                .toList();
    }

    public ProdutoResponseDTO save(ProdutoRequestDTO produto) {
        Produto produtoSalvo = produtoRepository.save(Produto.toEntity(produto));
        return ProdutoResponseDTO.toDto(produtoSalvo);
    }

    public void delete(UUID produtoId) {
        produtoRepository.deleteById(produtoId);
    }

    public ProdutoResponseDTO update(UUID produtoId, ProdutoRequestDTO produto) {
        Produto produtoExistente = produtoRepository.findById(produtoId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Produto não encontrado"));

        produtoExistente.setNome(produto.nome());
        produtoExistente.setDescricao(produto.descricao());
        produtoExistente.setPreco(produto.preco());
        produtoExistente.setCategoria(produto.categoria());
        produtoExistente.setImagem(produto.imagem());

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return ProdutoResponseDTO.toDto(produtoAtualizado);

    }
}
