
# Lista de avaliação prática de Estrutura de Dados

Professor: Gabriel Passos

Integrantes do grupo:

- Caetano Mikulis - 24079424-2 
- Gabriela Mendes - 24034816-2 
- Gustavo Takeda - 24289842-2 
- Ryan do Vale - 24196534-2 
- Leonardo Sampaio - 24211831-2

### Instruções de execução

**Java:** JDK **21** (o projeto do Eclipse está configurado para Java SE 21).

**No Eclipse:** `File` → `Import…` → `General` → `Existing Projects into Workspace` → `Next` → em **Root directory** escolha a pasta raiz deste repositório → `Finish`. Use o botão **Run** com o projeto selecionado.

---

## Projeto 1 – Árvores e Balanceamento

### Descrição

Este projeto apresenta uma análise experimental comparando o desempenho de três árvores de busca binária (BST, AVL e rubro-negra na variante *left-leaning*) e, em separado, instâncias do problema do caixeiro viajante (TSP) resolvidas com heurística de vizinho mais próximo seguida de melhoria 2-opt.

### Estruturas e algoritmos implementados

- BST (árvore de busca binária)
- AVL (balanceamento por altura)
- Árvore rubro-negra (LLRB)
- TSP: vizinho mais próximo e refinamento 2-opt

### Metodologia

#### Experimentos com árvores

- Tamanhos de entrada: 1.000, 10.000 e 100.000 elementos  
- 30 execuções por estrutura e por tamanho, com chaves pseudoaleatórias (`Random(42)`, valores em `[0, n·10)`)  
- Em cada execução: tempo de inserção do conjunto completo; 1.000 buscas; 500 remoções com chaves aleatórias; altura final da árvore  

#### Experimento TSP

- Números de cidades: 50, 200 e 500  
- 30 execuções por tamanho; coordenadas aleatórias em `[0, 1000]²` e distâncias euclidianas entre todas as cidades  

### Métrica de Avaliação

- Tempos de inserção, busca e remoção em milissegundos (ms), obtidos com `System.nanoTime()` convertido para ms  
- Para o TSP: comprimento do tour e tempo de `solve()` (ms)  
- Estatísticas agregadas: média, desvio padrão amostral, mínimo e máximo  

## Projeto 2 - Sistemas de Busca

### Descrição

Este projeto compara experimentalmente o desempenho de três formas de busca sobre conjuntos ordenados: percorrimento sequencial em vetor, busca binária em vetor ordenado e busca em árvore BST construída a partir dos mesmos elementos. O programa mede o tempo de um lote fixo de operações de consulta para vários tamanhos de entrada.

### Métodos implementados

- Busca sequencial  
- Busca binária (vetor ordenado)  
- Busca em árvore BST (inserção dos valores em ordem embaralhada antes da medição, para evitar degeneração em lista)  

### Metodologia

O benchmark utiliza, para cada tamanho `n`:

#### Preparação dos dados

- Vetor ordenado com `n` inteiros distintos gerados aleatoriamente e ordenados  
- Chaves de busca alternando entre valores presentes e ausentes no vetor  
- Para a fase BST: a árvore é preenchida antes da medição, com os mesmos elementos em ordem aleatória  

#### Parâmetros (configuráveis no console)

- Repetições por tamanho (padrão: 30) para média e desvio  
- Buscas por medição — tamanho do lote cronometrado (padrão: 10.000)  
- Lista de tamanhos `n` (padrão: 1.000, 5.000, 10.000, 20.000, 30.000 e 50.000)  
- Semente numérica para reprodutibilidade (ou valor aleatório a cada execução)  

### Métrica de Avaliação

- Duração total do lote de buscas medida com `System.nanoTime()`, exibida em milissegundos (ms) com média e desvio padrão  
- Ao fim de cada fase, regressão linear do tempo em função de `n` (busca sequencial) ou de `log₂(n)` (busca binária e BST), para confronto com `O(n)` e `O(log n)`  

## Projeto 3 - Benchmark de Ordenação

### Descrição
Este projeto tem como objetivo analisar o desempenho de diferentes algoritmos de ordenação por meio de um benchmark computacional. Foram realizados testes com diferentes tamanhos de entrada e cenários, permitindo comparar a eficiência de cada algoritmo.

### Algoritmos Implementados

- Bubble Sort
- Insertion Sort
- Quick Sort
- Merge Sort

### Metodologia

O programa executa testes de desempenho considerando:

#### Tamanhos de entrada
- 1.000 elementos  
- 5.000 elementos  
- 10.000 elementos  

#### Cenários testados
- Vetor aleatório  
- Vetor ordenado  
- Vetor inverso  

Cada algoritmo é executado **30 vezes** por combinação (tamanho × cenário), sempre a partir de uma **cópia** do vetor de entrada, para comparação justa entre métodos e entre execuções.

### Métrica de Avaliação

- Duração medida com `System.nanoTime()` em cada uma das 30 execuções; reportam-se **média** e **desvio padrão amostral** (mesma convenção de `projeto2.BenchmarkBusca`), em milissegundos e em nanossegundos.


