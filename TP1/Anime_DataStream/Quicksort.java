public class Quicksort {
    public void ordenaQuicksort(byte[] array, int tamanhoVetor) {
        ordenaQuicksort(array, 0, tamanhoVetor - 1);
    }

    // Ordenar o vetor por Quicksort
    public void ordenaQuicksort(byte[] array, int esq, int dir) {
        int i = esq, j = dir;

        byte pivo = array[(esq + dir) / 2];

        while (i <= j) {
            while (array[i] < pivo) {
                i++;
            }
            while (array[j] > pivo) {
                j--;
            }
            if (i <= j) {
                swapRegistro(array, i, j);
                i++;
                j--;
            }
        }

        if (esq < j) {
            ordenaQuicksort(array, esq, j);
        }
        if (i < dir) {
            ordenaQuicksort(array, i, dir);
        }
    }

    // Trocar os personagens de lugar
    public void swapRegistro(byte[] array, int i, int j) {
        byte registroTmp = array[i];
        array[i] = array[j];
        array[j] = registroTmp;
    }
}
