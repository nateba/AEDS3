import java.io.IOException;
import java.io.RandomAccessFile;

public class Testelzw {
    public static void main(String[] args) {

        LZW lzw = new LZW();
        try {
            long startTime = System.currentTimeMillis(); // tempo inicial
            lzw.comprimir("dados/animes.db", "arquivoComprimido.bin");
            long finishTime = System.currentTimeMillis(); // tempo inicial
            long tempoTotal = finishTime - startTime;
            System.out.println("Tempo de execucao: " + tempoTotal + "ms");

            LZW lzw2 = new LZW();
            long startTime2 = System.currentTimeMillis(); // tempo inicial
            lzw2.descomprimir("arquivoComprimido.bin", "animes2.db");
            long finishTime2 = System.currentTimeMillis(); // tempo inicial
            long tempoTotal2 = finishTime2 - startTime2;
            System.out.println("Tempo de execucao: " + tempoTotal2 + "ms");
            long tamanhoOriginal = new RandomAccessFile("dados/animes.db", "r").length();// tamanho do arquivo
            long tamanhoFinal = new RandomAccessFile("arquivoComprimido.bin", "r").length(); // tamanho do arquivo
            //
            float taxaCompressao = ((float) tamanhoFinal / tamanhoOriginal);
            float reducao = 100 * (1 - taxaCompressao); // percentual de reducao
            System.out.println("Percentual de reducao: " + reducao + "%");
        } catch (IOException e) {
            System.out.println("Erro ao comprimir o arquivo: " + e.getMessage());
            e.printStackTrace();
        }

    }
}
