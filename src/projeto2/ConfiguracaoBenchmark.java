package projeto2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Parâmetros do benchmark: valores padrão da atividade embutidos; configuração via menu no console.
 */
public final class ConfiguracaoBenchmark {

    /** Valores padrão (edital / experimentos típicos). A semente padrão é aleatória a cada execução. */
    public static final int REPETICOES_PADRAO = 30;
    public static final int BUSCAS_POR_EXECUCAO_PADRAO = 10_000;
    public static final int[] TAMANHOS_PADRAO = {1_000, 5_000, 10_000, 20_000, 30_000, 50_000};

    public final long semente;
    public final int repeticoes;
    public final int buscasPorExecucao;
    public final int[] tamanhos;

    private ConfiguracaoBenchmark(long semente, int repeticoes, int buscasPorExecucao, int[] tamanhos) {
        this.semente = semente;
        this.repeticoes = repeticoes;
        this.buscasPorExecucao = buscasPorExecucao;
        this.tamanhos = tamanhos;
    }

    /** Nova semente pseudoaleatória (64 bits) para esta execução. */
    public static long novaSementeAleatoria() {
        return ThreadLocalRandom.current().nextLong();
    }

    /**
     * Pergunta cada parâmetro no console; linha vazia mantém o padrão exibido.
     *
     * @param args se {@code args[0]} existir, define apenas o <em>valor sugerido</em> da semente (ainda pode ser alterado no menu).
     */
    public static ConfiguracaoBenchmark lerInterativo(Scanner in, String[] args) {
        long sugestaoSemente = novaSementeAleatoria();
        if (args != null && args.length >= 1 && !args[0].trim().isEmpty()) {
            sugestaoSemente = Long.parseLong(args[0].trim());
        }

        System.out.println();
        System.out.println("--- Projeto 2 | configuracao do benchmark ---");
        System.out.println();

        long semente = perguntaLong(in, "Semente", sugestaoSemente);
        int repeticoes = perguntaInt(in, "Repeticoes por tamanho (media/desvio)", REPETICOES_PADRAO);
        int buscas = perguntaInt(in, "Buscas por medicao (tamanho do lote)", BUSCAS_POR_EXECUCAO_PADRAO);
        int[] tamanhos = perguntaTamanhos(in, TAMANHOS_PADRAO);

        validar(repeticoes, buscas, tamanhos);
        return new ConfiguracaoBenchmark(semente, repeticoes, buscas, tamanhos);
    }

    private static long perguntaLong(Scanner in, String rotulo, long padrao) {
        System.out.print(rotulo + " [" + padrao + "]: ");
        System.out.flush();
        if (!in.hasNextLine()) {
            return padrao;
        }
        String linha = in.nextLine().trim();
        if (linha.isEmpty()) {
            return padrao;
        }
        return Long.parseLong(linha);
    }

    private static int perguntaInt(Scanner in, String rotulo, int padrao) {
        System.out.print(rotulo + " [" + padrao + "]: ");
        System.out.flush();
        if (!in.hasNextLine()) {
            return padrao;
        }
        String linha = in.nextLine().trim();
        if (linha.isEmpty()) {
            return padrao;
        }
        return Integer.parseInt(linha);
    }

    private static int[] perguntaTamanhos(Scanner in, int[] padrao) {
        String exibir = tamanhosParaTexto(padrao);
        System.out.print("Tamanhos n (quantos quiser, separados por virgula; [] opcionais) [" + exibir + "]: ");
        System.out.flush();
        if (!in.hasNextLine()) {
            return padrao.clone();
        }
        String linha = in.nextLine().trim();
        if (linha.isEmpty()) {
            return padrao.clone();
        }
        return parseTamanhosLinha(linha);
    }

    private static String tamanhosParaTexto(int[] t) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(t[i]);
        }
        return sb.toString();
    }

    /**
     * Aceita {@code 5000,20000}, {@code [5000, 20000]}, espaços e qualquer quantidade de inteiros.
     */
    private static int[] parseTamanhosLinha(String raw) {
        String s = raw.trim(); 
        if (s.startsWith("[") && s.endsWith("]")) {
            s = s.substring(1, s.length() - 1).trim();
        }
        String[] partes = s.split(",");
        List<Integer> lista = new ArrayList<Integer>();
        for (String parte : partes) {
            String t = parte.replace('[', ' ').replace(']', ' ').trim();
            if (!t.isEmpty()) {
                lista.add(Integer.parseInt(t));
            }
        }
        if (lista.isEmpty()) {
            return TAMANHOS_PADRAO.clone();
        }
        return lista.stream().mapToInt(Integer::intValue).toArray();
    }

    private static void validar(int repeticoes, int buscas, int[] tamanhos) {
        if (repeticoes < 1) {
            throw new IllegalArgumentException("repeticoes deve ser >= 1");
        }
        if (buscas < 1) {
            throw new IllegalArgumentException("buscas por medicao deve ser >= 1");
        }
        if (tamanhos.length < 1) {
            throw new IllegalArgumentException("informe pelo menos um tamanho n");
        }
        for (int n : tamanhos) {
            if (n < 1) {
                throw new IllegalArgumentException("cada n deve ser >= 1: " + n);
            }
        }
    }

    @Override
    public String toString() {
        return "semente=" + semente + ", repeticoes=" + repeticoes + ", buscasPorExecucao=" + buscasPorExecucao
                + ", tamanhos=" + Arrays.toString(tamanhos);
    }
}
