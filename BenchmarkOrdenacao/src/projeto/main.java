package projeto;

import java.util.Arrays;
import java.util.Random;

public class main {

    public static void main(String[] args) {

        int[] tamanhos = {1000, 10000, 100000};

        for (int n : tamanhos) {
            System.out.println("\n===== TAMANHO: " + n + " =====");

            int[] aleatorio = gerarAleatorio(n);
            int[] ordenado = gerarOrdenado(n);
            int[] inverso = gerarInverso(n);

            System.out.println("\n--- CENÁRIO ALEATÓRIO ---");
            executarTodos(aleatorio);

            System.out.println("\n--- CENÁRIO ORDENADO ---");
            executarTodos(ordenado);

            System.out.println("\n--- CENÁRIO INVERSO ---");
            executarTodos(inverso);
        }
    }

    public static void executarTodos(int[] vetor) {

        testar("Bubble Sort", vetor, new SortFunction() {
            public void sort(int[] array) {
                bubbleSort(array);
            }
        });

        testar("Insertion Sort", vetor, new SortFunction() {
            public void sort(int[] array) {
                insertionSort(array);
            }
        });

        testar("Quick Sort", vetor, new SortFunction() {
            public void sort(int[] array) {
                quickSort(array);
            }
        });

        testar("Merge Sort", vetor, new SortFunction() {
            public void sort(int[] array) {
                mergeSort(array);
            }
        });
    }

    // ================= TESTE =================
    public static void testar(String nome, int[] vetorOriginal, SortFunction func) {
        int[] vetor = Arrays.copyOf(vetorOriginal, vetorOriginal.length);

        long inicio = System.currentTimeMillis();
        func.sort(vetor);
        long fim = System.currentTimeMillis();

        System.out.println(nome + ": " + (fim - inicio) + " ms");
    }

    interface SortFunction {
        void sort(int[] array);
    }

    // ================= GERADORES =================
    public static int[] gerarAleatorio(int n) {
        Random rand = new Random();
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = rand.nextInt(100000);
        }
        return v;
    }

    public static int[] gerarOrdenado(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = i;
        }
        return v;
    }

    public static int[] gerarInverso(int n) {
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = n - i;
        }
        return v;
    }

    // ================= ALGORITMOS =================

    public static void bubbleSort(int[] v) {
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

    public static void insertionSort(int[] v) {
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

    // ✅ QUICK SORT CORRIGIDO (SEM STACKOVERFLOW)
    public static void quickSort(int[] v) {
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
            while (v[i] < pivo) i++;
            while (v[j] > pivo) j--;

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

    public static void mergeSort(int[] v) {
        if (v.length < 2)
            return;

        int meio = v.length / 2;

        int[] esquerda = Arrays.copyOfRange(v, 0, meio);
        int[] direita = Arrays.copyOfRange(v, meio, v.length);

        mergeSort(esquerda);
        mergeSort(direita);

        merge(v, esquerda, direita);
    }

    private static void merge(int[] v, int[] esq, int[] dir) {
        int i = 0, j = 0, k = 0;

        while (i < esq.length && j < dir.length) {
            if (esq[i] <= dir[j]) {
                v[k++] = esq[i++];
            } else {
                v[k++] = dir[j++];
            }
        }

        while (i < esq.length)
            v[k++] = esq[i++];

        while (j < dir.length)
            v[k++] = dir[j++];
    }
}