import java.util.*;

public class HuffmanCoding {
    public Map<Character, String> construirArvoreHuffman(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        // Calcular a frequência de cada caractere na string dos 'animes.db'
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Calcular a prioridade dos nós na fila da árvore
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        // Construir a árvore de Huffman
        while (priorityQueue.size() > 1) {
            HuffmanNode left = priorityQueue.poll();
            HuffmanNode right = priorityQueue.poll();

            HuffmanNode mergedNode = new HuffmanNode('\0', left.frequency + right.frequency);
            mergedNode.left = left;
            mergedNode.right = right;

            priorityQueue.add(mergedNode);
        }

        // Gerar a 'tabela' com os códigos de Huffman que serão usados posteriormente
        Map<Character, String> codigosHuffman = new HashMap<>();
        gerarCodigos(priorityQueue.peek(), "", codigosHuffman);

        return codigosHuffman;
    }

    public void gerarCodigos(HuffmanNode raiz, String codigo, Map<Character, String> codigosHuffman) {
        if (raiz == null) {
            return;
        }

        if (raiz.left == null && raiz.right == null) {
            codigosHuffman.put(raiz.data, codigo);
        }

        gerarCodigos(raiz.left, codigo + "0", codigosHuffman);
        gerarCodigos(raiz.right, codigo + "1", codigosHuffman);
    }

    public String codificar(String input, Map<Character, String> codigosHuffman) {
        StringBuilder stringCodificada = new StringBuilder();
        for (char c : input.toCharArray()) {
            stringCodificada.append(codigosHuffman.get(c));
        }
        return stringCodificada.toString();
    }

    public String decodificar(String stringCodificada, Map<Character, String> codigosHuffman) {
        StringBuilder stringDecodificada = new StringBuilder();
        int index = 0;

        while (index < stringCodificada.length()) {
            for (Map.Entry<Character, String> entry : codigosHuffman.entrySet()) {
                String code = entry.getValue();
                if (index + code.length() <= stringCodificada.length() &&
                        stringCodificada.substring(index, index + code.length()).equals(code)) {
                    stringDecodificada.append(entry.getKey());
                    index += code.length();
                    break;
                }
            }
        }

        return stringDecodificada.toString();
    }
}
