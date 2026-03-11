package com.alura.literalura;

import com.alura.literalura.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação LiterAlura.
 *
 * Implementa {@link CommandLineRunner} para executar o menu interativo
 * logo após o contexto do Spring ser inicializado.
 */
@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final Principal principal;

    public LiteraluraApplication(Principal principal) {
        this.principal = principal;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        principal.exibirMenu();
    }
}
