public class HeapMinimo {
    private int capacidade;
    private int tamanho;
    private No[] heap;

    // Construtor do Heap
    public HeapMinimo(int capacidade) {
        this.capacidade = capacidade;
        this.tamanho = 0; // Checagem de quando o Heap estará cheio
        this.heap = new No[capacidade];
    }

    private int getIndexPai(int index) {
        return (index - 1) / 2;
    }

    private int getIndexFilhoEsquerdo(int index) {
        return 2 * index + 1;
    }

    private int getIndexFilhoDireito(int index) {
        return 2 * index + 2;
    }

    // Função para trocar os registros de lugar no Heap
    private void swapRegistro(int index1, int index2) {
        No temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    public void inserirRegistro(No no) {
        // Optamos por inserir o registro no final do Heap e ir 'subindo' com ele
        // recursivamente, ao invés de inseri-lo na raiz e afundá-lo no Heap
        heap[tamanho] = no;
        int indexAtual = tamanho;
        tamanho++;

        // A conferência precisa ocorrer em três etapas:
        // 1. Se a raiz já foi atingida;
        // 2. Se há divergência de segmentos;
        // 3. Se, dentro do mesmo segmento, qual o menor valor.
        while (indexAtual > 0 && (heap[indexAtual].getSegmento() < heap[getIndexPai(indexAtual)].getSegmento()
                || (heap[indexAtual].getSegmento() == heap[getIndexPai(indexAtual)].getSegmento()
                        && heap[indexAtual].getRegistro().getIdRegistro() < heap[getIndexPai(indexAtual)].getRegistro()
                                .getIdRegistro()))) {
            swapRegistro(indexAtual, getIndexPai(indexAtual));
            indexAtual = getIndexPai(indexAtual);
        }
    }

    public No retirarRaiz() {
        No raiz = heap[0];
        heap[0] = heap[tamanho - 1];
        tamanho--;

        heapify(0); // Refazer o Heap após ter a raiz retirada

        return raiz;
    }

    // Função que reorganiza o Heap após uma retirada de raiz
    private void heapify(int index) {
        int menorIndice = index;
        int indexFilhoEsquerdo = getIndexFilhoEsquerdo(index);
        int indexFilhoDireito = getIndexFilhoDireito(index);

        // A conferência precisa ocorrer em três etapas:
        // 1. Se o filho é menor;
        // 2. Se há divergência de segmentos;
        // 3. Se, dentro do mesmo segmento, qual o menor valor.
        if (indexFilhoEsquerdo < tamanho
                && (heap[indexFilhoEsquerdo].getSegmento() < heap[menorIndice].getSegmento()
                        || (heap[indexFilhoEsquerdo].getSegmento() == heap[menorIndice].getSegmento()
                                && heap[indexFilhoEsquerdo]
                                        .getRegistro()
                                        .getIdRegistro() < heap[menorIndice].getRegistro().getIdRegistro()))) {
            menorIndice = indexFilhoEsquerdo;
        }

        // A conferência precisa ocorrer em três etapas:
        // 1. Se o filho é menor;
        // 2. Se há divergência de segmentos;
        // 3. Se, dentro do mesmo segmento, qual o menor valor.
        if (indexFilhoDireito < tamanho
                && (heap[indexFilhoDireito].getSegmento() < heap[menorIndice].getSegmento()
                        || (heap[indexFilhoDireito].getSegmento() == heap[menorIndice].getSegmento()
                                && heap[indexFilhoDireito]
                                        .getRegistro()
                                        .getIdRegistro() < heap[menorIndice].getRegistro().getIdRegistro()))) {
            menorIndice = indexFilhoDireito;
        }

        // Caso tenha havido variação, rodar o 'swap'
        if (menorIndice != index) {
            swapRegistro(index, menorIndice);
            heapify(menorIndice);
        }
    }

    // Função para conferir se o Heap está cheio
    public boolean heapCheio() {
        return tamanho >= capacidade;
    }

    // Função para conferir se o Heap está vazio
    public boolean heapVazio() {
        return tamanho <= 0;
    }

    // Função para ver a raiz do Heap
    public No getRaiz() {
        return heap[0];
    }
}
