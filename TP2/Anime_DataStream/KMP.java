public class KMP {
    int numeroComparacoes = 0;

    // Função principal que realiza a pesquisa usando o algoritmo KMP
    public boolean KMPSearch(String pat, String txt) {
        int M = pat.length(); // Tamanho do padrão
        int N = txt.length(); // Tamanho do texto de pesquisa

        System.out.print("Padrão Procurado: \t");

        for (int p = 0; p < pat.length(); p++) {
            System.out.print(pat.charAt(p) + " ");
        }
        System.out.println();

        boolean encontrado = false;

        int[] lps = new int[M]; // Array para armazenar os valores do maior prefixo sufixo
        int j = 0;

        computeLPSArray(pat, M, lps); // Pré-processamento para calcular o array LPS

        int i = 0;
        while (N - i >= M - j) {
            if (pat.charAt(j) == txt.charAt(i)) {
                i++;
                j++;

                numeroComparacoes++;
            }

            if (j == M) {
                // Se o padrão é encontrado, imprime o índice onde a correspondência começa
                System.out.println("Padrão encontrado na posição: [" + (i - j) + "]");
                encontrado = true;
                j = lps[j - 1]; // Retorna j para continuar a pesquisa de outras ocorrências

                numeroComparacoes++;
            } else if (i < N && pat.charAt(j) != txt.charAt(i)) {
                // Se houver uma não correspondência, ajusta j usando o array LPS
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }

                numeroComparacoes++;
            }
        }

        System.out.println("\nNúmero de Comparações: " + numeroComparacoes);

        return encontrado;
    }

    // Função para calcular o array LPS (Longest Prefix Suffix)
    public void computeLPSArray(String pat, int M, int[] lps) {
        int len = 0;
        lps[0] = 0;
        int i = 1;

        while (i < M) {
            if (pat.charAt(i) == pat.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                // Se houver uma não correspondência, ajusta len usando o array LPS
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        printArray(lps); // Imprime o array LPS calculado
    }

    // Função para imprimir um array de inteiros
    public void printArray(int[] arr) {
        System.out.print("Vetor de Transição: \t");

        for (int value : arr) {
            System.out.print(value + " ");
        }

        System.out.println();
    }
}