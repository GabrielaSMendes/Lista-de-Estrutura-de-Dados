package projeto3;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public final class Projeto3 {

    /** Alinhado ao espírito de {@code projeto2.BenchmarkBusca}: amostragem estatística. */
    private static final int REPETICOES = 30;

    private Projeto3() {
    }

    public static void rodar(Scanner entrada, String[] args) {
        int[] tamanhos = {1000, 5000, 10000};

        for (int n : tamanhos) {
            System.out.println("\n===== TAMANHO: " + n + " =====");

            int[] aleatorio = gerarAleatorio(n);
            int[] ordenado = gerarOrdenado(n);
            int[] inverso = gerarInverso(n);

            System.out.println("\n--- CENÁRIO ALEATÓRIO ---");
            executarTodos(n, aleatorio);

            System.out.println("\n--- CENÁRIO ORDENADO ---");
            executarTodos(n, ordenado);

            System.out.println("\n--- CENÁRIO INVERSO ---");
            executarTodos(n, inverso);
        }
    }

    public static void main(String[] args) {
        rodar(new Scanner(System.in), args);
    }

    private static void executarTodos(int n, int[] vetor) {

        testar(n, "Bubble Sort", vetor, new SortFunction() {
            @Override
            public void sort(int[] array) {
                bubbleSort(array);
            }
        });

        testar(n, "Insertion Sort", vetor, new SortFunction() {
            @Override
            public void sort(int[] array) {
                insertionSort(array);
            }
        });

        testar(n, "Quick Sort", vetor, new SortFunction() {
            @Override
            public void sort(int[] array) {
                quickSort(array);
            }
        });

        testar(n, "Merge Sort", vetor, new SortFunction() {
            @Override
            public void sort(int[] array) {
                mergeSort(array);
            }
        });
    }

    private static void testar(int n, String nome, int[] vetorOriginal, SortFunction func) {
        long[] temposNs = new long[REPETICOES];
        for (int i = 0; i < REPETICOES; i++) {
            int[] vetor = Arrays.copyOf(vetorOriginal, vetorOriginal.length);
            long t0 = System.nanoTime();
            func.sort(vetor);
            temposNs[i] = System.nanoTime() - t0;
        }
        double mediaNs = mediaNanos(temposNs);
        double dpNs = desvioPadraoAmostralNanos(temposNs, mediaNs);
        double mediaMs = mediaNs / 1_000_000.0;
        double dpMs = dpNs / 1_000_000.0;
        System.out.printf(Locale.US,
                "n=%7d | %-14s | média: %10.3f ms | desvio: %8.3f ms (%.2e ns ± %.2e ns)%n",
                n, nome, mediaMs, dpMs, mediaNs, dpNs);
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

    private interface SortFunction {
        void sort(int[] array);
    }

    private static int[] gerarAleatorio(int n) {
        Random rand = new Random(0x243F6A8885A308D3L ^ (long) n);
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = rand.nextInt(100000);
        }
        return v;
    }

    private static int[] gerarOrdenado(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = i;
        }
        return v;
    }

    private static int[] gerarInverso(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = n - i;
        }
        return v;
    }

    private static void bubbleSort(int[] v) {
        int n = v.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (v[j] > v[j + 1]) {
                    int temp = v[j];
                    v[j] = v[j + 1];
                    v[j + 1] = temp;
                }
            }
        }
    }

    private static void insertionSort(int[] v) {
        for (int i = 1; i < v.length; i++) {
            int key = v[i];
            int j = i - 1;

            while (j >= 0 && v[j] > key) {
                v[j + 1] = v[j];
                j--;
            }

            v[j + 1] = key;
        }
    }

    private static void quickSort(int[] v) {
        quickSortRec(v, 0, v.length - 1);
    }

    private static void quickSortRec(int[] v, int inicio, int fim) {
        if (inicio < fim) {
            int index = particionar(v, inicio, fim);
            quickSortRec(v, inicio, index - 1);
            quickSortRec(v, index, fim);
        }
    }

    private static int particionar(int[] v, int inicio, int fim) {
        int meio = (inicio + fim) / 2;
        int pivo = v[meio];

        int i = inicio;
        int j = fim;

        while (i <= j) {
            while (v[i] < pivo) {
                i++;
            }
            while (v[j] > pivo) {
                j--;
            }

            if (i <= j) {
                int temp = v[i];
                v[i] = v[j];
                v[j] = temp;
                i++;
                j--;
            }
        }

        return i;
    }

    private static void mergeSort(int[] v) {
        if (v.length < 2) {
            return;
        }

        int meio = v.length / 2;

        int[] esquerda = Arrays.copyOfRange(v, 0, meio);
        int[] direita = Arrays.copyOfRange(v, meio, v.length);

        mergeSort(esquerda);
        mergeSort(direita);

        merge(v, esquerda, direita);
    }

    private static void merge(int[] v, int[] esq, int[] dir) {
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < esq.length && j < dir.length) {
            if (esq[i] <= dir[j]) {
                v[k++] = esq[i++];
            } else {
                v[k++] = dir[j++];
            }
        }

        while (i < esq.length) {
            v[k++] = esq[i++];
        }

        while (j < dir.length) {
            v[k++] = dir[j++];
        }
    }
}
