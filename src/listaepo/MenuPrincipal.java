package listaepo;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import projeto1.Projeto1;
import projeto2.BenchmarkBusca;
import projeto3.Projeto3;

/**
 * Ponto de entrada do trabalho: menu na raiz da aplicação; cada entrega do edital fica no seu pacote ({@code projeto1}, {@code projeto2}, {@code projeto3}).
 */
public final class MenuPrincipal {

    private MenuPrincipal() {
    }

    public static void main(String[] args) {
        Path inicio = Paths.get("").toAbsolutePath().normalize();
        Path repoRoot = encontrarRaizRepositorio(inicio);
        if (repoRoot == null) {
            System.err.println("Não encontrei a raiz do repositório (pasta com src/listaepo) a partir de: " + inicio);
            System.exit(1);
        }

        Charset console = Charset.defaultCharset();
        Scanner entrada = new Scanner(new InputStreamReader(System.in, console));
        try {
            loopMenu(repoRoot, entrada);
            System.out.println();
            System.out.println("finalizando ...");
        } finally {
            entrada.close();
        }
    }

    /** Sobe diretórios até achar {@code src/listaepo} (layout padrão deste repo). */
    private static Path encontrarRaizRepositorio(Path diretorioAtual) {
        Path p = diretorioAtual;
        for (int i = 0; i < 10 && p != null; i++) {
            if (Files.isDirectory(p.resolve("src").resolve("listaepo"))) {
                return p;
            }
            p = p.getParent();
        }
        return null;
    }

    private static void loopMenu(Path repoRoot, Scanner entrada) {
        while (true) {
            System.out.println();
            System.out.println("========================================");
            System.out.println("  Lista - Estrutura de Dados");
            System.out.println("========================================");
            System.out.println();
            System.out.println("  [1]  Projeto 1 - Árvores e balanceamento");
            System.out.println("  [2]  Projeto 2 - Sistemas de busca");
            System.out.println("  [3]  Projeto 3 - Benchmark de ordenação");
            System.out.println("  [0]  Sair");
            System.out.println();
            System.out.print("Digite a opção: ");
            System.out.flush();

            if (!entrada.hasNextLine()) {
                return;
            }
            String linha = entrada.nextLine().trim();
            if (linha.isEmpty()) {
                System.out.println("Opção inválida.");
                continue;
            }
            if ("0".equals(linha)) {
                return;
            }
            if ("1".equals(linha)) {
                executarProjeto1(entrada);
            } else if ("2".equals(linha)) {
                executarProjeto2(entrada);
            } else if ("3".equals(linha)) {
                executarProjeto3(entrada);
            } else {
                System.out.println("Opção inválida.");
            }
            aguardarEnter(entrada);
        }
    }

    private static void aguardarEnter(Scanner entrada) {
        System.out.println();
        System.out.print("Pressione Enter para voltar ao menu... ");
        System.out.flush();
        if (entrada.hasNextLine()) {
            entrada.nextLine();
        }
    }

    private static void executarProjeto3(Scanner entrada) {
        try {
            Projeto3.rodar(entrada, new String[0]);
        } catch (Exception e) {
            System.err.println("Erro ao executar o benchmark de ordenação: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static void executarProjeto2(Scanner entrada) {
        try {
            BenchmarkBusca.rodar(entrada, new String[0]);
        } catch (Exception e) {
            System.err.println("Erro ao executar o benchmark: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    private static void executarProjeto1(Scanner entrada) {
        try {
            Projeto1.rodar(entrada, new String[0]);
        } catch (Exception e) {
            System.err.println("Erro ao executar o Projeto 1: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
