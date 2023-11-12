import java.io.*;
import java.util.Map;

public class Descompressao {
    String caminhoArquivoDb = Menu.enderecoDB;

    // Variáveis para cálculo de tempo de execução
    long tempoInicio = 0;
    long tempoFinalHuffman = 0;

    // Chamar as funções para descompactar arquivos por Huffman e LZW
    public void descomprimirArquivo(int versao) throws Exception {
        // Armazenar o tempo em que a descompressão iniciou
        tempoInicio = System.nanoTime();

        // Pegar a string que foi gerada pela compressão
        StringBuilder stringParaComprimir = BaseString.gerarStringBaseComprimida(versao);

        // Descomprimir o arquivo pelo algoritmo de Huffman
        gerarArquivoDescomprimidoHuffman(stringParaComprimir);

        // Descomprimir o arquivo pelo algoritmo de LZW
        // [IMPLEMENTAR]

        // Calcular a taxa de compressão dos algoritmos e comparar ambos
        calculaTempoDescompressao();
    }

    private void gerarArquivoDescomprimidoHuffman(StringBuilder stringBinaria) throws Exception {
        // Passar o endereço da base de dados
        RandomAccessFile arq = new RandomAccessFile("descompressao/animes_dscmp.db", "rw");

        HuffmanCoding huffmanCoding = new HuffmanCoding();

        String stringBase = BaseString.gerarStringDaBaseDeAnimes(caminhoArquivoDb);

        // Reconstruir a árvore de Huffman para pegar a tabela de códigos
        Map<Character, String> codigosHuffman = huffmanCoding.construirArvoreHuffman(stringBase);

        String stringComFlag = huffmanCoding.decodificar(stringBinaria.toString(), codigosHuffman);

        /*
         * JUSTIFICATIAV DA DECISÃO:
         * 
         * Optamos por inserir uma flag entre cada atributo do arquivo compactado
         * original, pois na conversão de números (int, float e long) para string,
         * alguns bytes estavam sendo extrapolados e afetando a checagem de metadados
         * 
         * A flag utilizada para separar os atributos foi o elemento '{'
         * 
         * Com a separação, foi possível separar os elementos novamente e reconstruir a
         * base de dados com os metadados originais de tipos dos bytes
         * 
         * Antes da separação, o algoritmo Huffman apresentava uma taxa de compressão em
         * torno de 60% e depois da separação ele apresentou uma taxa de compressão de
         * 66%
         * 
         * A diferença entre o tamanho de ambos foi julgada como não tão significativa
         * nessa situação, de modo que compensou o acréscimo de um caractere como flag
         * de divisão
         */
        String[] elementosRegistros = stringComFlag.split("\\{");

        arq.writeInt(Integer.parseInt(elementosRegistros[0]));

        for (int i = 1; i < elementosRegistros.length; i++) {
            arq.writeInt(Integer.parseInt(elementosRegistros[i])); // ID do REGISTRO
            i++;
            arq.writeBoolean(Boolean.parseBoolean(elementosRegistros[i])); // Lápide do REGISTRO
            i++;
            arq.writeInt(Integer.parseInt(elementosRegistros[i])); // Tamanho do REGISTRO
            i++;
            arq.writeInt(Integer.parseInt(elementosRegistros[i])); // ID do ANIME
            i++;
            arq.writeUTF(elementosRegistros[i]); // Nome do ANIME
            i++;
            arq.writeFloat(Float.parseFloat(elementosRegistros[i])); // Score do ANIME
            i++;
            arq.writeUTF(elementosRegistros[i]); // Genre do ANIME
            i++;
            arq.writeUTF(elementosRegistros[i]); // Type do ANIME
            i++;
            arq.writeInt(Integer.parseInt(elementosRegistros[i])); // Episodes do ANIME
            i++;
            arq.writeLong(Long.parseLong(elementosRegistros[i]));
        }

        arq.close();

        tempoFinalHuffman = System.nanoTime();
    }

    // Calcular o tempo gasto na execução dos algoritmos de descompressão
    public void calculaTempoDescompressao() {
        long tempoExecucaoHuffman = (tempoFinalHuffman - tempoInicio) / 1000000; // Converter para milisegundos

        System.out.println("\nTEMPO DE EXECUÇÃO");
        System.out.println("Tempo de execução da descompressão em Huffman: " + tempoExecucaoHuffman + " milissegundos");
    }

    // Fazer uma comparação entre os algoritmos Huffman e LZW e mostrar para o
    // usuário qual se saiu melhor em tempo de execução
    public void compararAlgoritmosDescompressao() {

    }
}