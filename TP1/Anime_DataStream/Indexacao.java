import java.io.*;
import java.util.*;

public class Indexacao {
    public static void main(String[] args) throws IOException {

        // criarListaInvertidaNomes();
        criarListaInvertidaTypes();

    }

    public static void criarListaInvertidaNomes() {
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

            pesquisaListaInvertida("Naruto", listaInvertida);

            gerarArquivoListaInvertida(listaInvertida, "indexacao/listainvertida_nomes.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void criarListaInvertidaTypes() {
        try {
            ArrayList<Registro> registros = new ArrayList<Registro>();
            registros = geraListaRegistros();

            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            for (Registro registro : registros) {
                String tipo = substituirCaracteresEspeciais(registro.getAnime().getType());

                boolean termoRepetido = false;
                int indexTermo = 0;

                for (ListaInvertida item : listaInvertida) {
                    if (tipo.equals(item.getTermo())) {
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
                    tupla = new ListaInvertida(tipo, registro.getIdRegistro());

                    listaInvertida.add(tupla);
                }

            }

            gerarArquivoListaInvertida(listaInvertida, "indexacao/listainvertida_types.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gerarArquivoListaInvertida(ArrayList<ListaInvertida> listaInvertida, String enderecoArquivo)
            throws IOException {
        try {
            RandomAccessFile li = new RandomAccessFile(enderecoArquivo, "rw");

            for (ListaInvertida tupla : listaInvertida) {
                li.seek(li.length());
                li.writeUTF(tupla.getTermo());
                li.writeInt(tupla.getIdRegistro().size());

                for (int id : tupla.getIdRegistro()) {
                    li.seek(li.length());
                    li.writeInt(id);
                }
            }

            li.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<ListaInvertida> leituraArquivoListaInvertida(String enderecoArquivo) throws IOException {
        try {
            RandomAccessFile li = new RandomAccessFile(enderecoArquivo, "rw");
            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            long ponteiroBase = li.getFilePointer();

            while (ponteiroBase < li.length()) {
                ListaInvertida tupla = new ListaInvertida();
                tupla.setTermo(li.readUTF());

                int qtdIds = li.readInt();

                for (int i = 0; i < qtdIds; i++) {
                    tupla.setIdRegistro(li.readInt());
                }

                listaInvertida.add(tupla);

                ponteiroBase = li.getFilePointer();
            }

            li.close();

            return listaInvertida;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
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
    // COLOCAR A OPÇÃO DUPLA
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

    // Função de criar novos elementos na Lista Invertida
    public static void criarTupla(Anime anime) throws IOException {
        ArrayList<ListaInvertida> listaInvertidaNomes = new ArrayList<ListaInvertida>();
        ArrayList<ListaInvertida> listaInvertidaTypes = new ArrayList<ListaInvertida>();

        listaInvertidaNomes = leituraArquivoListaInvertida("indexacao/listainvertida_nomes.db");
        listaInvertidaTypes = leituraArquivoListaInvertida("indexacao/listainvertida_types.db");

        String nome = substituirCaracteresEspeciais(anime.nome);
        String[] termosNome = nome.split(" ");

        for (int i = 0; i < termosNome.length; i++) {
            if (!palavraMuitoGenerica(termosNome[i])) {
                boolean termoRepetido = false;
                int indexTermo = 0;

                for (ListaInvertida item : listaInvertidaNomes) {
                    if (termosNome[i].equals(item.getTermo())) {
                        termoRepetido = true;
                        indexTermo = listaInvertidaNomes.indexOf(item);
                        break;
                    }
                }

                ListaInvertida tupla = new ListaInvertida();

                if (termoRepetido) {
                    tupla = listaInvertidaNomes.get(indexTermo);
                    tupla.setIdRegistro(anime.idAnime);
                    listaInvertidaNomes.set(indexTermo, tupla);
                } else {
                    tupla = new ListaInvertida(termosNome[i], anime.idAnime);

                    listaInvertidaNomes.add(tupla);
                }
            }
        }

        String tipo = substituirCaracteresEspeciais(anime.type);

        boolean termoRepetido = false;
        int indexTermo = 0;

        for (ListaInvertida item : listaInvertidaTypes) {
            if (tipo.equals(item.getTermo())) {
                termoRepetido = true;
                indexTermo = listaInvertidaTypes.indexOf(item);
                break;
            }
        }

        ListaInvertida tupla = new ListaInvertida();

        if (termoRepetido) {
            tupla = listaInvertidaTypes.get(indexTermo);
            tupla.setIdRegistro(anime.idAnime);
            listaInvertidaTypes.set(indexTermo, tupla);
        } else {
            tupla = new ListaInvertida(tipo, anime.idAnime);

            listaInvertidaTypes.add(tupla);
        }
    }

    // Função de deletar ID ou tupla
    public static void deletarTupla(Anime anime) {
        // Testar se ID existe
        // Testar se é tamanho 1 -> Jà deleta toda a tupla
        // Se não, deletar somente o ID
    }
}
