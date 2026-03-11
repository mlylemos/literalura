package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Autor.
 * Fornece operações CRUD e consultas customizadas via JPQL e derived queries.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    /**
     * Busca um autor pelo nome exato (case-insensitive).
     */
    Optional<Autor> findByNomeIgnoreCase(String nome);

    /**
     * Lista autores cujo nome contenha o trecho informado (case-insensitive).
     */
    List<Autor> findByNomeContainingIgnoreCase(String nome);

    /**
     * Retorna todos os autores que estavam vivos em um determinado ano.
     *
     * A lógica é:
     *   - Nasceram antes ou no ano informado (anoNascimento <= ano)
     *   - E ou não possuem ano de falecimento (ainda vivos) OU morreram após o ano (anoFalecimento >= ano)
     *
     * @param ano o ano a ser consultado
     * @return lista de autores vivos no ano fornecido
     */
    @Query("""
            SELECT a FROM Autor a
            WHERE a.anoNascimento <= :ano
              AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)
            ORDER BY a.nome
            """)
    List<Autor> findAutoresVivosNoAno(@Param("ano") int ano);

    /**
     * Retorna todos os autores ordenados pelo nome.
     */
    List<Autor> findAllByOrderByNomeAsc();
}
