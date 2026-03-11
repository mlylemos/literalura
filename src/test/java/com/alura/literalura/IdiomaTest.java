package com.alura.literalura;

import com.alura.literalura.model.Idioma;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o enum Idioma.
 */
class IdiomaTest {

    @Test
    @DisplayName("Deve encontrar idioma por código ISO")
    void deveEncontrarIdiomaPorCodigoIso() {
        assertEquals(Idioma.INGLES, Idioma.fromTexto("en"));
        assertEquals(Idioma.PORTUGUES, Idioma.fromTexto("pt"));
        assertEquals(Idioma.ESPANHOL, Idioma.fromTexto("es"));
    }

    @Test
    @DisplayName("Deve encontrar idioma por nome (case-insensitive)")
    void deveEncontrarIdiomaPorNome() {
        assertEquals(Idioma.INGLES, Idioma.fromTexto("Inglês"));
        assertEquals(Idioma.PORTUGUES, Idioma.fromTexto("português"));
        assertEquals(Idioma.FRANCES, Idioma.fromTexto("FRANCÊS"));
    }

    @Test
    @DisplayName("Deve retornar null para idioma desconhecido")
    void deveRetornarNullParaIdiomaDesconhecido() {
        assertNull(Idioma.fromTexto("xyz"));
        assertNull(Idioma.fromTexto(null));
        assertNull(Idioma.fromTexto(""));
    }
}
