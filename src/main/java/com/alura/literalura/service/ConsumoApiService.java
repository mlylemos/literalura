package com.alura.literalura.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * Serviço responsável por realizar requisições HTTP à API Gutendex.
 *
 * Utiliza a API nativa do Java 11+ (java.net.http.HttpClient),
 * sem dependências de bibliotecas externas como RestTemplate ou OkHttp.
 */
@Service
public class ConsumoApiService {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final int TIMEOUT_SEGUNDOS = 15;

    private final HttpClient httpClient;

    public ConsumoApiService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    /**
     * Busca livros na API Gutendex pelo título informado.
     *
     * @param titulo título (ou parte dele) do livro desejado
     * @return corpo da resposta JSON como String
     * @throws RuntimeException em caso de falha na requisição ou resposta inválida
     */
    public String buscarPorTitulo(String titulo) {
        String tituloEncoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = URL_BASE + "?search=" + tituloEncoded;
        return realizarRequisicao(url);
    }

    /**
     * Realiza uma requisição GET genérica para a URL informada.
     *
     * @param url URL completa para a requisição
     * @return corpo da resposta como String
     */
    public String realizarRequisicao(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(TIMEOUT_SEGUNDOS))
                .header("Accept", "application/json")
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException(
                        "Falha na requisição. Status HTTP: " + response.statusCode());
            }

            return response.body();

        } catch (IOException e) {
            throw new RuntimeException("Erro de I/O ao conectar com a API: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Requisição interrompida: " + e.getMessage(), e);
        }
    }
}
