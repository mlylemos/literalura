package com.alura.literalura;

import com.alura.literalura.dto.DadosLivroDTO;
import com.alura.literalura.dto.RespostaGutendexDTO;
import com.alura.literalura.service.ConversorJsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o serviço de conversão JSON.
 */
class ConversorJsonServiceTest {

    private ConversorJsonService conversor;

    // JSON simulando uma resposta da API Gutendex
    private static final String JSON_VALIDO = """
            {
              "count": 1,
              "results": [
                {
                  "id": 1342,
                  "title": "Pride and Prejudice",
                  "authors": [
                    {
                      "name": "Austen, Jane",
                      "birth_year": 1775,
                      "death_year": 1817
                    }
                  ],
                  "languages": ["en"],
                  "download_count": 7012345
                }
              ]
            }
            """;

    @BeforeEach
    void setUp() {
        conversor = new ConversorJsonService();
    }

    @Test
    @DisplayName("Deve converter JSON válido para RespostaGutendexDTO corretamente")
    void deveConverterJsonParaRespostaGutendex() {
        RespostaGutendexDTO resposta = conversor.converter(JSON_VALIDO, RespostaGutendexDTO.class);

        assertNotNull(resposta);
        assertEquals(1, resposta.total());
        assertFalse(resposta.livros().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear corretamente os campos do livro")
    void deveMapearCamposDoLivro() {
        RespostaGutendexDTO resposta = conversor.converter(JSON_VALIDO, RespostaGutendexDTO.class);
        DadosLivroDTO livro = resposta.livros().get(0);

        assertEquals("Pride and Prejudice", livro.titulo());
        assertEquals(7012345, livro.numeroDownloads());
        assertFalse(livro.idiomas().isEmpty());
        assertEquals("en", livro.idiomas().get(0));
    }

    @Test
    @DisplayName("Deve mapear corretamente os dados do autor")
    void deveMapearDadosDoAutor() {
        RespostaGutendexDTO resposta = conversor.converter(JSON_VALIDO, RespostaGutendexDTO.class);
        var autor = resposta.livros().get(0).autores().get(0);

        assertEquals("Austen, Jane", autor.nome());
        assertEquals(1775, autor.anoNascimento());
        assertEquals(1817, autor.anoFalecimento());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException para JSON inválido")
    void deveLancarExcecaoParaJsonInvalido() {
        assertThrows(RuntimeException.class,
                () -> conversor.converter("{ json inválido }", RespostaGutendexDTO.class));
    }
}
