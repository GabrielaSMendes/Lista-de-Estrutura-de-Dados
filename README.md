# Projeto — Estruturas de Dados e Algoritmos

Comparativo experimental entre **BST**, **AVL**, **árvore rubro-negra (LLRB)** e heurística **TSP** (vizinho mais próximo + 2-opt).

## Estrutura do projeto

Os ficheiros com `package trees` ou `package tsp` têm de estar nas pastas com o mesmo nome (requisito do Java).

```
Projeto1Estrutura/
├── Main.java              ← programa principal (pacote default)
├── TrabalhoAD.java        ← versão monolítica opcional (sem pacotes)
├── trees/
│   ├── BST.java
│   ├── AVL.java
│   └── RedBlack.java      ← LLRB alinhada a Sedgewick & Wayne (Algs4)
└── tsp/
    └── TSP.java
```

## Compilar e executar (pacotes)

No Windows, use **UTF-8** na compilação (há acentos e símbolos em `Main.java`).

```powershell
cd caminho\para\Projeto1Estrutura
javac -encoding UTF-8 -d out trees/BST.java trees/AVL.java trees/RedBlack.java tsp/TSP.java Main.java
java -Dfile.encoding=UTF-8 -cp out Main
```

Se o terminal não mostrar acentos corretamente, execute `chcp 65001` antes do `java`, ou use o `TrabalhoAD.java` (menos dependente do encoding do consola).

## Opção alternativa — ficheiro único

```powershell
javac -encoding UTF-8 TrabalhoAD.java
java -Dfile.encoding=UTF-8 TrabalhoAD
```

## O que está implementado

### Árvores

| Estrutura   | Inserção / busca / remoção | Altura (garantia) |
|------------|----------------------------|-------------------|
| BST        | O(h) no pior caso          | O(n) no pior caso; com dados aleatórios costuma ~O(log n) |
| AVL        | O(log n)                   | O(log n) |
| Rubro-negra (LLRB) | O(log n)           | O(log n) |

- **AVL:** fator de balanceamento em cada nó; rotações simples e duplas.
- **Rubro-negra:** variante **left-leaning** (Sedgewick & Wayne): rotações devolvem a nova raiz da subárvore, sem campo `parent` nas operações de inserção/remoção.

### Caixeiro viajante (TSP)

1. **Vizinho mais próximo** — construção do tour, O(n²).
2. **2-opt** — melhoria local até não haver ganho, O(n²) por passagem completa (várias passagens possíveis).

Instâncias: coordenadas uniformes em [0, 1000]²; matriz de distâncias euclidianas (`TSP.randomInstance`).

### Experiments em `Main`

- **Árvores:** n ∈ {1 000, 10 000, 100 000}; para cada n, **30** corridas com chaves `Random(42)` e `nextInt(n*10)`.
- **TSP:** n ∈ {50, 200, 500} cidades; **30** corridas com `Random(r * 1337L)`.
- **Métricas:** tempo de inserção total (ms), 1000 buscas, 500 remoções (chaves aleatórias do array), altura final; para o TSP, comprimento do tour e tempo de `solve()` (ms).
- **Estatísticas:** média, desvio padrão amostral, mínimo e máximo.
