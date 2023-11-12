import java.io.*;
import java.util.*;

public class Compressao {
    // As variáveis com os tamanhos dos arquivos serão colocadas como globais para
    // serem acessadas por funções de diferentes escopos
    long tamanhoArquivoOriginal = 0;
    long tamanhoArquivoHuffman = 0;
    long tamanhoArquivoLZW = 0;

    String caminhoArquivoDb = Menu.enderecoDB;

    static int versaoArquivo = 1;

    static int bitsRestantesHuffman = 0;

    // Variáveis para cálculo de tempo de execução
    long tempoInicio = 0;
    long tempoFinalHuffman = 0;

    // Chamar as funções para comprimir arquivos por Huffman e LZW
    public void comprimirArquivo() throws Exception {
        // Armazenar o tempo em que a compressão iniciou
        tempoInicio = System.nanoTime();

        // Pegar a string que será comprimida
        String stringParaComprimir = BaseString.gerarStringDaBaseDeAnimes(caminhoArquivoDb);

        // Pegar o tamanho do arquivo original para cálculo
        tamanhoArquivoOriginal = BaseString.getTamanhoArquivoOriginal(caminhoArquivoDb);

        // Gerar o arquivo pelo algoritmo de Huffman
        gerarArquivoComprimidoHuffman(stringParaComprimir);

        // Gerar o arquivo pelo algoritmo de LZW
        // [IMPLEMENTAR]

        // Calcular a taxa de compressão dos algoritmos e comparar ambos
        calculaTaxaCompressao();

        // Calcular o tempo de execução dos algoritmos e comparar ambos
        calculaTempoCompressao();
    }

    public void gerarArquivoComprimidoHuffman(String registros) throws IOException {
        // Abrir o arquivo ANIMES.DB para leitura e escrita
        RandomAccessFile arq = new RandomAccessFile("compressao/animesHuffmanCompressao" + versaoArquivo + ".db", "rw");

        HuffmanCoding huffmanCoding = new HuffmanCoding();

        Map<Character, String> huffmanCodes = huffmanCoding.construirArvoreHuffman(registros);

        String stringCodificada = huffmanCoding.codificar(registros, huffmanCodes);

        // Calcular a possibilidade de bytes do arquivo
        bitsRestantesHuffman = stringCodificada.length() % 8;

        // Rodar por toda a string codificada em mod 8
        for (int i = 0; i < stringCodificada.length() - bitsRestantesHuffman; i += 8) {
            byte byteCodificado = 0;

            // Transformar os caracteres codificados em bytes
            for (int j = i; j < i + 8; j++) {
                // Operação para empurrar um bit para a esquerda e colocar nele o valor recebido
                byteCodificado = (byte) (byteCodificado << 1 | Character.getNumericValue(stringCodificada.charAt(j)));
            }

            // Escrever byte a byte compactado no arquivo .db
            arq.write(byteCodificado);
        }

        // Testar se existem bits sobrando na string codificada
        if (bitsRestantesHuffman != 0) {
            // Rodar pelos bits restantes da string codificada
            byte ultimoByte = 0;

            // Colocar os caracteres restantes no último byte
            for (int i = stringCodificada.length() - bitsRestantesHuffman; i < stringCodificada.length(); i++) {
                ultimoByte = (byte) (ultimoByte << 1 | Character.getNumericValue(stringCodificada.charAt(i)));
            }

            // Escrever o último byte com os bits restantes no arquivo .db
            arq.write(ultimoByte);
        }

        tamanhoArquivoHuffman = arq.length();

        arq.close();

        tempoFinalHuffman = System.nanoTime();
    }

    // Calcular as taxas de compressão com ambos os algoritmos e exibir para o
    // usuário a porcentagem em relação ao tamanho original do arquivo compactado
    public void calculaTaxaCompressao() {
        System.out.println("\nTAXAS DE COMPRESSÃO");
        System.out.println("Tamanho do arquivo original: " + tamanhoArquivoOriginal + " bytes");
        System.out.println("Tamanho do arquivo compactado Huffman: " + tamanhoArquivoHuffman + " bytes");

        System.out
                .println("Taxa de Compressão Huffman: " + (float) tamanhoArquivoHuffman / tamanhoArquivoOriginal * 100
                        + "%");

        // Aumentar a versão do arquivo, caso o usuário queira compactar mais de uma vez
        versaoArquivo++;
    }

    // Calcular o tempo de execução de cada um dos algoritmos e exibir para o
    // usuário
    public void calculaTempoCompressao() {
        long tempoExecucaoHuffman = (tempoFinalHuffman - tempoInicio) / 1000000; // Converter para milisegundos

        System.out.println("\nTEMPO DE EXECUÇÃO");
        System.out.println("Tempo de execução da compressão em Huffman: " + tempoExecucaoHuffman + " milissegundos");
    }

    // Fazer uma comparação entre os algoritmos Huffman e LZW e mostrar para o
    // usuário qual se saiu melhor, tanto em tamanho do arquivo compactado quanto em
    // tempo de execução
    public void compararAlgoritmosCompressao() {

    }
}