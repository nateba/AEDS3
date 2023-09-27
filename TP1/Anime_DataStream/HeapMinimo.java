public class HeapMinimo {
    private int capacidade;
    private int tamanho;
    private No[] heap;

    public HeapMinimo(int capacidade) {
        this.capacidade = capacidade;
        this.tamanho = 0;
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

    private void swapRegistro(int index1, int index2) {
        No temp = heap[index1];
        heap[index1] = heap[index2];
        heap[index2] = temp;
    }

    public void inserirRegistro(No no) {
        heap[tamanho] = no;
        int indexAtual = tamanho;
        tamanho++;

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

        heapify(0);

        return raiz;
    }

    private void heapify(int index) {
        int menorIndice = index;
        int indexFilhoEsquerdo = getIndexFilhoEsquerdo(index);
        int indexFilhoDireito = getIndexFilhoDireito(index);

        if (indexFilhoEsquerdo < tamanho
                && (heap[indexFilhoEsquerdo].getSegmento() < heap[menorIndice].getSegmento()
                        || (heap[indexFilhoEsquerdo].getSegmento() == heap[menorIndice].getSegmento()
                                && heap[indexFilhoEsquerdo]
                                        .getRegistro()
                                        .getIdRegistro() < heap[menorIndice].getRegistro().getIdRegistro()))) {
            menorIndice = indexFilhoEsquerdo;
        }

        if (indexFilhoDireito < tamanho
                && (heap[indexFilhoDireito].getSegmento() < heap[menorIndice].getSegmento()
                        || (heap[indexFilhoDireito].getSegmento() == heap[menorIndice].getSegmento()
                                && heap[indexFilhoDireito]
                                        .getRegistro()
                                        .getIdRegistro() < heap[menorIndice].getRegistro().getIdRegistro()))) {
            menorIndice = indexFilhoDireito;
        }

        if (menorIndice != index) {
            swapRegistro(index, menorIndice);
            heapify(menorIndice);
        }
    }

    public boolean heapCheio() {
        return tamanho >= capacidade;
    }

    public boolean heapVazio() {
        return tamanho <= 0;
    }

    public No getRaiz() {
        return heap[0];
    }
}
