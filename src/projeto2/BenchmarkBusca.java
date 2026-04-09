package projeto2;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Experimentos: compara tempo de um lote de buscas (estruturas montadas antes da medição).
 * Parâmetros: padrões em {@link ConfiguracaoBenchmark}; ajuste via {@link ConfiguracaoBenchmark#lerInterativo(java.util.Scanner, String[])}.
 */
public final class BenchmarkBusca {

    private BenchmarkBusca() {
    }

    /** Uma linha de resultado agregado de uma fase (para o ajuste ao fim da fase). */
    private static final class LinhaMedicaoFase {
        final int n;
        final double mediaNs;

        LinhaMedicaoFase(int n, double mediaNs) {
            this.n = n;
            this.mediaNs = mediaNs;
        }
    }

    public static void main(String[] args) {
        Charset cs = Charset.defaultCharset();
        Scanner s = new Scanner(new InputStreamReader(System.in, cs));
        try {
            rodar(s, args);
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }

    public static void rodar(Scanner entrada, String[] args) throws Exception {
        ConfiguracaoBenchmark cfg = ConfiguracaoBenchmark.lerInterativo(entrada, args != null ? args : new String[0]);
        executar(cfg);
    }

    private enum Fase {
        SEQUENCIAL("Sequencial", "Busca sequencial"),
        BINARIA("Binária", "Busca binária"),
        ARVORE_BST("Árvore BST", "Busca em Árvore BST");

        final String rotulo;
        final String tituloSecao;

        Fase(String rotulo, String tituloSecao) {
            this.rotulo = rotulo;
            this.tituloSecao = tituloSecao;
        }
    }

    private static void executar(ConfiguracaoBenchmark cfg) {
        System.out.println();
        System.out.println("Parametros: " + cfg);
        System.out.println();

        Fase[] ordem = {Fase.SEQUENCIAL, Fase.BINARIA, Fase.ARVORE_BST};
        for (int i = 0; i < ordem.length; i++) {
            rodarFase(cfg, ordem[i], i + 1, ordem.length);
        }
    }

    private static void rodarFase(ConfiguracaoBenchmark cfg, Fase fase, int indice, int total) {
        System.out.printf("=== %d/%d — %s ===%n", indice, total, fase.tituloSecao);
        List<LinhaMedicaoFase> linhas = new ArrayList<LinhaMedicaoFase>();
        for (int n : cfg.tamanhos) {
            linhas.add(medirParaN(n, cfg, fase));
        }
        imprimirResumoTeoriaEmpirico(linhas, fase);
    }

    private static LinhaMedicaoFase medirParaN(int n, ConfiguracaoBenchmark cfg, Fase fase) {
        long seed = cfg.semente;
        Random rVetor = new Random(seed);
        int[] vetorOrdenado = gerarVetorOrdenadoUnico(n, rVetor);

        BuscaArvoreBST arvore = null;
        if (fase == Fase.ARVORE_BST) {
            arvore = new BuscaArvoreBST();
            int[] insercao = Arrays.copyOf(vetorOrdenado, n);
            embaralhar(insercao, new Random(seed ^ 0x9E3779B97F4A7C15L));
            for (int v : insercao) {
                arvore.inserir(v);
            }
        }

        int[] chaves = gerarChavesBusca(vetorOrdenado, cfg.buscasPorExecucao, new Random(seed + 1));

        medirFase(fase, vetorOrdenado, chaves, arvore);

        long[] tempos = new long[cfg.repeticoes];
        for (int i = 0; i < cfg.repeticoes; i++) {
            tempos[i] = medirFase(fase, vetorOrdenado, chaves, arvore);
        }

        double mediaNs = imprimirLinha(n, fase.rotulo, tempos);
        return new LinhaMedicaoFase(n, mediaNs);
    }

    /**
     * Ao fim da fase: O(·) e uma linha t = … com a e b em notação científica (2 casas no mantissa).
     */
    private static void imprimirResumoTeoriaEmpirico(List<LinhaMedicaoFase> linhas, Fase fase) {
        if (linhas.isEmpty()) {
            return;
        }
        int k = linhas.size();
        double[] tMs = new double[k];
        for (int i = 0; i < k; i++) {
            tMs[i] = linhas.get(i).mediaNs / 1_000_000.0;
        }

        switch (fase) {
            case SEQUENCIAL:
                System.out.println("O(n)");
                if (k >= 2) {
                    double[] x = new double[k];
                    for (int i = 0; i < k; i++) {
                        x[i] = linhas.get(i).n;
                    }
                    double[] ab = regressaoMinimosQuadrados(x, tMs);
                    System.out.printf(Locale.US, "t = %.2e * n %+.2e   (t em ms)%n", ab[0], ab[1]);
                }
                break;
            case BINARIA:
            case ARVORE_BST:
                System.out.println("O(log n)");
                if (k >= 2) {
                    double[] x = new double[k];
                    for (int i = 0; i < k; i++) {
                        x[i] = Math.log(linhas.get(i).n) / Math.log(2.0);
                    }
                    double[] ab = regressaoMinimosQuadrados(x, tMs);
                    System.out.printf(Locale.US, "t = %.2e * log2(n) %+.2e   (t em ms)%n", ab[0], ab[1]);
                }
                break;
        }
        System.out.println();
    }

    /** Retorna [a, b] para y ~= a*x + b. */
    private static double[] regressaoMinimosQuadrados(double[] x, double[] y) {
        int n = x.length;
        double mx = 0, my = 0;
        for (int i = 0; i < n; i++) {
            mx += x[i];
            my += y[i];
        }
        mx /= n;
        my /= n;
        double sxx = 0, sxy = 0;
        for (int i = 0; i < n; i++) {
            double dx = x[i] - mx;
            sxx += dx * dx;
            sxy += dx * (y[i] - my);
        }
        if (sxx == 0) {
            return new double[]{0, my};
        }
        double a = sxy / sxx;
        double b = my - a * mx;
        return new double[]{a, b};
    }

    /** Uma execução completa do lote de buscas da fase; duração em nanossegundos. */
    private static long medirFase(Fase fase, int[] vetor, int[] chaves, BuscaArvoreBST arvore) {
        long t0 = System.nanoTime();
        switch (fase) {
            case SEQUENCIAL:
                for (int q : chaves) {
                    BuscaSequencial.buscar(vetor, q);
                }
                break;
            case BINARIA:
                for (int q : chaves) {
                    BuscaBinaria.buscar(vetor, q);
                }
                break;
            case ARVORE_BST:
                for (int q : chaves) {
                    arvore.contem(q);
                }
                break;
        }
        return System.nanoTime() - t0;
    }

    private static double mediaNanos(long[] valores) {
        double s = 0;
        for (long v : valores) {
            s += v;
        }
        return s / valores.length;
    }

    /** Desvio padrão amostral (n − 1 no denominador), em ns. */
    private static double desvioPadraoAmostralNanos(long[] valores, double media) {
        if (valores.length < 2) {
            return 0;
        }
        double acc = 0;
        for (long v : valores) {
            double d = v - media;
            acc += d * d;
        }
        return Math.sqrt(acc / (valores.length - 1));
    }

    private static double imprimirLinha(int n, String rotulo, long[] temposNs) {
        double mediaNs = mediaNanos(temposNs);
        double dpNs = desvioPadraoAmostralNanos(temposNs, mediaNs);
        double mediaMs = mediaNs / 1_000_000.0;
        double dpMs = dpNs / 1_000_000.0;
        System.out.printf(
                "n=%7d | %-12s | média: %10.3f ms | desvio: %8.3f ms (%.2e ns ± %.2e ns)%n",
                n, rotulo, mediaMs, dpMs, mediaNs, dpNs);
        return mediaNs;
    }

    static int[] gerarVetorOrdenadoUnico(int n, Random rnd) {
        java.util.HashSet<Integer> conjunto = new java.util.HashSet<>(Math.max(16, n * 2));
        while (conjunto.size() < n) {
            conjunto.add(rnd.nextInt());
        }
        int[] a = conjunto.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(a);
        return a;
    }

    static void embaralhar(int[] a, Random rnd) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }

    static int[] gerarChavesBusca(int[] ordenado, int quantidade, Random rnd) {
        int n = ordenado.length;
        int[] chaves = new int[quantidade];
        for (int i = 0; i < quantidade; i++) {
            if ((i & 1) == 0) {
                chaves[i] = ordenado[rnd.nextInt(n)];
            } else {
                int candidato;
                do {
                    candidato = rnd.nextInt();
                } while (Arrays.binarySearch(ordenado, candidato) >= 0);
                chaves[i] = candidato;
            }
        }
        return chaves;
    }
}
