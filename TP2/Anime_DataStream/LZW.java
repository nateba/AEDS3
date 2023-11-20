import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class LZW {

    private HashMap<String, Integer> dicionario;
    private int dictSize;
    private byte[] buffer;
    private boolean bitEsq;
    private String[] strArray;

    LZW() {
        dicionario = new HashMap<>();
        dictSize = 256;
        buffer = new byte[3];
        bitEsq = true;
    }

    /* Métodos de compressão */
    public void comprimir(String inFileName, String outFileName) throws IOException {
        inicializaDicionario();
        compressFile(inFileName, outFileName);
    }

    private void inicializaDicionario() {
        dicionario = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dicionario.put((char) i + "", i);
        }
    }

    private void compressFile(String inFile, String outFile) throws IOException {
        String s = new String();
        byte inByte;
        RandomAccessFile in = null;
        RandomAccessFile out = null;
        try {
            in = new RandomAccessFile(inFile, "r");
            out = new RandomAccessFile(outFile, "rw");
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo");
            e.printStackTrace();
            return;
        }

        try {
            inByte = in.readByte();
            int i = (int) inByte;
            char ch;
            if (i < 0) {
                i += 256;
            }
            ch = (char) i;
            s += ch;

            // Loop que irá percorrer todo o arquivo comprimindo e salvando no arquivo de
            // saída
            while (true) {
                inByte = in.readByte();
                i = (int) inByte;

                if (i < 0) {
                    i += 256;
                }
                ch = (char) i;

                // Enquanto conter a string no dicionário, adiciona o próximo caractere
                if (dicionario.containsKey(s + ch)) {
                    s += ch;
                } else {
                    String str12Bit = to12bit(dicionario.get(s));

                    // Escreve os 12 bits no buffer e então salva no arquivo o buffer
                    if (bitEsq) {
                        buffer[0] = (byte) Integer.parseInt(str12Bit.substring(
                                0, 8), 2);
                        buffer[1] = (byte) Integer.parseInt(str12Bit.substring(8, 12)
                                + "0000", 2);
                    } else {
                        buffer[1] += (byte) Integer.parseInt(str12Bit.substring(0, 4), 2);
                        buffer[2] = (byte) Integer.parseInt(str12Bit.substring(4, 12), 2);
                        // Escreve efetivamente no texto
                        for (int j = 0; j < buffer.length; j++) {
                            out.writeByte(buffer[j]);
                            buffer[j] = 0;
                        }
                    }
                    bitEsq = !bitEsq;

                    // Adiciona a string no dicionário
                    if (dictSize < 4096) {
                        dicionario.put(s + ch, dictSize++);
                    }

                    // Reinicia a string com o último char lido
                    s = "" + ch;
                }
            }

        } catch (IOException e) {
            String str12Bit = to12bit(dicionario.get(s));
            if (bitEsq) {
                buffer[0] = (byte) Integer.parseInt(str12Bit.substring(0, 8), 2);
                buffer[1] = (byte) Integer.parseInt(str12Bit.substring(8, 12)
                        + "0000", 2);
                out.writeByte(buffer[0]);
                out.writeByte(buffer[1]);
            } else {
                buffer[1] += (byte) Integer.parseInt(str12Bit.substring(0, 4), 2);
                buffer[2] = (byte) Integer.parseInt(str12Bit.substring(4, 12), 2);

                // Escreve efetivamente no texto
                for (int j = 0; j < buffer.length; j++) {
                    out.writeByte(buffer[j]);
                    buffer[j] = 0;
                }
            }
        }
    }

    private String to12bit(int i) {
        String str = Integer.toBinaryString(i);
        while (str.length() < 12) {
            str = "0" + str;
        }
        return str;
    }

    /* Métodos de descompressão */
    public void descomprimir(String inFileName, String outFileName) throws IOException {
        inicializaDicionarioDescompress();
        decompressFile(inFileName, outFileName);
    }

    // Reinicializa o dicionário e o array de strings com os caracteres iniciais
    // ASCII
    private void inicializaDicionarioDescompress() {
        dicionario = new HashMap<>();
        strArray = new String[4096];
        dictSize = 256;
        for (int i = 0; i < dictSize; i++) {
            dicionario.put((char) i + "", i);
            strArray[i] = (char) i + "";
        }
    }

    private void decompressFile(String inFile, String outFile) throws IOException {
        RandomAccessFile in = null;
        RandomAccessFile out = null;
        int antWord = 0, currentWord = 0;
        try {
            in = new RandomAccessFile(inFile, "r");
            out = new RandomAccessFile(outFile, "rw");
        } catch (Exception e) {
            System.out.println("Erro ao abrir arquivo");
            e.printStackTrace();
        }
        try {
            // Leitura do primeiro caractere
            buffer[0] = in.readByte();
            buffer[1] = in.readByte();
            antWord = get12BitValue(buffer[0], buffer[1], bitEsq);
            bitEsq = !bitEsq;
            out.writeBytes(strArray[antWord]);

            while (true) {
                // Leitura do próximo caractere
                if (bitEsq) {
                    buffer[0] = in.readByte();
                    buffer[1] = in.readByte();
                    currentWord = get12BitValue(buffer[0], buffer[1], bitEsq);
                } else {
                    buffer[2] = in.readByte();
                    currentWord = get12BitValue(buffer[1], buffer[2], bitEsq);
                }
                bitEsq = !bitEsq;

                // Se o caractere não estiver no dicionário, adiciona ao dicionario
                // a string do caractere anterior concatenado com o primeiro caractere dela
                // mesma
                if (currentWord >= dictSize) {
                    // Apenas insere caso haja espaço no dicionário
                    if (dictSize < 4096) {
                        strArray[dictSize] = strArray[antWord] + strArray[antWord].charAt(0);
                    }
                    dictSize++;
                    out.writeBytes(strArray[antWord] + strArray[antWord].charAt(0));
                } else {
                    if (dictSize < 4096) {
                        // adiciona a string anterior concatenado com o primeiro caractere da nova
                        strArray[dictSize] = strArray[antWord] + strArray[currentWord].charAt(0);
                    }
                    dictSize++;
                    out.writeBytes(strArray[currentWord]);
                }
                antWord = currentWord;
            }
        } catch (Exception e) {
            in.close();
            out.close();
        }
    }

    // REMOVER O BOOLEANO
    // Lê os 12 bits do arquivo e retorna o inteiro correspondente
    private int get12BitValue(byte b1, byte b2, boolean bitEsq) {
        String s1 = Integer.toBinaryString(b1);
        String s2 = Integer.toBinaryString(b2);
        int retNum = 0;
        // Caso s1 tenha menos de 8 bits, completa com Zeros à esquerda
        while (s1.length() < 8) {
            s1 = "0" + s1;
        }
        // Caso s2 tenha menos de 8 bits, completa com Zeros à esquerda
        while (s2.length() < 8) {
            s2 = "0" + s2;
        }

        // Verifica se possuiem mais que 12 bits, e remove os da esquerda e seleciona
        // apenas os 8 ultimos
        if (s1.length() > 8) {
            s1 = s1.substring(s1.length() - 8);
        }
        if (s2.length() > 8) {
            s2 = s2.substring(s2.length() - 8);
        }

        if (bitEsq) {
            retNum = Integer.parseInt(s1 + s2.substring(0, 4), 2);
        } else {
            retNum = Integer.parseInt(s1.substring(4, 8) + s2, 2);
        }

        return retNum;
    }
}