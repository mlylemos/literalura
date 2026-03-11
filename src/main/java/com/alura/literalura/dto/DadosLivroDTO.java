package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

/**
 * DTO que representa os dados de um livro retornado pela API Gutendex.
 *
 * Campos do JSON mapeados:
 *  - "title"            → título do livro
 *  - "authors"          → lista de autores
 *  - "languages"        → lista de códigos de idioma ISO 639-1
 *  - "download_count"   → número de downloads
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivroDTO(
        @JsonAlias("title")          String titulo,
        @JsonAlias("authors")        List<DadosAutorDTO> autores,
        @JsonAlias("languages")      List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDownloads
) {}
