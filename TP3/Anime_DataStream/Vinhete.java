import java.io.RandomAccessFile;

public class Vinhete {
    // A chave usada para codificar o texto
    private static String chave;

    public static boolean setChave(String novaChave) throws Exception {
        chave = novaChave;
        RandomAccessFile arq = new RandomAccessFile("cifragem/animesVinheteCifra.db", "rw");

        String caminhoArquivoDb = Menu.enderecoDB;

        String stringParaCodificar = BaseString.gerarStringDaBaseDeAnimes(caminhoArquivoDb);

        // Chama a função vigenereCipher para codificar o texto e armazena o resultado
        // na variável encodedText
        String textoCodificado = cifraVigenere(stringParaCodificar);

        // Converte a string codificada em bytes
        byte[] bytes = textoCodificado.getBytes();

        // Escreve os bytes no arquivo de saída
        arq.write(bytes);
        return true;
    }

    public static String cifraVigenere(String texto) {
        // O alfabeto usado como referência
        String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        // Inicializa a string codificada como um StringBuilder (para eficiência)
        StringBuilder textoCodificado = new StringBuilder();

        // Indice da chave
        int indiceChave = 0;

        // Loop através de cada caractere no texto
        for (char charNoTexto : texto.toCharArray()) {
            // Se o caractere é uma letra
            if (Character.isLetter(charNoTexto)) {
                // Encontra a posição do caractere no alfabeto
                int posTexto = alfabeto.indexOf(Character.toUpperCase(charNoTexto));
                // Encontra a posição do caractere na chave
                int posChave = alfabeto.indexOf(chave.charAt(indiceChave % chave.length()));

                // Verifica se posTexto e posChave são diferentes de -1
                if (posTexto != -1 && posChave != -1) {
                    // Calcula a nova posição
                    int novaPos = (posTexto + posChave) % alfabeto.length();

                    // Adiciona o novo caractere à string codificada
                    textoCodificado.append(alfabeto.charAt(novaPos));

                    // Atualiza o índice da chave
                    indiceChave++;
                } else {
                    // Se o caractere não é uma letra ou não está no alfabeto, adiciona-o
                    // diretamente à string codificada
                    textoCodificado.append(charNoTexto);
                }
            } else {
                // Se o caractere não é uma letra, adiciona-o diretamente à string codificada
                textoCodificado.append(charNoTexto);
            }
        }

        // Retorna a string codificada
        return textoCodificado.toString();
    }
}