package com.alura.literalura.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Autor no banco de dados.
 * Possui relacionamento One-to-Many com Livro.
 */
@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "ano_nascimento")
    private Integer anoNascimento;

    @Column(name = "ano_falecimento")
    private Integer anoFalecimento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Livro> livros = new ArrayList<>();

    // ─── Construtores ───────────────────────────────────────────────────────────

    public Autor() {}

    public Autor(String nome, Integer anoNascimento, Integer anoFalecimento) {
        this.nome = nome;
        this.anoNascimento = anoNascimento;
        this.anoFalecimento = anoFalecimento;
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getAnoNascimento() { return anoNascimento; }
    public void setAnoNascimento(Integer anoNascimento) { this.anoNascimento = anoNascimento; }

    public Integer getAnoFalecimento() { return anoFalecimento; }
    public void setAnoFalecimento(Integer anoFalecimento) { this.anoFalecimento = anoFalecimento; }

    public List<Livro> getLivros() { return livros; }
    public void setLivros(List<Livro> livros) { this.livros = livros; }

    // ─── toString ────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        String nascimento = anoNascimento != null ? String.valueOf(anoNascimento) : "desconhecido";
        String falecimento = anoFalecimento != null ? String.valueOf(anoFalecimento) : "ainda vivo";
        return String.format("%-35s (nascimento: %s | falecimento: %s)",
                nome, nascimento, falecimento);
    }
}
