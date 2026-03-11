package com.alura.literalura.service;

import com.alura.literalura.dto.DadosAutorDTO;
import com.alura.literalura.dto.DadosLivroDTO;
import com.alura.literalura.dto.RespostaGutendexDTO;
import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Livro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Serviço central da aplicação.
 * Coordena o consumo da API Gutendex, conversão do JSON e persistência no banco de dados.
 */
@Service
public class LivroService {

    private final ConsumoApiService apiService;
    private final ConversorJsonService conversorJson;
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;

    public LivroService(ConsumoApiService apiService,
                        ConversorJsonService conversorJson,
                        LivroRepository livroRepository,
                        AutorRepository autorRepository) {
        this.apiService = apiService;
        this.conversorJson = conversorJson;
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 1 — Buscar livro por título na API e salvar no banco
    // ═══════════════════════════════════════════════════════════════════════════

    /**
     * Busca um livro pelo título na API Gutendex.
     * Se o livro já estiver no banco, retorna o existente sem duplicar.
     * Caso contrário, persiste o livro e seu autor.
     *
     * @param titulo título (ou parte dele) informado pelo usuário
     * @return Optional contendo o Livro encontrado/salvo, ou vazio se não encontrado
     */
    @Transactional
    public Optional<Livro> buscarESalvarLivro(String titulo) {
        String json = apiService.buscarPorTitulo(titulo);
        RespostaGutendexDTO resposta = conversorJson.converter(json, RespostaGutendexDTO.class);

        if (resposta.livros() == null || resposta.livros().isEmpty()) {
            return Optional.empty();
        }

        DadosLivroDTO dadosLivro = resposta.livros().get(0);

        // Verifica se o livro já existe no banco pelo título
        Optional<Livro> livroExistente = livroRepository.findByTituloIgnoreCase(dadosLivro.titulo());
        if (livroExistente.isPresent()) {
            return livroExistente;
        }

        return Optional.of(converterESalvar(dadosLivro));
    }

    /**
     * Converte um DTO em entidades e persiste no banco.
     * Reutiliza o autor se ele já existir (pelo nome), evitando duplicatas.
     */
    private Livro converterESalvar(DadosLivroDTO dto) {
        // Determina o idioma (pega o primeiro da lista)
        Idioma idioma = null;
        if (dto.idiomas() != null && !dto.idiomas().isEmpty()) {
            idioma = Idioma.fromTexto(dto.idiomas().get(0));
        }

        // Resolve autor
        Autor autor = resolverAutor(dto);

        Livro livro = new Livro(dto.titulo(), idioma, dto.numeroDownloads(), autor);
        return livroRepository.save(livro);
    }

    /**
     * Busca o autor no banco pelo nome ou cria um novo a partir do DTO.
     */
    private Autor resolverAutor(DadosLivroDTO dto) {
        if (dto.autores() == null || dto.autores().isEmpty()) {
            return null;
        }

        DadosAutorDTO dadosAutor = dto.autores().get(0);

        return autorRepository.findByNomeIgnoreCase(dadosAutor.nome())
                .orElseGet(() -> {
                    Autor novoAutor = new Autor(
                            dadosAutor.nome(),
                            dadosAutor.anoNascimento(),
                            dadosAutor.anoFalecimento()
                    );
                    return autorRepository.save(novoAutor);
                });
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 2 — Listar todos os livros cadastrados
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Livro> listarTodosLivros() {
        return livroRepository.findAllComAutor();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 3 — Listar todos os autores cadastrados
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Autor> listarTodosAutores() {
        return autorRepository.findAllByOrderByNomeAsc();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 4 — Listar autores vivos em determinado ano
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Autor> listarAutoresVivosNoAno(int ano) {
        return autorRepository.findAutoresVivosNoAno(ano);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 5 — Listar livros por idioma
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Livro> listarLivrosPorIdioma(Idioma idioma) {
        return livroRepository.findByIdioma(idioma);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 6 — Estatísticas de downloads (DoubleSummaryStatistics)
    // ═══════════════════════════════════════════════════════════════════════════

    public DoubleSummaryStatistics obterEstatisticasDownloads() {
        return livroRepository.findAll()
                .stream()
                .filter(l -> l.getNumeroDownloads() != null)
                .mapToDouble(Livro::getNumeroDownloads)
                .summaryStatistics();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 7 — Top 10 livros mais baixados
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Livro> listarTop10MaisBaixados() {
        return livroRepository.findTop10ByOrderByNumeroDownloadsDesc();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 8 — Buscar autor por nome (no banco de dados)
    // ═══════════════════════════════════════════════════════════════════════════

    public List<Autor> buscarAutorPorNome(String nome) {
        return autorRepository.findByNomeContainingIgnoreCase(nome);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 9 — Contagem de livros por idioma
    // ═══════════════════════════════════════════════════════════════════════════

    public Map<Idioma, Long> contarLivrosPorIdioma() {
        return Arrays.stream(Idioma.values())
                .collect(Collectors.toMap(
                        idioma -> idioma,
                        livroRepository::countByIdioma,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
