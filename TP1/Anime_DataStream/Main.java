import java.io.*;
import java.util.*;

public class Main {
    public static String header;

    public static String[] splitCSVLinha(String linha) {
        List<String> columns = new ArrayList<>();
        boolean entreAspas = false;
        StringBuilder currentColumn = new StringBuilder();

        for (char c : linha.toCharArray()) {
            if (c == '\"') {
                entreAspas = !entreAspas;
            } else if (c == ',' && !entreAspas) {
                columns.add(currentColumn.toString());
                currentColumn = new StringBuilder();
            } else {
                currentColumn.append(c);
            }
        }

        columns.add(currentColumn.toString());
        return columns.toArray(new String[0]);
    }

    public static Anime[] readFromCSV(String csvPath) {
        ArrayList<Anime> animeList = new ArrayList<>();
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(csvPath));
            header = csvReader.readLine();
            String linha;
            while ((linha = csvReader.readLine()) != null) {
                String[] data = splitCSVLinha(linha);
                Anime anime = new Anime(Integer.parseInt(data[0]), data[1], Float.parseFloat(data[2]), data[3], data[4],
                        Integer.parseInt(data[5]), data[6]);
                animeList.add(anime);
            }
            csvReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return animeList.toArray(new Anime[0]);
    }

    public static void main(String[] args) {
        RandomAccessFile arq; // arquivo de saída

        byte[] ba;
        int len;

        // Escrita
        try {
            arq = new RandomAccessFile("dados/animes.db", "rw");

            Anime[] animes = readFromCSV("dados/anime_tratado.csv");

            long p1 = arq.getFilePointer();

            for (Anime anime : animes) {
                ba = anime.toByteArray();
                arq.writeInt(ba.length); // Tamanho do registro em bytes
                arq.write(ba);
            }

            long p2 = arq.getFilePointer();

            // Leitura
            BufferedWriter writer = new BufferedWriter(new FileWriter("dados/animes_impressos.txt"));

            arq.seek(p1);

            while (p1 < p2) {
                len = arq.readInt();
                ba = new byte[len];
                arq.read(ba);

                Anime anime = new Anime();
                anime.fromByteArray(ba);

                p1 = arq.getFilePointer();

                // Escreve no arquivo de impressão
                writer.write(anime.toString());
                writer.newLine();
            }

            writer.close();
            arq.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

}
