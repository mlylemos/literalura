package com.alura.literalura.principal;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Livro;
import com.alura.literalura.service.LivroService;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Classe principal de interação com o usuário via console.
 * Apresenta o menu e processa as escolhas do usuário em um loop contínuo.
 */
@Component
public class Principal {

    // ─── Cores ANSI para o terminal ──────────────────────────────────────────────
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String GREEN  = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED    = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";

    private final LivroService livroService;
    private final Scanner scanner;

    public Principal(LivroService livroService) {
        this.livroService = livroService;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Exibe o menu principal e processa as opções em loop até o usuário sair.
     */
    public void exibirMenu() {
        exibirBanner();

        int opcao = -1;
        while (opcao != 0) {
            imprimirMenu();
            opcao = lerOpcaoNumerica();

            switch (opcao) {
                case 1  -> buscarLivroPorTitulo();
                case 2  -> listarTodosLivros();
                case 3  -> listarTodosAutores();
                case 4  -> listarAutoresVivosNoAno();
                case 5  -> listarLivrosPorIdioma();
                case 6  -> exibirEstatisticasDownloads();
                case 7  -> listarTop10MaisBaixados();
                case 8  -> buscarAutorPorNome();
                case 9  -> exibirContagemPorIdioma();
                case 0  -> System.out.println(YELLOW + "\n👋 Encerrando LiterAlura. Até logo!\n" + RESET);
                default -> System.out.println(RED + "\n⚠  Opção inválida. Tente novamente.\n" + RESET);
            }
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // Menu e utilidades de I/O
    // ═══════════════════════════════════════════════════════════════════════════

    private void imprimirMenu() {
        System.out.println(CYAN + BOLD + "\n╔══════════════════════════════════╗");
        System.out.println("║         📚  L I T E R A L U R A  ║");
        System.out.println("╚══════════════════════════════════╝" + RESET);
        System.out.println(BOLD + "  Escolha uma opção:" + RESET);
        System.out.println("  1 - 🔍 Buscar livro por título");
        System.out.println("  2 - 📖 Listar todos os livros");
        System.out.println("  3 - 👤 Listar todos os autores");
        System.out.println("  4 - 🗓  Listar autores vivos em determinado ano");
        System.out.println("  5 - 🌐 Listar livros por idioma");
        System.out.println("  6 - 📊 Estatísticas de downloads");
        System.out.println("  7 - 🏆 Top 10 livros mais baixados");
        System.out.println("  8 - 🔎 Buscar autor por nome");
        System.out.println("  9 - 🗺  Contagem de livros por idioma");
        System.out.println("  0 - 🚪 Sair");
        System.out.print(CYAN + "\n▶ Opção: " + RESET);
    }

    private void exibirBanner() {
        System.out.println(PURPLE + BOLD);
        System.out.println("  ██╗     ██╗████████╗███████╗██████╗  █████╗ ██╗     ██╗   ██╗██████╗  █████╗ ");
        System.out.println("  ██║     ██║╚══██╔══╝██╔════╝██╔══██╗██╔══██╗██║     ██║   ██║██╔══██╗██╔══██╗");
        System.out.println("  ██║     ██║   ██║   █████╗  ██████╔╝███████║██║     ██║   ██║██████╔╝███████║");
        System.out.println("  ██║     ██║   ██║   ██╔══╝  ██╔══██╗██╔══██║██║     ██║   ██║██╔══██╗██╔══██║");
        System.out.println("  ███████╗██║   ██║   ███████╗██║  ██║██║  ██║███████╗╚██████╔╝██║  ██║██║  ██║");
        System.out.println("  ╚══════╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝");
        System.out.println(RESET);
        System.out.println(CYAN + "  Catálogo de Livros — Alura ONE Challenge" + RESET);
        System.out.println(CYAN + "  Powered by Gutendex API + PostgreSQL + Spring Boot\n" + RESET);
    }

    private int lerOpcaoNumerica() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void separador() {
        System.out.println(CYAN + "─".repeat(56) + RESET);
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 1 — Buscar livro por título
    // ═══════════════════════════════════════════════════════════════════════════

    private void buscarLivroPorTitulo() {
        System.out.print(BOLD + "\n🔍 Digite o título do livro: " + RESET);
        String titulo = scanner.nextLine().trim();

        if (titulo.isBlank()) {
            System.out.println(RED + "⚠  Título não pode ser vazio." + RESET);
            return;
        }

        System.out.println(YELLOW + "🌐 Consultando a API Gutendex..." + RESET);
        try {
            Optional<Livro> resultado = livroService.buscarESalvarLivro(titulo);
            if (resultado.isPresent()) {
                System.out.println(GREEN + "\n✅ Livro encontrado e salvo no catálogo:\n" + RESET);
                System.out.println(resultado.get());
            } else {
                System.out.println(RED + "\n❌ Nenhum livro encontrado para: \"" + titulo + "\"" + RESET);
            }
        } catch (RuntimeException e) {
            System.out.println(RED + "\n❌ Erro ao acessar a API: " + e.getMessage() + RESET);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 2 — Listar todos os livros
    // ═══════════════════════════════════════════════════════════════════════════

    private void listarTodosLivros() {
        List<Livro> livros = livroService.listarTodosLivros();
        separador();
        System.out.println(BOLD + "\n📖 Livros no catálogo (" + livros.size() + " registrado(s)):\n" + RESET);

        if (livros.isEmpty()) {
            System.out.println("  Nenhum livro cadastrado ainda. Use a opção 1 para buscar.");
        } else {
            livros.forEach(l -> System.out.println(l + "\n"));
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 3 — Listar todos os autores
    // ═══════════════════════════════════════════════════════════════════════════

    private void listarTodosAutores() {
        List<Autor> autores = livroService.listarTodosAutores();
        separador();
        System.out.println(BOLD + "\n👤 Autores cadastrados (" + autores.size() + "):\n" + RESET);

        if (autores.isEmpty()) {
            System.out.println("  Nenhum autor cadastrado ainda.");
        } else {
            autores.forEach(a -> System.out.println("  • " + a));
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 4 — Listar autores vivos em determinado ano
    // ═══════════════════════════════════════════════════════════════════════════

    private void listarAutoresVivosNoAno() {
        System.out.print(BOLD + "\n🗓  Digite o ano: " + RESET);
        String entrada = scanner.nextLine().trim();

        try {
            int ano = Integer.parseInt(entrada);
            if (ano < 0 || ano > 2100) {
                System.out.println(RED + "⚠  Ano inválido. Informe um valor entre 0 e 2100." + RESET);
                return;
            }

            List<Autor> autores = livroService.listarAutoresVivosNoAno(ano);
            separador();
            System.out.println(BOLD + "\n🗓  Autores vivos em " + ano + " (" + autores.size() + " encontrado(s)):\n" + RESET);

            if (autores.isEmpty()) {
                System.out.println("  Nenhum autor encontrado para o ano " + ano + ".");
            } else {
                autores.forEach(a -> System.out.println("  • " + a));
            }
            separador();

        } catch (NumberFormatException e) {
            System.out.println(RED + "⚠  Entrada inválida. Digite um número inteiro (ex.: 1850)." + RESET);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 5 — Listar livros por idioma
    // ═══════════════════════════════════════════════════════════════════════════

    private void listarLivrosPorIdioma() {
        System.out.println(BOLD + "\n🌐 Idiomas disponíveis:" + RESET);
        Arrays.stream(Idioma.values())
                .forEach(i -> System.out.printf("  %-4s → %s%n", i.getCodigoIso(), i.getNomeExibicao()));

        System.out.print(BOLD + "\nDigite o código do idioma (ex.: en, pt, es): " + RESET);
        String codigo = scanner.nextLine().trim();

        Idioma idioma = Idioma.fromTexto(codigo);
        if (idioma == null) {
            System.out.println(RED + "⚠  Idioma \"" + codigo + "\" não reconhecido." + RESET);
            return;
        }

        List<Livro> livros = livroService.listarLivrosPorIdioma(idioma);
        separador();
        System.out.println(BOLD + "\n🌐 Livros em " + idioma.getNomeExibicao()
                + " (" + livros.size() + " encontrado(s)):\n" + RESET);

        if (livros.isEmpty()) {
            System.out.println("  Nenhum livro em " + idioma.getNomeExibicao() + " no catálogo.");
        } else {
            livros.forEach(l -> System.out.println(l + "\n"));
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 6 — Estatísticas de downloads
    // ═══════════════════════════════════════════════════════════════════════════

    private void exibirEstatisticasDownloads() {
        DoubleSummaryStatistics stats = livroService.obterEstatisticasDownloads();
        separador();
        System.out.println(BOLD + "\n📊 Estatísticas de Downloads:\n" + RESET);

        if (stats.getCount() == 0) {
            System.out.println("  Sem dados disponíveis. Busque livros primeiro.");
        } else {
            System.out.printf("  📚 Total de livros analisados : %d%n", stats.getCount());
            System.out.printf("  ⬇  Total de downloads         : %,.0f%n", stats.getSum());
            System.out.printf("  📈 Máximo de downloads        : %,.0f%n", stats.getMax());
            System.out.printf("  📉 Mínimo de downloads        : %,.0f%n", stats.getMin());
            System.out.printf("  📊 Média de downloads         : %,.2f%n", stats.getAverage());
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 7 — Top 10 livros mais baixados
    // ═══════════════════════════════════════════════════════════════════════════

    private void listarTop10MaisBaixados() {
        List<Livro> top10 = livroService.listarTop10MaisBaixados();
        separador();
        System.out.println(BOLD + "\n🏆 Top 10 Livros Mais Baixados:\n" + RESET);

        if (top10.isEmpty()) {
            System.out.println("  Nenhum livro no catálogo ainda.");
        } else {
            int posicao = 1;
            for (Livro livro : top10) {
                System.out.printf("  %2d° %s — %s  (%,d downloads)%n",
                        posicao++,
                        livro.getTitulo(),
                        livro.getAutor() != null ? livro.getAutor().getNome() : "N/A",
                        livro.getNumeroDownloads() != null ? livro.getNumeroDownloads() : 0);
            }
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 8 — Buscar autor por nome
    // ═══════════════════════════════════════════════════════════════════════════

    private void buscarAutorPorNome() {
        System.out.print(BOLD + "\n🔎 Digite o nome (ou parte) do autor: " + RESET);
        String nome = scanner.nextLine().trim();

        if (nome.isBlank()) {
            System.out.println(RED + "⚠  Nome não pode ser vazio." + RESET);
            return;
        }

        List<Autor> autores = livroService.buscarAutorPorNome(nome);
        separador();
        System.out.println(BOLD + "\n🔎 Resultado para \"" + nome + "\" (" + autores.size() + " encontrado(s)):\n" + RESET);

        if (autores.isEmpty()) {
            System.out.println("  Nenhum autor encontrado com esse nome.");
        } else {
            autores.forEach(a -> {
                System.out.println("  • " + a);
                System.out.println("    Livros: " + a.getLivros().stream()
                        .map(Livro::getTitulo)
                        .reduce((t1, t2) -> t1 + ", " + t2)
                        .orElse("nenhum no catálogo"));
            });
        }
        separador();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // OPÇÃO 9 — Contagem de livros por idioma
    // ═══════════════════════════════════════════════════════════════════════════

    private void exibirContagemPorIdioma() {
        Map<Idioma, Long> contagem = livroService.contarLivrosPorIdioma();
        separador();
        System.out.println(BOLD + "\n🗺  Quantidade de livros por idioma:\n" + RESET);

        contagem.entrySet().stream()
                .filter(e -> e.getValue() > 0)
                .sorted(Map.Entry.<Idioma, Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-12s (%s) : %d livro(s)%n",
                        e.getKey().getNomeExibicao(),
                        e.getKey().getCodigoIso(),
                        e.getValue()));

        long total = contagem.values().stream().mapToLong(Long::longValue).sum();
        if (total == 0) {
            System.out.println("  Nenhum livro cadastrado ainda.");
        }
        separador();
    }
}
