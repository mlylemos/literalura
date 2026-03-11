package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO que representa os dados de um autor retornado pela API Gutendex.
 *
 * Campos do JSON mapeados:
 *  - "name"       → nome completo do autor
 *  - "birth_year" → ano de nascimento
 *  - "death_year" → ano de falecimento
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosAutorDTO(
        @JsonAlias("name")       String nome,
        @JsonAlias("birth_year") Integer anoNascimento,
        @JsonAlias("death_year") Integer anoFalecimento
) {}
