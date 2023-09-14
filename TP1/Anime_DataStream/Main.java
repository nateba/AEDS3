import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        try {
            // Passar o path dos arquivos DB e CSV, respectivamente
            escreverArquivoDB("dados/animes.db", "dados/animes_tratados.csv");

            // Passar o path dos arquivos DB e TXT, respectivamente
            lerArquivoDB("dados/animes.db", "dados/animes_impressos.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void escreverArquivoDB(String enderecoDB, String enderecoCSV) throws Exception {
        // Escrita dos registros
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw");

        byte[] ba;

        Anime[] animes = lerArquivoCSV(enderecoCSV);

        int ultimoId;
        ultimoId = animes[animes.length - 1].idAnime;

        arq.writeInt(ultimoId);

        for (Anime anime : animes) {
            Registro registro = new Registro(anime);

            ba = registro.toByteArray();

            arq.write(ba);
        }

        arq.close();
    }

    public static void lerArquivoDB(String enderecoDB, String enderecoTXT) throws Exception {
        // Leitura dos registros
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        // Escrever os registros para impressão
        BufferedWriter writer = new BufferedWriter(new FileWriter("dados/animes_impressos.txt"));

        byte[] ba;
        int idTeste;
        int len;
        boolean lapis;

        // Endereço do ponteiro de início
        long ponteiroBase = arq.getFilePointer();
        arq.seek(ponteiroBase + 4);

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
            writer.write(registro.toString());
            writer.newLine();
        }

        writer.close();
        arq.close();
    }

    public static void escreverArquivoTXT(String enderecoTXT, Anime anime) throws Exception {
        // Vazio por enquanto
    }

    public static String header;

    public static String[] dividirLinhaCSV(String linha) {
        List<String> colunas = new ArrayList<>();
        boolean entreAspas = false;
        StringBuilder colunaAtual = new StringBuilder();

        for (char c : linha.toCharArray()) {
            if (c == '\"') {
                entreAspas = !entreAspas;
            } else if (c == ',' && !entreAspas) {
                colunas.add(colunaAtual.toString());
                colunaAtual = new StringBuilder();
            } else {
                colunaAtual.append(c);
            }
        }

        colunas.add(colunaAtual.toString());

        return colunas.toArray(new String[0]);
    }

    public static Anime[] lerArquivoCSV(String pathCSV) {
        ArrayList<Anime> animeList = new ArrayList<>();

        try {
            BufferedReader leitorCSV = new BufferedReader(new FileReader(pathCSV));
            header = leitorCSV.readLine();
            String linha;

            while ((linha = leitorCSV.readLine()) != null) {
                String[] data = dividirLinhaCSV(linha);
                Anime anime = new Anime(Integer.parseInt(data[0]), data[1], Float.parseFloat(data[2]), data[3], data[4],
                        Integer.parseInt(data[5]), data[6]);

                animeList.add(anime);
            }

            leitorCSV.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return animeList.toArray(new Anime[0]);
    }
}