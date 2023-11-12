import java.io.*;

public class BaseString {
    public static String gerarStringDaBaseDeAnimes(String enderecoDB) throws Exception {
        // Abrir o arquivo ANIMES.DB somente para leitura
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        byte[] ba;
        int idAnime;
        int len;
        boolean lapis;

        long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início

        int maiorId = arq.readInt();

        String registrosString = new String();
        registrosString += "" + maiorId;

        // Enquanto não chegar no fim do arquivo, a repetição acontece
        while (ponteiroBase < arq.length()) {
            idAnime = arq.readInt();
            lapis = arq.readBoolean();
            len = arq.readInt();

            ba = new byte[len];
            arq.read(ba);

            // Gerar o objeto Registro
            Registro registro = new Registro();
            registro.fromByteArray(idAnime, lapis, len, ba);

            ponteiroBase = arq.getFilePointer();

            // Encaixar todos os registros na mesma string
            registrosString += registro.toStringCompressao(ba);
        }

        // Fechar o arquivo
        arq.close();

        return registrosString;
    }

    public static long getTamanhoArquivoOriginal(String enderecoDB) throws Exception {
        // Abrir o arquivo ANIMES.DB somente para leitura
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        long tamanhoArquivoOriginal = arq.length();

        arq.close();

        // Retornar o tamanho do arquivo original em bytes
        return tamanhoArquivoOriginal;
    }

    public static StringBuilder gerarStringBaseComprimida(int versaoArquivo) throws Exception {
        // Abrir o arquivo ANIMES.DB somente para leitura
        RandomAccessFile arq = new RandomAccessFile("compressao/animesHuffmanCompressao" + versaoArquivo + ".db", "r");

        long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início

        StringBuilder stringBinaria = new StringBuilder();

        // Enquanto não chegar no fim do arquivo, a repetição acontece
        // Desconsiderar o último byte, pois ele pode ter somente alguns bits úteis
        while (ponteiroBase < arq.length() - 1) {
            byte byteComprimido = arq.readByte();

            for (int i = 7; i >= 0; i--) {
                byte valorByte = (byte) ((byteComprimido >> i) & 1);
                stringBinaria.append(valorByte);
            }

            ponteiroBase = arq.getFilePointer();
        }

        // Tratar o último byte separadamente
        byte ultimoByte = arq.readByte();

        StringBuilder ultimaByteString = new StringBuilder();

        for (int i = 7; i >= 0; i--) {
            byte valorByte = (byte) ((ultimoByte >> i) & 1);

            ultimaByteString.append(valorByte);
        }

        // Fazer o teste para o caso de haver um arquivo com numero de bits múltiplos de
        // oito, não perdendo informações
        if (Compressao.bitsRestantesHuffman != 0) {
            for (int i = 0; i <= 7; i++) {
                if (i > 7 - Compressao.bitsRestantesHuffman) {
                    stringBinaria.append(ultimaByteString.charAt(i));
                }
            }
        } else {
            stringBinaria.append(ultimaByteString);
        }

        // Fechar o arquivo
        arq.close();

        return stringBinaria;
    }
}
