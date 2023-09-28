import java.io.*;
import java.util.*;

class ComparadorPorId implements Comparator<Registro> {
    @Override
    public int compare(Registro r1, Registro r2) {
        return Integer.compare(r1.getIdRegistro(), r2.getIdRegistro());
    }
}

public class OrdenacaoExterna {
    int bloco = 100;

    int qtdRegistros = 0;

    public void intercalacaoBalanceadaComum() {
        qtdRegistros = contaRegistros("dados/animes.db");

        distribuicaoComum("dados/animes.db");

        intercalacaoComum();
    }

    public void intercalacaoBalanceadaVariavel() {
        qtdRegistros = contaRegistros("dados/animes.db");

        distribuicaoComum("dados/animes.db");

        intercalacaoVariavel();
    }

    public void intercalacaoBalanceadaSubstituicao() {
        qtdRegistros = contaRegistros("dados/animes.db");

        distribuicaoSelecao("dados/animes.db");

        intercalacaoComum();
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

    public void distribuicaoComum(String enderecoDB) {
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

            while (registros.size() > 0) {
                ArrayList<Registro> blocoOrdenavelTmp = new ArrayList<Registro>();

                for (int i = 0; i < bloco; i++) {
                    if (registros.size() > 0) {
                        blocoOrdenavelTmp.add(registros.get(0));
                        registros.remove(0);
                    }
                }

                Collections.sort(blocoOrdenavelTmp, new ComparadorPorId());

                RandomAccessFile numeroArquivoEscrita;
                numeroArquivoEscrita = (contadorTmp % 2 == 0) ? tmp1 : tmp2;

                for (Registro registro : blocoOrdenavelTmp) {
                    numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                    numeroArquivoEscrita.writeInt(registro.getIdRegistro());
                    numeroArquivoEscrita.writeBoolean(registro.isLapide());
                    numeroArquivoEscrita.writeInt(registro.getTamanho());
                    numeroArquivoEscrita.write(registro.getAnime().toByteArray());
                }

                contadorTmp++;
            }

            arq.close();
            tmp1.close();
            tmp2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void distribuicaoSelecao(String enderecoDB) {
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

            System.out.println(heapMinimo.getRaiz().getRegistro().getIdRegistro());

            RandomAccessFile numeroArquivoEscrita;
            numeroArquivoEscrita = (contadorTmp % 2 == 0) ? tmp1 : tmp2;

            boolean alternarSegmento = false;

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
                        alternarSegmento = true;
                        System.out.println("Troca");
                    }

                    No novoNo = new No();

                    if (alternarSegmento) {
                        novoNo = new No((segmento + 1), registros.get(0));
                        alternarSegmento = false;
                    } else {
                        novoNo = new No(segmento, registros.get(0));
                    }

                    heapMinimo.inserirRegistro(novoNo);
                    registros.remove(0);
                }

                // Conferir se todos os segmentos são 1, 2, etc dentro do Heap
                // Se sim, preciso alternar o número do arquivo de escrita e contadorTmp
                if (heapMinimo.heapVazio() == false
                        && heapMinimo.getRaiz().getRegistro().getIdRegistro() < no.getRegistro().getIdRegistro()) {
                    contadorTmp++;
                    numeroArquivoEscrita = (contadorTmp % 2 == 0) ? tmp1 : tmp2;
                    segmento++;
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

    public void intercalacaoComum() {
        try {
            RandomAccessFile tmp1 = new RandomAccessFile("tmps/tmp1.db", "rw");
            RandomAccessFile tmp2 = new RandomAccessFile("tmps/tmp2.db", "rw");
            RandomAccessFile tmp3 = new RandomAccessFile("tmps/tmp3.db", "rw");
            RandomAccessFile tmp4 = new RandomAccessFile("tmps/tmp4.db", "rw");

            ArrayList<Registro> registrosTmp1 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp2 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp3 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp4 = new ArrayList<Registro>();

            int numeroRodada = 0; // Número da rodada de intercalação

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            long ponteiroBase;

            boolean arquivoOrdenado = false;

            while (arquivoOrdenado == false) {
                System.out.println("[ Intercalação Rodada " + (numeroRodada + 1) + " ]");
                System.out.print(".");
                System.out.print(".");
                System.out.print(".");
                System.out.print(".");
                System.out.println(".");

                if (numeroRodada % 2 == 0) {
                    do {
                        tmp1.seek(0);
                        ponteiroBase = tmp1.getFilePointer();

                        // Varre o arquivo temporário 1
                        while (ponteiroBase < tmp1.length()) {
                            idRegistro = tmp1.readInt();
                            lapis = tmp1.readBoolean();
                            len = tmp1.readInt();

                            ba = new byte[len];
                            tmp1.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp1.add(registro);

                            ponteiroBase = tmp1.getFilePointer();
                        }

                        tmp2.seek(0);
                        ponteiroBase = tmp2.getFilePointer();

                        // Varre o arquivo temporário 2
                        while (ponteiroBase < tmp2.length()) {
                            idRegistro = tmp2.readInt();
                            lapis = tmp2.readBoolean();
                            len = tmp2.readInt();

                            ba = new byte[len];
                            tmp2.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp2.add(registro);

                            ponteiroBase = tmp2.getFilePointer();
                        }

                        tmp1.setLength(0);
                        tmp2.setLength(0);

                        int rodadaArquivo = 0;

                        while (registrosTmp1.size() > 0 || registrosTmp2.size() > 0) {
                            ArrayList<Registro> blocoOrdenavelTmp = new ArrayList<Registro>();

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp1.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp1.get(0));
                                    registrosTmp1.remove(0);
                                }
                            }

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp2.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp2.get(0));
                                    registrosTmp2.remove(0);
                                }
                            }

                            Collections.sort(blocoOrdenavelTmp, new ComparadorPorId());

                            RandomAccessFile numeroArquivoEscrita;
                            numeroArquivoEscrita = (rodadaArquivo % 2 == 0) ? tmp3 : tmp4;

                            for (Registro registro : blocoOrdenavelTmp) {
                                numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                                numeroArquivoEscrita.writeInt(registro.getIdRegistro());
                                numeroArquivoEscrita.writeBoolean(registro.isLapide());
                                numeroArquivoEscrita.writeInt(registro.getTamanho());
                                numeroArquivoEscrita.write(registro.getAnime().toByteArray());
                            }

                            if (blocoOrdenavelTmp.size() >= qtdRegistros) {
                                arquivoOrdenado = true;
                                gerarArquivoOrdenado(blocoOrdenavelTmp);
                                break;
                            }

                            blocoOrdenavelTmp.clear();

                            rodadaArquivo++;
                        }

                    } while (registrosTmp1.size() > 0 || registrosTmp2.size() > 0);
                } else {
                    do {
                        tmp3.seek(0);
                        ponteiroBase = tmp3.getFilePointer();

                        // Varre o arquivo temporário 3
                        while (ponteiroBase < tmp3.length()) {
                            idRegistro = tmp3.readInt();
                            lapis = tmp3.readBoolean();
                            len = tmp3.readInt();

                            ba = new byte[len];
                            tmp3.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp3.add(registro);

                            ponteiroBase = tmp3.getFilePointer();
                        }

                        tmp4.seek(0);
                        ponteiroBase = tmp4.getFilePointer();

                        // Varre o arquivo temporário 2
                        while (ponteiroBase < tmp4.length()) {
                            idRegistro = tmp4.readInt();
                            lapis = tmp4.readBoolean();
                            len = tmp4.readInt();

                            ba = new byte[len];
                            tmp4.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp4.add(registro);

                            ponteiroBase = tmp4.getFilePointer();
                        }

                        tmp3.setLength(0);
                        tmp4.setLength(0);

                        int rodadaArquivo = 0;

                        while (registrosTmp3.size() > 0 || registrosTmp4.size() > 0) {
                            ArrayList<Registro> blocoOrdenavelTmp = new ArrayList<Registro>();

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp3.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp3.get(0));
                                    registrosTmp3.remove(0);
                                }
                            }

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp4.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp4.get(0));
                                    registrosTmp4.remove(0);
                                }
                            }

                            Collections.sort(blocoOrdenavelTmp, new ComparadorPorId());

                            RandomAccessFile numeroArquivoEscrita;
                            numeroArquivoEscrita = (rodadaArquivo % 2 == 0) ? tmp1 : tmp2;

                            for (Registro registro : blocoOrdenavelTmp) {
                                numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                                numeroArquivoEscrita.writeInt(registro.getIdRegistro());
                                numeroArquivoEscrita.writeBoolean(registro.isLapide());
                                numeroArquivoEscrita.writeInt(registro.getTamanho());
                                numeroArquivoEscrita.write(registro.getAnime().toByteArray());
                            }

                            if (blocoOrdenavelTmp.size() >= qtdRegistros) {
                                arquivoOrdenado = true;
                                gerarArquivoOrdenado(blocoOrdenavelTmp);
                                break;
                            }

                            blocoOrdenavelTmp.clear();

                            rodadaArquivo++;
                        }

                    } while (registrosTmp3.size() > 0 || registrosTmp4.size() > 0);
                }

                numeroRodada++;
            }

            tmp1.close();
            tmp2.close();
            tmp3.close();
            tmp4.close();

            escreverArquivoTXT("tmps/tmp1.db", "tmps/txt1.txt");
            escreverArquivoTXT("tmps/tmp2.db", "tmps/txt2.txt");
            escreverArquivoTXT("tmps/tmp3.db", "tmps/txt3.txt");
            escreverArquivoTXT("tmps/tmp4.db", "tmps/txt4.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void intercalacaoVariavel() {
        try {
            RandomAccessFile tmp1 = new RandomAccessFile("tmps/tmp1.db", "rw");
            RandomAccessFile tmp2 = new RandomAccessFile("tmps/tmp2.db", "rw");
            RandomAccessFile tmp3 = new RandomAccessFile("tmps/tmp3.db", "rw");
            RandomAccessFile tmp4 = new RandomAccessFile("tmps/tmp4.db", "rw");

            ArrayList<Registro> registrosTmp1 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp2 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp3 = new ArrayList<Registro>();
            ArrayList<Registro> registrosTmp4 = new ArrayList<Registro>();

            int numeroRodada = 0; // Número da rodada de intercalação

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            long ponteiroBase;

            boolean arquivoOrdenado = false;

            while (arquivoOrdenado == false) {
                System.out.println(numeroRodada);
                if (numeroRodada % 2 == 0) {
                    do {
                        tmp1.seek(0);
                        ponteiroBase = tmp1.getFilePointer();

                        // Varre o arquivo temporário 1
                        while (ponteiroBase < tmp1.length()) {
                            idRegistro = tmp1.readInt();
                            lapis = tmp1.readBoolean();
                            len = tmp1.readInt();

                            ba = new byte[len];
                            tmp1.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp1.add(registro);

                            ponteiroBase = tmp1.getFilePointer();
                        }

                        tmp2.seek(0);
                        ponteiroBase = tmp2.getFilePointer();

                        // Varre o arquivo temporário 2
                        while (ponteiroBase < tmp2.length()) {
                            idRegistro = tmp2.readInt();
                            lapis = tmp2.readBoolean();
                            len = tmp2.readInt();

                            ba = new byte[len];
                            tmp2.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp2.add(registro);

                            ponteiroBase = tmp2.getFilePointer();
                        }

                        tmp1.setLength(0);
                        tmp2.setLength(0);

                        int rodadaArquivo = 0;

                        while (registrosTmp1.size() > 0 || registrosTmp2.size() > 0) {
                            ArrayList<Registro> blocoOrdenavelTmp = new ArrayList<Registro>();

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp1.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp1.get(0));
                                    registrosTmp1.remove(0);
                                }

                                if (i == ((bloco * Math.pow(2, numeroRodada)) - 1) && registrosTmp1.size() > 0
                                        && blocoOrdenavelTmp
                                                .get(blocoOrdenavelTmp.size() - 1).getIdRegistro() < registrosTmp1
                                                        .get(0).getIdRegistro()) {
                                    i = 0;
                                }
                            }

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp2.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp2.get(0));
                                    registrosTmp2.remove(0);
                                }

                                if (i == ((bloco * Math.pow(2, numeroRodada)) - 1) && registrosTmp2.size() > 0
                                        && blocoOrdenavelTmp
                                                .get(blocoOrdenavelTmp.size() - 1).getIdRegistro() < registrosTmp2
                                                        .get(0).getIdRegistro()) {
                                    i = 0;
                                }
                            }

                            Collections.sort(blocoOrdenavelTmp, new ComparadorPorId());

                            RandomAccessFile numeroArquivoEscrita;
                            numeroArquivoEscrita = (rodadaArquivo % 2 == 0) ? tmp3 : tmp4;

                            for (Registro registro : blocoOrdenavelTmp) {
                                numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                                numeroArquivoEscrita.writeInt(registro.getIdRegistro());
                                numeroArquivoEscrita.writeBoolean(registro.isLapide());
                                numeroArquivoEscrita.writeInt(registro.getTamanho());
                                numeroArquivoEscrita.write(registro.getAnime().toByteArray());
                            }

                            System.out.println(blocoOrdenavelTmp.size());

                            if (blocoOrdenavelTmp.size() >= qtdRegistros) {
                                arquivoOrdenado = true;
                            }

                            blocoOrdenavelTmp.clear();

                            rodadaArquivo++;
                        }

                    } while (registrosTmp1.size() > 0 || registrosTmp2.size() > 0);
                } else {
                    do {
                        tmp3.seek(0);
                        ponteiroBase = tmp3.getFilePointer();

                        // Varre o arquivo temporário 3
                        while (ponteiroBase < tmp3.length()) {
                            idRegistro = tmp3.readInt();
                            lapis = tmp3.readBoolean();
                            len = tmp3.readInt();

                            ba = new byte[len];
                            tmp3.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp3.add(registro);

                            ponteiroBase = tmp3.getFilePointer();
                        }

                        tmp4.seek(0);
                        ponteiroBase = tmp4.getFilePointer();

                        // Varre o arquivo temporário 2
                        while (ponteiroBase < tmp4.length()) {
                            idRegistro = tmp4.readInt();
                            lapis = tmp4.readBoolean();
                            len = tmp4.readInt();

                            ba = new byte[len];
                            tmp4.read(ba);

                            Registro registro = new Registro();
                            registro.fromByteArray(idRegistro, lapis, len, ba);

                            registrosTmp4.add(registro);

                            ponteiroBase = tmp4.getFilePointer();
                        }

                        tmp3.setLength(0);
                        tmp4.setLength(0);

                        int rodadaArquivo = 0;

                        while (registrosTmp3.size() > 0 || registrosTmp4.size() > 0) {
                            ArrayList<Registro> blocoOrdenavelTmp = new ArrayList<Registro>();

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp3.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp3.get(0));
                                    registrosTmp3.remove(0);

                                    if (i == ((bloco * Math.pow(2, numeroRodada)) - 1) && registrosTmp3.size() > 0
                                            && blocoOrdenavelTmp
                                                    .get(blocoOrdenavelTmp.size() - 1).getIdRegistro() < registrosTmp3
                                                            .get(0).getIdRegistro()) {
                                        i = 0;
                                    }
                                }
                            }

                            for (int i = 0; i < (bloco * Math.pow(2, numeroRodada)); i++) {
                                if (registrosTmp4.size() > 0) {
                                    blocoOrdenavelTmp.add(registrosTmp4.get(0));
                                    registrosTmp4.remove(0);

                                    if (i == ((bloco * Math.pow(2, numeroRodada)) - 1) && registrosTmp4.size() > 0
                                            && blocoOrdenavelTmp
                                                    .get(blocoOrdenavelTmp.size() - 1).getIdRegistro() < registrosTmp4
                                                            .get(0).getIdRegistro()) {
                                        i = 0;
                                    }
                                }
                            }

                            Collections.sort(blocoOrdenavelTmp, new ComparadorPorId());

                            RandomAccessFile numeroArquivoEscrita;
                            numeroArquivoEscrita = (rodadaArquivo % 2 == 0) ? tmp1 : tmp2;

                            for (Registro registro : blocoOrdenavelTmp) {
                                numeroArquivoEscrita.seek(numeroArquivoEscrita.length());
                                numeroArquivoEscrita.writeInt(registro.getIdRegistro());
                                numeroArquivoEscrita.writeBoolean(registro.isLapide());
                                numeroArquivoEscrita.writeInt(registro.getTamanho());
                                numeroArquivoEscrita.write(registro.getAnime().toByteArray());
                            }

                            System.out.println(blocoOrdenavelTmp.size());

                            if (blocoOrdenavelTmp.size() >= qtdRegistros) {
                                arquivoOrdenado = true;
                            }

                            blocoOrdenavelTmp.clear();

                            rodadaArquivo++;
                        }

                    } while (registrosTmp3.size() > 0 || registrosTmp4.size() > 0);
                }

                numeroRodada++;
            }

            tmp1.close();
            tmp2.close();
            tmp3.close();
            tmp4.close();

            escreverArquivoTXT("tmps/tmp1.db", "tmps/txt1.txt");
            escreverArquivoTXT("tmps/tmp2.db", "tmps/txt2.txt");
            escreverArquivoTXT("tmps/tmp3.db", "tmps/txt3.txt");
            escreverArquivoTXT("tmps/tmp4.db", "tmps/txt4.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gerarArquivoOrdenado(ArrayList<Registro> listaOrdenada) throws Exception {
        try {
            RandomAccessFile arqFinal = new RandomAccessFile("ordenacao/intercalacao_comum.db", "rw");

            for (Registro registro : listaOrdenada) {
                arqFinal.seek(arqFinal.length());
                arqFinal.writeInt(registro.getIdRegistro());
                arqFinal.writeBoolean(registro.isLapide());
                arqFinal.writeInt(registro.getTamanho());
                arqFinal.write(registro.getAnime().toByteArray());
            }

            arqFinal.close();
        } catch (FileNotFoundException e) {
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
