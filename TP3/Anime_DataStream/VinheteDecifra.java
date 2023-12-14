import java.io.RandomAccessFile;

public class VinheteDecifra {

    // A chave usada para decodificar o texto
    private static String chave;

    public static boolean setChave(String novaChave) throws Exception {
        chave = novaChave;
        RandomAccessFile arq = new RandomAccessFile("cifragem/animesVinheteCifra.db", "r");
        RandomAccessFile arqDecodificado = new RandomAccessFile("decifragem/animesVinheteDescripto.db", "rw");

        // Lê o conteúdo do arquivo codificado
        byte[] bytes = new byte[(int) arq.length()];
        arq.readFully(bytes);
        String stringParaDecodificar = new String(bytes);

        // Chama a função vigenereDecipher para decodificar o texto e armazena o
        // resultado na variável decodedText
        String textoDecodificado = cifraVigenereDecifrar(stringParaDecodificar, chave);

        // Converte a string decodificada em bytes
        byte[] bytesDecodificados = textoDecodificado.getBytes();

        // Escreve os bytes no arquivo de saída
        arqDecodificado.write(bytesDecodificados);
        return true;
    }

    public static String cifraVigenereDecifrar(String texto, String chave) {
        String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        chave = chave.toUpperCase();
        StringBuilder textoDecodificado = new StringBuilder();
        int indiceChave = 0;

        for (char charNoTexto : texto.toCharArray()) {
            if (Character.isLetter(charNoTexto)) {
                int posTexto = alfabeto.indexOf(Character.toUpperCase(charNoTexto));
                int posChave = alfabeto.indexOf(chave.charAt(indiceChave % chave.length()));

                // Verifica se posTexto e posChave são diferentes de -1
                if (posTexto != -1 && posChave != -1) {
                    int novaPos = (posTexto - posChave + alfabeto.length()) % alfabeto.length();
                    textoDecodificado.append(alfabeto.charAt(novaPos));
                    indiceChave++;
                } else {
                    // Se o caractere não é uma letra ou não está no alfabeto, adiciona-o
                    // diretamente à string decodificada
                    textoDecodificado.append(charNoTexto);
                }
            } else {
                textoDecodificado.append(charNoTexto);
            }
        }

        return textoDecodificado.toString();
    }
}