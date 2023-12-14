import java.io.RandomAccessFile;

public class CifraColuna {
    // A chave usada para codificar o texto
    private static String chave;

    public boolean setChave(String novaChave) throws Exception {
        chave = novaChave;
        RandomAccessFile arq = new RandomAccessFile("cifragem/animesColunaCifra.db", "rw");

        String caminhoArquivoDb = Menu.enderecoDB;

        String stringParaCodificar = BaseString.gerarStringDaBaseDeAnimes(caminhoArquivoDb);

        // Chama a função cifraColunar para codificar o texto e armazena o resultado na
        // variável textoCodificado
        String textoCodificado = cifraColunar(stringParaCodificar);

        // Converte a string codificada em bytes
        byte[] bytes = textoCodificado.getBytes();

        // Escreve os bytes no arquivo de saída
        arq.write(bytes);
        return true;
    }

    public static String cifraColunar(String texto) {
        int[] ordem = getOrdem(chave);
        StringBuilder textoCodificado = new StringBuilder();

        for (int i = 0; i < ordem.length; i++) {
            for (int j = ordem[i] - 1; j < texto.length(); j += chave.length()) {
                textoCodificado.append(texto.charAt(j));
            }
        }

        return textoCodificado.toString();
    }

    private static int[] getOrdem(String chave) {
        int[] ordem = new int[chave.length()];
        char[] arrayChave = chave.toCharArray();

        for (int i = 0; i < chave.length(); i++) {
            int posicao = 1;
            for (int j = 0; j < chave.length(); j++) {
                if (arrayChave[j] < arrayChave[i] || (arrayChave[j] == arrayChave[i] && j < i)) {
                    posicao++;
                }
            }
            ordem[i] = posicao;
        }

        return ordem;
    }
}