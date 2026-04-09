package projeto2;

/**
 * Busca binária em vetor ordenado (ordem não decrescente: menor para maior).
 *
 * <p><b>Pré-condição:</b> o array {@code a} deve estar ordenado; caso contrário o resultado não é confiável.
 *
 * <p><b>Como funciona ({@link #buscar(int[], int)}):</b> mantém um intervalo {@code [lo, hi]} onde a chave pode estar.
 * Calcula o índice médio {@code mid}; compara a chave com {@code a[mid]}.
 * Se a chave for menor, descarta a metade direita ({@code hi = mid - 1}); se for maior, descarta a esquerda ({@code lo = mid + 1});
 * se for igual, retorna {@code mid}. Repete até encontrar ou até o intervalo ficar vazio ({@code lo > hi}) — então retorna {@code -1}.
 *
 * <p>Cada passo reduz o intervalo pela metade — O(log n) comparações no pior caso.
 */
public final class BuscaBinaria {

    private BuscaBinaria() {
    }

    /**
     * @return índice da chave ou -1 se ausente
     */
    public static int buscar(int[] a, int chave) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int cmp = Integer.compare(chave, a[mid]);
            if (cmp < 0) {
                hi = mid - 1;
            } else if (cmp > 0) {
                lo = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
}
