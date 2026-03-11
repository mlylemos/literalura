package com.alura.literalura.model;

/**
 * Enum que representa os idiomas suportados pela aplicação.
 * Cada constante contém o código ISO 639-1 usado pela API Gutendex
 * e o nome legível em português.
 */
public enum Idioma {

    PORTUGUES("pt", "Português"),
    INGLES("en", "Inglês"),
    ESPANHOL("es", "Espanhol"),
    FRANCES("fr", "Francês"),
    ALEMAO("de", "Alemão"),
    ITALIANO("it", "Italiano"),
    LATIN("la", "Latim"),
    CHINES("zh", "Chinês"),
    JAPONES("ja", "Japonês"),
    RUSSO("ru", "Russo");

    private final String codigoIso;
    private final String nomeExibicao;

    Idioma(String codigoIso, String nomeExibicao) {
        this.codigoIso = codigoIso;
        this.nomeExibicao = nomeExibicao;
    }

    public String getCodigoIso() { return codigoIso; }
    public String getNomeExibicao() { return nomeExibicao; }

    /**
     * Busca um Idioma pelo código ISO ou pelo nome (case-insensitive).
     *
     * @param texto código ISO (ex.: "en") ou nome (ex.: "inglês")
     * @return o Idioma correspondente ou null se não encontrado
     */
    public static Idioma fromTexto(String texto) {
        if (texto == null || texto.isBlank()) return null;
        String normalizado = texto.trim().toLowerCase();
        for (Idioma idioma : values()) {
            if (idioma.codigoIso.equalsIgnoreCase(normalizado)
                    || idioma.nomeExibicao.equalsIgnoreCase(normalizado)) {
                return idioma;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return nomeExibicao + " (" + codigoIso + ")";
    }
}
