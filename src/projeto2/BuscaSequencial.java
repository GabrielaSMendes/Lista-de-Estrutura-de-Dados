package projeto2;

/**
 * Busca sequencial (linear) em um vetor de inteiros.
 *
 * <p><b>Como funciona ({@link #buscar(int[], int)}):</b> percorre o vetor da posição {@code 0} até {@code length-1},
 * comparando cada elemento com a chave. No primeiro índice em que {@code a[i] == chave}, devolve {@code i}.
 * Se o laço terminar sem igualdade, a chave não existe — retorna {@code -1}.
 *
 * <p>Não exige que o vetor esteja ordenado. No pior caso visita todos os {@code n} elementos — O(n) comparações.
 */
public final class BuscaSequencial {

    private BuscaSequencial() {
    }

    /**
     * @return índice da chave ou -1 se ausente
     */
    public static int buscar(int[] a, int chave) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == chave) {
                return i;
            }
        }
        return -1;
    }
}
