import java.io.*;
import java.util.*;

public class Indexacao {
    public static void main(String[] args) {
        gerarListaInvertida();
    }

    public static void gerarListaInvertida() {
        try {
            ArrayList<Registro> registros = new ArrayList<Registro>();
            registros = geraListaRegistros();

            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            // Quebrar o nome dos Animes e adicionar na Lista Invertida
            for (Registro registro : registros) {
                String nome = substituirCaracteresEspeciais(registro.getAnime().getNome());
                String[] termos = nome.split(" ");

                for (int i = 0; i < termos.length; i++) {
                    if (!palavraMuitoGenerica(termos[i])) {
                        boolean termoRepetido = false;
                        int indexTermo = 0;

                        for (ListaInvertida item : listaInvertida) {
                            if (termos[i].equals(item.getTermo())) {
                                termoRepetido = true;
                                indexTermo = listaInvertida.indexOf(item);
                                break;
                            }
                        }

                        ListaInvertida tupla = new ListaInvertida();

                        if (termoRepetido) {
                            tupla = listaInvertida.get(indexTermo);
                            tupla.setIdRegistro(registro.getIdRegistro());
                            listaInvertida.set(indexTermo, tupla);
                        } else {
                            tupla = new ListaInvertida(termos[i], registro.getIdRegistro());

                            listaInvertida.add(tupla);
                        }
                    }
                }
            }

            for (int i = 0; i < 10; i++) {
                System.out.println(listaInvertida.get(i).getTermo() + " " + listaInvertida.get(i).getIdRegistro());
            }

            System.out.println("\n==========================\n");

            pesquisaListaInvertida("Cowboy Bebop: Tengoku", listaInvertida);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Registro> geraListaRegistros() {
        try {
            RandomAccessFile arq = new RandomAccessFile("dados/animes.db", "r"); // Abre arquivo para leitura e escrita

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

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

            arq.close();

            return registros;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String substituirCaracteresEspeciais(String input) {
        // Usar uma expressão regular para encontrar todos os caracteres especiais
        String regex = "[^a-zA-Z0-9 ]";

        // Substitua os caracteres especiais por uma string vazia
        String resultado = input.replaceAll(regex, "");

        return resultado.toLowerCase();
    }

    public static boolean palavraMuitoGenerica(String termo) {
        String[] palavrasMuitoRepetidas = { "no", "bouken", "to", "ni", "1", "",
                "the", "of", "e", "tv", "tenshi", "ai", "movie", "black", "gakuen", "mahou", "shoujo",
                "mobile", "suit", "gundam", "in", "x", "a", "shin", "densetsu", "uchuu", "world", "2",
                "wo", "go", "animation", "sekai", "kimi", "ga", "boku", "princess", "ova", "love",
                "special", "na", "senki", "koi", "monogatari", "dragon", "girls", "season", "de",
                "kara", "wa", "senshi", "shounen", "conan", "4", "2nd", "ka", "dai", "ii", "iii", "3",
                "one", "piece", "doraemon", "picture", "pokemon", "battle", "yo", "ginga", "ore",
                "gekijou", "specials", "detective", "recap", "lupin", "episode", "drama", "anime", "live" };

        for (String palavra : palavrasMuitoRepetidas) {
            if (palavra.equals(termo)) {
                return true;
            }
        }

        return false;
    }

    // ATUALIZAR PARA UMA FUNÇÃO COM RETORNO
    public static void pesquisaListaInvertida(String pesquisa, ArrayList<ListaInvertida> listaInvertida) {
        String palavras = substituirCaracteresEspeciais(pesquisa);
        String[] termos = palavras.split(" ");
        ArrayList<ArrayList<Integer>> conjuntoTeste = new ArrayList<ArrayList<Integer>>(termos.length);

        for (String termo : termos) {
            for (ListaInvertida tupla : listaInvertida) {
                if (tupla.getTermo().equals(termo)) {
                    conjuntoTeste.add(tupla.getIdRegistro());
                }
            }
        }

        // Fazer a interseção entre os conjuntos
        Set<Integer> intersecao = new HashSet<>(conjuntoTeste.get(0));

        for (int i = 1; i < conjuntoTeste.size(); i++) {
            intersecao.retainAll(conjuntoTeste.get(i));
        }

        List<Integer> resultado = new ArrayList<>(intersecao);

        System.out.println(resultado);
    }
}
