package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO raiz da resposta paginada da API Gutendex.
 * Exemplo: GET https://gutendex.com/books/?search=hamlet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record RespostaGutendexDTO(
        @JsonAlias("count")   int total,
        @JsonAlias("results") List<DadosLivroDTO> livros
) {}
