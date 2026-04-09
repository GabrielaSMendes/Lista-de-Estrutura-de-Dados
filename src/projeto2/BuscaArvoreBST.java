package projeto2;

/**
 * Implementação de uma árvore binária de busca (BST) para inteiros, com inserção e busca.
 *
 * <p><b>Regra da BST:</b> em cada nó, o filho esquerdo (e toda sua subárvore) guarda valores
 * <em>menores</em> que o valor do nó; o filho direito guarda valores <em>maiores</em>.
 * Não há valores duplicados na árvore (inserir um valor já existente não altera a estrutura).
 *
 * <p><b>Como funciona a busca ({@link #contem(int)}):</b>
 * <ol>
 *   <li>Começa na raiz.</li>
 *   <li>Compara a chave procurada com o valor do nó atual.</li>
 *   <li>Se forem iguais, a chave existe — retorna {@code true}.</li>
 *   <li>Se a chave for menor, o próximo nó a visitar é o filho <em>esquerdo</em> (todos lá são menores que o atual).</li>
 *   <li>Se a chave for maior, passa-se ao filho <em>direito</em>.</li>
 *   <li>Se não houver filho nessa direção ({@code null}), a chave não está na árvore — retorna {@code false}.</li>
 * </ol>
 * A implementação usa um laço iterativo (sem recursão explícita) descendo da raiz até encontrar ou esgotar o caminho.
 *
 * <p><b>Complexidade da busca:</b> O(h), com h = altura. Em média, com inserções aleatórias, h é da ordem de log n;
 * no pior caso (inserção ordenada), a árvore degenera em lista e h = n.
 */
public final class BuscaArvoreBST {

    private static final class No {
        final int valor;
        No esquerda;
        No direita;

        No(int valor) {
            this.valor = valor;
        }
    }

    private No raiz;

    public void inserir(int valor) {
        raiz = inserir(raiz, valor);
    }

    private static No inserir(No no, int valor) {
        if (no == null) {
            return new No(valor);
        }
        if (valor < no.valor) {
            no.esquerda = inserir(no.esquerda, valor);
        } else if (valor > no.valor) {
            no.direita = inserir(no.direita, valor);
        }
        return no;
    }

    public boolean contem(int valor) {
        return buscar(raiz, valor) != null;
    }

    private static No buscar(No no, int valor) {
        while (no != null) {
            if (valor < no.valor) {
                no = no.esquerda;
            } else if (valor > no.valor) {
                no = no.direita;
            } else {
                return no;
            }
        }
        return null;
    }
}
