package com.alura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

/**
 * Serviço utilitário para conversão de JSON em objetos Java,
 * utilizando a biblioteca Jackson (ObjectMapper).
 */
@Service
public class ConversorJsonService {

    private final ObjectMapper objectMapper;

    public ConversorJsonService() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Converte uma String JSON para um objeto do tipo especificado.
     *
     * @param json  String contendo o JSON a ser convertido
     * @param clazz Classe alvo da conversão
     * @param <T>   Tipo genérico do objeto de retorno
     * @return objeto Java preenchido com os dados do JSON
     * @throws RuntimeException se o JSON for inválido ou incompatível com a classe
     */
    public <T> T converter(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter JSON para " + clazz.getSimpleName()
                    + ": " + e.getMessage(), e);
        }
    }
}
