package com.alura.literalura.model;

import jakarta.persistence.*;

/**
 * Entidade que representa um Livro no banco de dados.
 * Possui relacionamento Many-to-One com Autor.
 */
@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Idioma idioma;

    @Column(name = "numero_downloads")
    private Integer numeroDownloads;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    // ─── Construtores ───────────────────────────────────────────────────────────

    public Livro() {}

    public Livro(String titulo, Idioma idioma, Integer numeroDownloads, Autor autor) {
        this.titulo = titulo;
        this.idioma = idioma;
        this.numeroDownloads = numeroDownloads;
        this.autor = autor;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public Idioma getIdioma() { return idioma; }
    public void setIdioma(Idioma idioma) { this.idioma = idioma; }

    public Integer getNumeroDownloads() { return numeroDownloads; }
    public void setNumeroDownloads(Integer numeroDownloads) { this.numeroDownloads = numeroDownloads; }

    public Autor getAutor() { return autor; }
    public void setAutor(Autor autor) { this.autor = autor; }

    // ─── toString ────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        String nomeAutor = autor != null ? autor.getNome() : "Autor desconhecido";
        String downloads = numeroDownloads != null
                ? String.format("%,d", numeroDownloads)
                : "N/A";
        return String.format(
                "┌─ Livro ──────────────────────────────────────────────┐%n" +
                "│ Título:     %-40s │%n" +
                "│ Autor:      %-40s │%n" +
                "│ Idioma:     %-40s │%n" +
                "│ Downloads:  %-40s │%n" +
                "└──────────────────────────────────────────────────────┘",
                truncate(titulo, 40),
                truncate(nomeAutor, 40),
                idioma != null ? idioma.toString() : "N/A",
                downloads
        );
    }

    private String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() > max ? s.substring(0, max - 3) + "..." : s;
    }
}
