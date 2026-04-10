package projeto1;

import projeto1.trees.*;
import projeto1.tsp.TSP;

import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

public final class Projeto1 {

    private Projeto1() {
    }

    static final int RUNS = 30;
    static final int[] TREE_SIZES = {1_000, 10_000, 100_000};
    static final int[] TSP_SIZES = {50, 200, 500};

    static final int BANNER = 70;
    static final Locale OUT = Locale.US;

    static double mean(double[] v) {
        double s = 0;
        for (double x : v) s += x;
        return s / v.length;
    }

    static double stddev(double[] v) {
        double m = mean(v), s = 0;
        for (double x : v) s += (x - m) * (x - m);
        return Math.sqrt(s / (v.length - 1));
    }

    static double min(double[] v) {
        double m = v[0];
        for (double x : v) if (x < m) m = x;
        return m;
    }

    static double max(double[] v) {
        double m = v[0];
        for (double x : v) if (x > m) m = x;
        return m;
    }

    static String repeat(char c, int n) {
        char[] a = new char[n];
        Arrays.fill(a, c);
        return new String(a);
    }

    static void banner(char edge, String title) {
        String line = repeat(edge, BANNER);
        System.out.println();
        System.out.println(line);
        int pad = (BANNER - title.length()) / 2;
        if (pad < 2) pad = 2;
        System.out.println(repeat(' ', pad) + title);
        System.out.println(line);
    }

    static void printTreeTable(String metricTitle, double[] bst, double[] avl, double[] rb) {
        System.out.println();
        System.out.println("    * " + metricTitle);
        System.out.println("    +" + repeat('-', 62) + "+");
        System.out.println("    | Estrutura     |     media |        dp |       min |       max |");
        System.out.println("    +" + repeat('-', 62) + "+");
        printTreeRow("BST", bst);
        printTreeRow("AVL", avl);
        printTreeRow("Red-Black", rb);
        System.out.println("    +" + repeat('-', 62) + "+");
    }

    static void printTreeRow(String label, double[] v) {
        System.out.printf(OUT, "    | %-13s | %9.4f | %9.4f | %9.4f | %9.4f |%n",
                label, mean(v), stddev(v), min(v), max(v));
    }

    static void printTspBlock(double[] lengths, double[] times) {
        System.out.println();
        System.out.println("    * Resultado (NN + 2-opt)");
        System.out.println("    +" + repeat('-', 62) + "+");
        System.out.println("    | Medida                    |     media |        dp |       min |       max |");
        System.out.println("    +" + repeat('-', 62) + "+");
        System.out.printf(OUT, "    | %-25s | %9.2f | %9.2f | %9.2f | %9.2f |%n",
                "Comprimento do tour", mean(lengths), stddev(lengths), min(lengths), max(lengths));
        System.out.printf(OUT, "    | %-25s | %9.4f | %9.4f | %9.4f | %9.4f |%n",
                "Tempo de execucao (ms)", mean(times), stddev(times), min(times), max(times));
        System.out.println("    +" + repeat('-', 62) + "+");
    }

    static void runTreeExperiment() {
        banner('=', "EXPERIMENTOS - ARVORES DE BUSCA (BST, AVL, Red-Black)");

        for (int size : TREE_SIZES) {
            System.out.println();
            System.out.println("  >>  n = " + String.format(OUT, "%,d", size)
                    + " elementos   |   " + RUNS + " execucoes por estrutura");

            double[] bstIns = new double[RUNS], bstSrch = new double[RUNS],
                    bstRem = new double[RUNS], bstH = new double[RUNS];
            double[] avlIns = new double[RUNS], avlSrch = new double[RUNS],
                    avlRem = new double[RUNS], avlH = new double[RUNS];
            double[] rbIns = new double[RUNS], rbSrch = new double[RUNS],
                    rbRem = new double[RUNS], rbH = new double[RUNS];

            Random rng = new Random(42);

            for (int r = 0; r < RUNS; r++) {
                int[] data = new int[size];
                for (int i = 0; i < size; i++) data[i] = rng.nextInt(size * 10);

                BST<Integer> bst = new BST<>();
                long t0 = System.nanoTime();
                for (int v : data) bst.insert(v);
                bstIns[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) bst.search(data[rng.nextInt(size)]);
                bstSrch[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 500; i++) bst.remove(data[rng.nextInt(size)]);
                bstRem[r] = (System.nanoTime() - t0) / 1e6;
                bstH[r] = bst.height();

                AVL<Integer> avl = new AVL<>();
                t0 = System.nanoTime();
                for (int v : data) avl.insert(v);
                avlIns[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) avl.search(data[rng.nextInt(size)]);
                avlSrch[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 500; i++) avl.remove(data[rng.nextInt(size)]);
                avlRem[r] = (System.nanoTime() - t0) / 1e6;
                avlH[r] = avl.height();

                RedBlack<Integer> rb = new RedBlack<>();
                t0 = System.nanoTime();
                for (int v : data) rb.insert(v);
                rbIns[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 1000; i++) rb.search(data[rng.nextInt(size)]);
                rbSrch[r] = (System.nanoTime() - t0) / 1e6;

                t0 = System.nanoTime();
                for (int i = 0; i < 500; i++) rb.remove(data[rng.nextInt(size)]);
                rbRem[r] = (System.nanoTime() - t0) / 1e6;
                rbH[r] = rb.height();
            }

            printTreeTable("Insercao (ms)", bstIns, avlIns, rbIns);
            printTreeTable("Busca (1000 ops, ms)", bstSrch, avlSrch, rbSrch);
            printTreeTable("Remocao (500 ops, ms)", bstRem, avlRem, rbRem);
            printTreeTable("Altura final da arvore", bstH, avlH, rbH);
        }
    }

    static void runTSPExperiment() {
        banner('=', "EXPERIMENTOS - CAIXEIRO VIAJANTE (vizinho mais proximo + 2-opt)");

        for (int size : TSP_SIZES) {
            System.out.println();
            System.out.println("  >>  n = " + size + " cidades   |   " + RUNS + " execucoes");

            double[] lengths = new double[RUNS];
            double[] times = new double[RUNS];

            for (int r = 0; r < RUNS; r++) {
                Random rng = new Random(r * 1337L);
                double[][] dist = TSP.randomInstance(size, rng);
                TSP tsp = new TSP(dist);
                long t0 = System.nanoTime();
                lengths[r] = tsp.solve();
                times[r] = (System.nanoTime() - t0) / 1e6;
            }

            printTspBlock(lengths, times);
        }
    }

    public static void rodar(Scanner entrada, String[] args) {
        banner('=', "ANALISE EXPERIMENTAL - ESTRUTURAS DE DADOS E TSP");
        runTreeExperiment();
        runTSPExperiment();
        System.out.println();
        System.out.println("  " + repeat('=', BANNER - 4));
        System.out.println("  OK  Todos os experimentos concluidos.");
        System.out.println("  " + repeat('=', BANNER - 4));
        System.out.println();
    }

    public static void main(String[] args) {
        rodar(new Scanner(System.in), args);
    }
}

