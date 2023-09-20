import java.io.*;
import java.util.*;

public class IntercalacaoSubstituicao {
    int qtdRegistros = 0;

    public void intercalacaoSelecaoSubstituicao() {
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
            RandomAccessFile tmp1 = new RandomAccessFile("tmps/tmp1.db", "rw");
            RandomAccessFile tmp2 = new RandomAccessFile("tmps/tmp2.db", "rw");

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

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
                }

                ponteiroBase = arq.getFilePointer();
            }

            // O HEAP PRECISA ACONTECER AQUI
            HeapMinimo heapMinimo = new HeapMinimo(100);
            int segmento = 0;

            // Montar o heap inicial
            while (heapMinimo.heapCheio() == false && registros.size() > 0) {
                No no = new No(segmento, registros.get(0));
                heapMinimo.inserirRegistro(no);

                registros.remove(0);
            }

            System.out.println(heapMinimo.getRaiz());

            RandomAccessFile numeroArquivoEscrita;
            numeroArquivoEscrita = (contadorTmp % 2 == 0) ? tmp1 : tmp2;

            // Variável que confere se o segmento deve ser alterado ou não
            RandomAccessFile conferenciaSegmento = numeroArquivoEscrita;

            while (heapMinimo.heapVazio() == false) {
                No no = heapMinimo.retirarRaiz();

                numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                numeroArquivoEscrita.writeInt(no.getRegistro().getIdRegistro());
                numeroArquivoEscrita.writeBoolean(no.getRegistro().isLapide());
                numeroArquivoEscrita.writeInt(no.getRegistro().getTamanho());
                numeroArquivoEscrita.write(no.getRegistro().getAnime().toByteArray());

                // Ainda existem registros a serem inseridos?
                if (registros.size() > 0) {
                    // Testar se deve ser mantido o segmento
                    if (registros.get(0).getIdRegistro() < no.getRegistro().getIdRegistro()) {
                        // Segmento não pode alternar a todo momento
                        // segmento = conferenciaSegmento == numeroArquivoEscrita ? segmento : (segmento
                        // + 1);
                        segmento++;
                    }

                    No novoNo = new No(segmento, registros.get(0));
                    heapMinimo.inserirRegistro(novoNo);
                    registros.remove(0);
                }

                conferenciaSegmento = numeroArquivoEscrita;
                // Conferir se todos os segmentos são 1, 2, etc dentro do Heap
                // Se sim, preciso alternar o número do arquivo de escrita e contadorTmp
                if (heapMinimo.heapVazio() == false
                        && heapMinimo.getRaiz().getRegistro().getIdRegistro() < no.getRegistro().getIdRegistro()) {
                    contadorTmp++;
                    numeroArquivoEscrita = (contadorTmp % 2 == 0) ? tmp1 : tmp2;
                }
            }

            arq.close();
            tmp1.close();
            tmp2.close();

            escreverArquivoTXT("tmps/tmp1.db", "tmps/txt1.txt");
            escreverArquivoTXT("tmps/tmp2.db", "tmps/txt2.txt");
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
            writer.write(registro.toString2(ba));
            writer.newLine();
        }

        writer.close();
        arq.close();
    }
}
