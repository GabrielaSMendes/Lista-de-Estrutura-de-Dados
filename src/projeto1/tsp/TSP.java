package projeto1.tsp;

import java.util.*;

public class TSP {

    private final double[][] dist;
    private final int n;

    public TSP(double[][] dist) {
        this.dist = dist;
        this.n = dist.length;
    }

    // ── Vizinho mais próximo (heurística construtiva) ─────────────────────────
    public int[] nearestNeighbor(int start) {
        boolean[] visited = new boolean[n];
        int[] tour = new int[n];
        tour[0] = start;
        visited[start] = true;
        for (int i = 1; i < n; i++) {
            int cur = tour[i - 1];
            int best = -1;
            double bestDist = Double.MAX_VALUE;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && dist[cur][j] < bestDist) {
                    bestDist = dist[cur][j];
                    best = j;
                }
            }
            tour[i] = best;
            visited[best] = true;
        }
        return tour;
    }

    // ── 2-opt (melhoria local) ────────────────────────────────────────────────
    public int[] twoOpt(int[] tour) {
        int[] best = Arrays.copyOf(tour, n);
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 2; j < n; j++) {
                    if (j == n - 1 && i == 0) continue;
                    double d0 = dist[best[i]][best[i + 1]] + dist[best[j]][best[(j + 1) % n]];
                    double d1 = dist[best[i]][best[j]] + dist[best[i + 1]][best[(j + 1) % n]];
                    if (d1 < d0 - 1e-10) {
                        reverse(best, i + 1, j);
                        improved = true;
                    }
                }
            }
        }
        return best;
    }

    private void reverse(int[] a, int from, int to) {
        while (from < to) {
            int tmp = a[from];
            a[from] = a[to];
            a[to] = tmp;
            from++;
            to--;
        }
    }

    // ── Comprimento do tour ───────────────────────────────────────────────────
    public double tourLength(int[] tour) {
        double total = 0;
        for (int i = 0; i < n; i++)
            total += dist[tour[i]][tour[(i + 1) % n]];
        return total;
    }

    // ── Heurística completa: NN + 2-opt ───────────────────────────────────────
    public double solve() {
        int[] tour = nearestNeighbor(0);
        tour = twoOpt(tour);
        return tourLength(tour);
    }

    // ── Geração de instância aleatória (cidades em [0,1000]^2) ───────────────
    public static double[][] randomInstance(int n, Random rng) {
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
}

