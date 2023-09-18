import java.io.*;
import java.util.*;

class ComparadorPorId implements Comparator<Registro> {
    @Override
    public int compare(Registro r1, Registro r2) {
        return Integer.compare(r1.getIdRegistro(), r2.getIdRegistro());
    }
}

public class IntercalacaoSimples {
    // pega 12000 registros
    // cria arquivo tmp1
    // cria arquivo tmp2

    // puxa 100 pra memoria principal
    // ordena
    // volta pro arquivo tmp1 com eles ordenados

    // puxa 100 pra memoria principal
    // ordena
    // volta pro arquivo tmp2 com eles ordenados

    // faz isso ate acabar o arquivo de 12k

    // faz o merge dos dois arquivos tmp1 e tmp2 jogando no tmp3 e no tmp4
    // cria o tmp3 e o tmp4

    // quando sobrar só 1 arquivo ta tudo ordenado
    int arqTmp = 2;
    int bloco = 100;

    int qtdRegistros = 0;

    public void intercalacaoBalanceada() {
        qtdRegistros = contaRegistros("dados/animes.db");

        distribuicao("dados/animes.db");

        // intercalacao();
    }

    public int contaRegistros(String enderecoDB) {
        int totalRegistros = 0;

        // Leitura dos registros
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r")) {
            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            while (ponteiroBase < arq.length()) {
                arq.readInt(); // Lê o ID

                boolean lapide = arq.readBoolean();

                if (lapide == false) {
                    totalRegistros++;
                }

                int tamanhoRegistro = arq.readInt();

                ponteiroBase = arq.getFilePointer();

                arq.seek(ponteiroBase + tamanhoRegistro);

                ponteiroBase = arq.getFilePointer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return totalRegistros;
    }

    public void distribuicao(String enderecoDB) {
        try {
            RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r"); // Abre arquivo para leitura e escrita
            RandomAccessFile[] tmps = new RandomAccessFile[arqTmp];

            for (int i = 0; i < arqTmp; i++) {
                tmps[i] = new RandomAccessFile("tmps/tmp" + (i + 1) + ".db", "rw");
            }

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            int contadorRegistros = 0;

            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            int contadorTmp = 0;

            ArrayList<Registro> registros = new ArrayList<Registro>();

            while (ponteiroBase < arq.length()) {
                idRegistro = arq.readInt();
                lapis = arq.readBoolean();
                len = arq.readInt();

                ba = new byte[len];
                arq.read(ba);

                if (lapis == false) {
                    Registro registro = new Registro();
                    registro.fromByteArray(idRegistro, lapis, len, ba);

                    registros.add(registro);

                    contadorRegistros++;
                }

                if (contadorRegistros == bloco) {
                    Collections.sort(registros, new ComparadorPorId());

                    for (Registro registro : registros) {
                        tmps[contadorTmp % arqTmp].seek(tmps[contadorTmp % arqTmp].length());
                        tmps[contadorTmp % arqTmp].writeInt(registro.getIdRegistro());
                        tmps[contadorTmp % arqTmp].writeBoolean(registro.isLapide());
                        tmps[contadorTmp % arqTmp].writeInt(registro.getTamanho());
                        tmps[contadorTmp % arqTmp].write(registro.getAnime().toByteArray());
                    }

                    contadorTmp++;
                    contadorRegistros = 0;
                    registros.clear();
                }

                ponteiroBase = arq.getFilePointer();
            }

            // escreverArquivoTXT("tmps/tmp1.db", "tmps/txt1.txt");
            // escreverArquivoTXT("tmps/tmp2.db", "tmps/txt2.txt");

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void intercalacao(String enderecoDB) {
        try {
            RandomAccessFile[] tmps = new RandomAccessFile[arqTmp * 2];

            for (int i = 0; i < arqTmp * 2; i++) {
                tmps[i] = new RandomAccessFile("tmps/tmp" + (i + 1) + ".db", "rw");
            }

            ArrayList<Registro>[] matrizTemporarios = new ArrayList[arqTmp];

            int numeroRodada = 1; // Número da intercalação

            long[] ponteiroBase = new long[arqTmp * 2];

            for (int i = 0; i < bloco; i++) {
                if (numeroRodada % 2 == 0) {

                } else {

                }

                long ponteiroBase = tmps[0].getFilePointer();
                tmps[0].seek(i);
            }

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            int contadorRegistros = 0;

            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            int contadorTmp = 0;

            ArrayList<Registro> registros = new ArrayList<Registro>();

            while (ponteiroBase < arq.length()) {
                idRegistro = arq.readInt();
                lapis = arq.readBoolean();
                len = arq.readInt();

                ba = new byte[len];
                arq.read(ba);

                if (lapis == false) {
                    Registro registro = new Registro();
                    registro.fromByteArray(idRegistro, lapis, len, ba);

                    registros.add(registro);

                    contadorRegistros++;
                }

                if (contadorRegistros == bloco) {
                    Collections.sort(registros, new ComparadorPorId());

                    for (Registro registro : registros) {
                        tmps[contadorTmp % arqTmp].seek(tmps[contadorTmp % arqTmp].length());
                        tmps[contadorTmp % arqTmp].writeInt(registro.getIdRegistro());
                        tmps[contadorTmp % arqTmp].writeBoolean(registro.isLapide());
                        tmps[contadorTmp % arqTmp].writeInt(registro.getTamanho());
                        tmps[contadorTmp % arqTmp].write(registro.getAnime().toByteArray());
                    }

                    contadorTmp++;
                    contadorRegistros = 0;
                    registros.clear();
                }

                ponteiroBase = arq.getFilePointer();
            }

            // escreverArquivoTXT("tmps/tmp1.db", "tmps/txt1.txt");
            // escreverArquivoTXT("tmps/tmp2.db", "tmps/txt2.txt");

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void escreverArquivoTXT(String enderecoDB, String enderecoTXT) throws Exception {
        // Leitura dos registros
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        // Escrever os registros para impressão
        BufferedWriter writer = new BufferedWriter(new FileWriter(enderecoTXT));

        byte[] ba;
        int idTeste;
        int len;
        boolean lapis;

        // Endereço do ponteiro de início
        long ponteiroBase = arq.getFilePointer();
        arq.seek(ponteiroBase);

        while (ponteiroBase < arq.length()) {
            idTeste = arq.readInt();
            lapis = arq.readBoolean();
            len = arq.readInt();

            ba = new byte[len];
            arq.read(ba);

            Registro registro = new Registro();
            registro.fromByteArray(idTeste, lapis, len, ba);

            ponteiroBase = arq.getFilePointer();

            // Escrever no arquivo de impressão
            writer.write(registro.toString(ba));
            writer.newLine();
        }

        writer.close();
        arq.close();
    }
}
