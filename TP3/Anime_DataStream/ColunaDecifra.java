import java.io.RandomAccessFile;

public class ColunaDecifra {

    // A chave usada para decodificar o texto
    private static String chave;

    public static boolean setChave(String novaChave) throws Exception {
        chave = novaChave;
        RandomAccessFile arq = new RandomAccessFile("cifragem/animesColunaCifra.db", "r");
        RandomAccessFile arqDecodificado = new RandomAccessFile("decifragem/animesColunaDecifra.db", "rw");

        // Lê o conteúdo do arquivo codificado
        byte[] bytes = new byte[(int) arq.length()];
        arq.readFully(bytes);
        String stringParaDecodificar = new String(bytes);

        // Chama a função cifraColunarDecifrar para decodificar o texto e armazena o
        // resultado na variável textoDecodificado
        String textoDecodificado = cifraColunarDecifrar(stringParaDecodificar, chave);

        // Converte a string decodificada em bytes
        byte[] bytesDecodificados = textoDecodificado.getBytes();

        // Escreve os bytes no arquivo de saída
        arqDecodificado.write(bytesDecodificados);
        return true;
    }

    public static String cifraColunarDecifrar(String texto, String chave) {
        int[] ordem = getOrdem(chave);
        char[][] tabela = new char[chave.length()][(texto.length() + chave.length() - 1) / chave.length()];
        int indice = 0;

        for (int i = 0; i < ordem.length; i++) {
            for (int j = 0; j < tabela[0].length; j++) {
                if (indice < texto.length()) {
                    tabela[ordem[i] - 1][j] = texto.charAt(indice++);
                } else {
                    tabela[ordem[i] - 1][j] = ' ';
                }
            }
        }

        StringBuilder textoDecodificado = new StringBuilder();
        for (int j = 0; j < tabela[0].length; j++) {
            for (int i = 0; i < ordem.length; i++) {
                textoDecodificado.append(tabela[i][j]);
            }
        }

        return textoDecodificado.toString();
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