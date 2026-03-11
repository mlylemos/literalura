package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para a entidade Livro.
 * Fornece operações CRUD e consultas customizadas via JPQL e derived queries.
 */
@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    /**
     * Busca um livro pelo título exato (case-insensitive).
     */
    Optional<Livro> findByTituloIgnoreCase(String titulo);

    /**
     * Lista livros filtrando pelo idioma.
     */
    List<Livro> findByIdioma(Idioma idioma);

    /**
     * Conta quantos livros existem em determinado idioma.
     */
    long countByIdioma(Idioma idioma);

    /**
     * Retorna todos os livros ordenados pelo número de downloads (decrescente).
     * Útil para o top 10 mais baixados.
     */
    List<Livro> findAllByOrderByNumeroDownloadsDesc();

    /**
     * Top 10 livros mais baixados.
     */
    @Query("SELECT l FROM Livro l ORDER BY l.numeroDownloads DESC LIMIT 10")
    List<Livro> findTop10ByOrderByNumeroDownloadsDesc();

    /**
     * Retorna todos os livros com dados do autor carregados (FETCH JOIN).
     */
    @Query("SELECT l FROM Livro l JOIN FETCH l.autor ORDER BY l.titulo")
    List<Livro> findAllComAutor();
}
