package projeto1;

import java.util.*;

/**
 * ============================================================
 *  TRABALHO PRÁTICO — ESTRUTURAS DE DADOS E ALGORITMOS
 *  BST · AVL · RUBRO-NEGRA · CAIXEIRO VIAJANTE (NN + 2-opt)
 *  Experimentos comparativos com análise estatística (30 runs)
 * ============================================================
 */
public class TrabalhoAD {

    // =========================================================
    //  1. BST — Árvore Binária de Busca
    // =========================================================
    static class BST<T extends Comparable<T>> {
        private Node root;

        private class Node {
            T key;
            Node left, right;

            Node(T k) {
                key = k;
            }
        }

        /** Inserção O(h) */
        public void insert(T k) {
            root = ins(root, k);
        }

        private Node ins(Node n, T k) {
            if (n == null) return new Node(k);
            int c = k.compareTo(n.key);
            if (c < 0) n.left = ins(n.left, k);
            else if (c > 0) n.right = ins(n.right, k);
            return n;
        }

        /** Busca O(h) */
        public boolean search(T k) {
            Node n = root;
            while (n != null) {
                int c = k.compareTo(n.key);
                if (c == 0) return true;
                n = c < 0 ? n.left : n.right;
            }
            return false;
        }

        /** Remoção O(h) */
        public void remove(T k) {
            root = rem(root, k);
        }

        private Node rem(Node n, T k) {
            if (n == null) return null;
            int c = k.compareTo(n.key);
            if (c < 0) n.left = rem(n.left, k);
            else if (c > 0) n.right = rem(n.right, k);
            else {
                if (n.left == null) return n.right;
                if (n.right == null) return n.left;
                Node m = min(n.right);
                n.key = m.key;
                n.right = rem(n.right, m.key);
            }
            return n;
        }

        private Node min(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }

        /** Altura O(n) */
        public int height() {
            return h(root);
        }

        private int h(Node n) {
            return n == null ? -1 : 1 + Math.max(h(n.left), h(n.right));
        }

        public void clear() {
            root = null;
        }
    }

    // =========================================================
    //  2. AVL — Árvore AVL
    // =========================================================
    static class AVL<T extends Comparable<T>> {
        private Node root;

        private class Node {
            T key;
            Node left, right;
            int height;

            Node(T k) {
                key = k;
            }
        }

        private int h(Node n) {
            return n == null ? -1 : n.height;
        }

        private void upH(Node n) {
            n.height = 1 + Math.max(h(n.left), h(n.right));
        }

        private int bf(Node n) {
            return n == null ? 0 : h(n.left) - h(n.right);
        }

        private Node rotL(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            upH(x);
            upH(y);
            return y;
        }

        private Node rotR(Node y) {
            Node x = y.left;
            y.left = x.right;
            x.right = y;
            upH(y);
            upH(x);
            return x;
        }

        private Node bal(Node n) {
            upH(n);
            if (bf(n) > 1) {
                if (bf(n.left) < 0) n.left = rotL(n.left);
                return rotR(n);
            }
            if (bf(n) < -1) {
                if (bf(n.right) > 0) n.right = rotR(n.right);
                return rotL(n);
            }
            return n;
        }

        /** Inserção O(log n) */
        public void insert(T k) {
            root = ins(root, k);
        }

        private Node ins(Node n, T k) {
            if (n == null) return new Node(k);
            int c = k.compareTo(n.key);
            if (c < 0) n.left = ins(n.left, k);
            else if (c > 0) n.right = ins(n.right, k);
            else return n;
            return bal(n);
        }

        /** Busca O(log n) */
        public boolean search(T k) {
            Node n = root;
            while (n != null) {
                int c = k.compareTo(n.key);
                if (c == 0) return true;
                n = c < 0 ? n.left : n.right;
            }
            return false;
        }

        /** Remoção O(log n) */
        public void remove(T k) {
            root = rem(root, k);
        }

        private Node rem(Node n, T k) {
            if (n == null) return null;
            int c = k.compareTo(n.key);
            if (c < 0) n.left = rem(n.left, k);
            else if (c > 0) n.right = rem(n.right, k);
            else {
                if (n.left == null) return n.right;
                if (n.right == null) return n.left;
                Node m = min(n.right);
                n.key = m.key;
                n.right = rem(n.right, m.key);
            }
            return bal(n);
        }

        private Node min(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }

        /** Altura O(1) */
        public int height() {
            return h(root);
        }

        public void clear() {
            root = null;
        }
    }

    // =========================================================
    //  3. RUBRO-NEGRA (Left-Leaning, Sedgewick)
    // =========================================================
    static class RedBlack<T extends Comparable<T>> {
        private static final boolean R = true, B = false;
        private Node root;

        private class Node {
            T key;
            Node left, right;
            boolean color;

            Node(T k) {
                key = k;
                color = R;
            }
        }

        private boolean isR(Node n) {
            return n != null && n.color == R;
        }

        private Node rotL(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            y.color = x.color;
            x.color = R;
            return y;
        }

        private Node rotR(Node y) {
            Node x = y.left;
            y.left = x.right;
            x.right = y;
            x.color = y.color;
            y.color = R;
            return x;
        }

        private void flip(Node n) {
            n.color = R;
            n.left.color = B;
            n.right.color = B;
        }

        /** Inserção O(log n) */
        public void insert(T k) {
            root = ins(root, k);
            root.color = B;
        }

        private Node ins(Node n, T k) {
            if (n == null) return new Node(k);
            int c = k.compareTo(n.key);
            if (c < 0) n.left = ins(n.left, k);
            else if (c > 0) n.right = ins(n.right, k);
            if (isR(n.right) && !isR(n.left)) n = rotL(n);
            if (isR(n.left) && isR(n.left.left)) n = rotR(n);
            if (isR(n.left) && isR(n.right)) flip(n);
            return n;
        }

        /** Busca O(log n) */
        public boolean search(T k) {
            Node n = root;
            while (n != null) {
                int c = k.compareTo(n.key);
                if (c == 0) return true;
                n = c < 0 ? n.left : n.right;
            }
            return false;
        }

        /** Remoção O(log n) */
        public void remove(T k) {
            if (!search(k)) return;
            if (!isR(root.left) && !isR(root.right)) root.color = R;
            root = del(root, k);
            if (root != null) root.color = B;
        }

        private Node del(Node n, T k) {
            if (k.compareTo(n.key) < 0) {
                if (!isR(n.left) && !isR(n.left.left)) n = mvL(n);
                n.left = del(n.left, k);
            } else {
                if (isR(n.left)) n = rotR(n);
                if (k.compareTo(n.key) == 0 && n.right == null) return null;
                if (!isR(n.right) && !isR(n.right.left)) n = mvR(n);
                if (k.compareTo(n.key) == 0) {
                    Node m = min(n.right);
                    n.key = m.key;
                    n.right = delMin(n.right);
                } else n.right = del(n.right, k);
            }
            return fix(n);
        }

        private Node mvL(Node n) {
            flip(n);
            if (isR(n.right.left)) {
                n.right = rotR(n.right);
                n = rotL(n);
                flip(n);
            }
            return n;
        }

        private Node mvR(Node n) {
            flip(n);
            if (isR(n.left.left)) {
                n = rotR(n);
                flip(n);
            }
            return n;
        }

        private Node delMin(Node n) {
            if (n.left == null) return null;
            if (!isR(n.left) && !isR(n.left.left)) n = mvL(n);
            n.left = delMin(n.left);
            return fix(n);
        }

        private Node fix(Node n) {
            if (isR(n.right)) n = rotL(n);
            if (isR(n.left) && isR(n.left.left)) n = rotR(n);
            if (isR(n.left) && isR(n.right)) flip(n);
            return n;
        }

        private Node min(Node n) {
            while (n.left != null) n = n.left;
            return n;
        }

        /** Altura O(n) */
        public int height() {
            return h(root);
        }

        private int h(Node n) {
            return n == null ? -1 : 1 + Math.max(h(n.left), h(n.right));
        }

        public void clear() {
            root = null;
        }
    }

    // =========================================================
    //  4. TSP — Vizinho Mais Próximo + 2-opt
    // =========================================================
    static class TSP {
        private final double[][] d;
        private final int n;

        TSP(double[][] d) {
            this.d = d;
            this.n = d.length;
        }

        /** Gera instância Euclidiana aleatória */
        static double[][] randomInstance(int n, Random rng) {
            double[] x = new double[n], y = new double[n];
            for (int i = 0; i < n; i++) {
                x[i] = rng.nextDouble() * 1000;
                y[i] = rng.nextDouble() * 1000;
            }
            double[][] d = new double[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    d[i][j] = Math.hypot(x[i] - x[j], y[i] - y[j]);
            return d;
        }

        /** Heurística construtiva: vizinho mais próximo */
        int[] nearestNeighbor(int start) {
            boolean[] vis = new boolean[n];
            int[] tour = new int[n];
            tour[0] = start;
            vis[start] = true;
            for (int i = 1; i < n; i++) {
                int cur = tour[i - 1], best = -1;
                double bd = Double.MAX_VALUE;
                for (int j = 0; j < n; j++)
                    if (!vis[j] && d[cur][j] < bd) {
                        bd = d[cur][j];
                        best = j;
                    }
                tour[i] = best;
                vis[best] = true;
            }
            return tour;
        }

        /** Melhoria local: 2-opt */
        int[] twoOpt(int[] tour) {
            int[] t = Arrays.copyOf(tour, n);
            boolean improved = true;
            while (improved) {
                improved = false;
                for (int i = 0; i < n - 1; i++) {
                    for (int j = i + 2; j < n; j++) {
                        if (j == n - 1 && i == 0) continue;
                        double delta = d[t[i]][t[j]] + d[t[i + 1]][t[(j + 1) % n]]
                                - d[t[i]][t[i + 1]] - d[t[j]][t[(j + 1) % n]];
                        if (delta < -1e-10) {
                            rev(t, i + 1, j);
                            improved = true;
                        }
                    }
                }
            }
            return t;
        }

        private void rev(int[] a, int l, int r) {
            while (l < r) {
                int tmp = a[l];
                a[l++] = a[r];
                a[r--] = tmp;
            }
        }

        double len(int[] t) {
            double s = 0;
            for (int i = 0; i < n; i++) s += d[t[i]][t[(i + 1) % n]];
            return s;
        }

        /** NN + 2-opt combinados */
        double solve() {
            return len(twoOpt(nearestNeighbor(0)));
        }
    }

    // =========================================================
    //  5. ESTATÍSTICA
    // =========================================================
    static double mean(double[] v) {
        double s = 0;
        for (double x : v) s += x;
        return s / v.length;
    }

    static double std(double[] v) {
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

    static void stat(String label, double[] v) {
        System.out.printf("    %-14s  média=%9.3f  dp=%8.3f  min=%9.3f  max=%9.3f%n",
                label, mean(v), std(v), min(v), max(v));
    }

    // =========================================================
    //  6. EXPERIMENTOS
    // =========================================================
    static final int RUNS = 30;

    static void runTrees() {
        int[] sizes = {1_000, 10_000, 100_000};
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║            EXPERIMENTOS — ÁRVORES DE BUSCA               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        for (int N : sizes) {
            System.out.printf("%n▶  n = %,7d   (%d execuções)%n", N, RUNS);

            double[][] ins = new double[3][RUNS];
            double[][] srch = new double[3][RUNS];
            double[][] rem = new double[3][RUNS];
            double[][] ht = new double[3][RUNS];
            String[] names = {"BST", "AVL", "Red-Black"};

            Random rng = new Random(42);
            for (int r = 0; r < RUNS; r++) {
                int[] data = new int[N];
                for (int i = 0; i < N; i++) data[i] = rng.nextInt(N * 10);

                BST<Integer> bst = new BST<>();
                AVL<Integer> avl = new AVL<>();
                RedBlack<Integer> rb = new RedBlack<>();

                // Inserção
                long t;
                t = System.nanoTime();
                for (int v : data) bst.insert(v);
                ins[0][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int v : data) avl.insert(v);
                ins[1][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int v : data) rb.insert(v);
                ins[2][r] = (System.nanoTime() - t) / 1e6;

                // Busca (1000 ops aleatórias)
                t = System.nanoTime();
                for (int i = 0; i < 1000; i++) bst.search(data[rng.nextInt(N)]);
                srch[0][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int i = 0; i < 1000; i++) avl.search(data[rng.nextInt(N)]);
                srch[1][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int i = 0; i < 1000; i++) rb.search(data[rng.nextInt(N)]);
                srch[2][r] = (System.nanoTime() - t) / 1e6;

                // Remoção (500 ops aleatórias)
                t = System.nanoTime();
                for (int i = 0; i < 500; i++) bst.remove(data[rng.nextInt(N)]);
                rem[0][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int i = 0; i < 500; i++) avl.remove(data[rng.nextInt(N)]);
                rem[1][r] = (System.nanoTime() - t) / 1e6;
                t = System.nanoTime();
                for (int i = 0; i < 500; i++) rb.remove(data[rng.nextInt(N)]);
                rem[2][r] = (System.nanoTime() - t) / 1e6;

                // Altura
                ht[0][r] = bst.height();
                ht[1][r] = avl.height();
                ht[2][r] = rb.height();
            }

            String[] ops = {"Inserção (ms)", "Busca 1k (ms)", "Remoção 500 (ms)", "Altura"};
            double[][][] all = {ins, srch, rem, ht};
            for (int op = 0; op < 4; op++) {
                System.out.println("  [" + ops[op] + "]");
                for (int t2 = 0; t2 < 3; t2++) stat(names[t2], all[op][t2]);
            }
        }
    }

    static void runTSP() {
        int[] sizes = {50, 200, 500};
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║    EXPERIMENTOS — CAIXEIRO VIAJANTE (NN + 2-opt)         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");

        for (int N : sizes) {
            System.out.printf("%n▶  cidades = %d   (%d execuções)%n", N, RUNS);
            double[] lens = new double[RUNS], times = new double[RUNS];
            for (int r = 0; r < RUNS; r++) {
                Random rng = new Random(r * 1337L);
                double[][] d = TSP.randomInstance(N, rng);
                TSP tsp = new TSP(d);
                long t = System.nanoTime();
                lens[r] = tsp.solve();
                times[r] = (System.nanoTime() - t) / 1e6;
            }
            System.out.println("  [Comprimento do tour]");
            stat("NN+2opt", lens);
            System.out.println("  [Tempo de execução (ms)]");
            stat("NN+2opt", times);
        }
    }

    // =========================================================
    //  7. MAIN
    // =========================================================
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  ANÁLISE EXPERIMENTAL — ESTRUTURAS DE DADOS E TSP        ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        runTrees();
        runTSP();
        System.out.println("\n✔  Experimentos concluídos com sucesso.");
    }
}

