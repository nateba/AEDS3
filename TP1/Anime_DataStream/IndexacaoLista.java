import java.io.*;
import java.util.*;

public class IndexacaoLista {
    public void criarListaInvertidaNomes() {
        try {
            // Ler o arquivo ANIMES.DB e obter os dados
            ArrayList<Registro> registros = new ArrayList<Registro>();
            registros = geraListaRegistros();

            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            for (Registro registro : registros) {
                // Limpar a palavra de caracteres especiais e letras maiúsculas
                String nome = substituirCaracteresEspeciais(registro.getAnime().getNome());
                String[] termos = nome.split(" "); // Quebrar o nome dos Animes

                // Fazer a repetição pelo número de palavras geradas
                for (int i = 0; i < termos.length; i++) {
                    // Remover palavras muito genéricas (que aparecem em mais de 50 registros)
                    if (!palavraMuitoGenerica(termos[i])) {
                        boolean termoRepetido = false;
                        int indexTermo = 0;

                        for (ListaInvertida item : listaInvertida) {
                            if (termos[i].equals(item.getTermo())) {
                                termoRepetido = true;
                                indexTermo = listaInvertida.indexOf(item); // Armazenar a posição da palavra que repete
                                break;
                            }
                        }

                        ListaInvertida tupla = new ListaInvertida();

                        // Caso o termo seja repetido, o processo se dá por acréscimo do novo ID
                        if (termoRepetido) {
                            tupla = listaInvertida.get(indexTermo);
                            tupla.setIdRegistro(registro.getIdRegistro());
                            listaInvertida.set(indexTermo, tupla);
                        } else {
                            // Criação de novo termo
                            tupla = new ListaInvertida(termos[i], registro.getIdRegistro());

                            listaInvertida.add(tupla); // Adicionar tupla na Lista Invertida
                        }
                    }
                }
            }

            // Chamar a função que grava a Lista Invertida em um arquivo .db
            gerarArquivoListaInvertida(listaInvertida, "indexacao/listainvertida_nomes.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void criarListaInvertidaTypes() {
        try {
            // Ler o arquivo ANIMES.DB e obter os dados
            ArrayList<Registro> registros = new ArrayList<Registro>();
            registros = geraListaRegistros();

            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            for (Registro registro : registros) {
                // Limpar a palavra de caracteres especiais e letras maiúsculas
                String tipo = substituirCaracteresEspeciais(registro.getAnime().getType());

                boolean termoRepetido = false;
                int indexTermo = 0;

                for (ListaInvertida item : listaInvertida) {
                    if (tipo.equals(item.getTermo())) {
                        termoRepetido = true;
                        indexTermo = listaInvertida.indexOf(item); // Armazenar a posição da palavra que repete
                        break;
                    }
                }

                ListaInvertida tupla = new ListaInvertida();

                // Caso o termo seja repetido, o processo se dá por acréscimo do novo ID
                if (termoRepetido) {
                    tupla = listaInvertida.get(indexTermo);
                    tupla.setIdRegistro(registro.getIdRegistro());
                    listaInvertida.set(indexTermo, tupla);
                } else {
                    // Criação de novo termo
                    tupla = new ListaInvertida(tipo, registro.getIdRegistro());

                    listaInvertida.add(tupla); // Adicionar tupla na Lista Invertida
                }

            }

            // Chamar a função que grava a Lista Invertida em um arquivo .db
            gerarArquivoListaInvertida(listaInvertida, "indexacao/listainvertida_types.db");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Função recebe objeto Lista Invertida e endereço como parâmetros
    public void gerarArquivoListaInvertida(ArrayList<ListaInvertida> listaInvertida, String enderecoArquivo)
            throws IOException {
        try {
            RandomAccessFile li = new RandomAccessFile(enderecoArquivo, "rw");

            // Limpar possíveis dados anteriores do arquivo (casos de reescrita)
            li.setLength(0);

            for (ListaInvertida tupla : listaInvertida) {
                li.seek(li.length());
                li.writeUTF(tupla.getTermo());
                // Optamos por adicionar a quantidade de IDs no arquivo de registros, para
                // facilitar a leitura no retorno
                li.writeInt(tupla.getIdRegistro().size());

                // Escreve todos os IDs que estão associados a um termo
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

    public ArrayList<ListaInvertida> leituraArquivoListaInvertida(String enderecoArquivo) throws IOException {
        try {
            // Ler o arquivo .db da Lista alvo e obter os dados
            RandomAccessFile li = new RandomAccessFile(enderecoArquivo, "rw");
            ArrayList<ListaInvertida> listaInvertida = new ArrayList<ListaInvertida>();

            long ponteiroBase = li.getFilePointer(); // Pegar o endereço do ponteiro de início

            // Enquanto não chegar no fim do arquivo, a repetição acontece
            while (ponteiroBase < li.length()) {
                ListaInvertida tupla = new ListaInvertida();
                tupla.setTermo(li.readUTF());

                // Opção da dupla de armazenar a quantidade de IDs
                int qtdIds = li.readInt();

                // Ler todos os IDs armazenados
                for (int i = 0; i < qtdIds; i++) {
                    tupla.setIdRegistro(li.readInt());
                }

                listaInvertida.add(tupla);

                ponteiroBase = li.getFilePointer(); // Atualiza o valor do Ponteiro Base
            }

            li.close();

            return listaInvertida; // Retornar a Lista Invertida
        } catch (FileNotFoundException e) {
            e.printStackTrace();

            return null; // Retorna 'null' caso a operação dê errado
        }
    }

    public ArrayList<Registro> geraListaRegistros() {
        try {
            // Abrir o arquivo ANIMES.DB somente para leitura
            RandomAccessFile arq = new RandomAccessFile("dados/animes.db", "r");

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapideRegistro;

            long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início
            arq.seek(ponteiroBase + 4); // Saltar os bytes com a informação do maior ID

            ArrayList<Registro> registros = new ArrayList<Registro>();

            // Enquanto não chegar no fim do arquivo, a repetição acontece
            while (ponteiroBase < arq.length()) {
                idRegistro = arq.readInt();
                lapideRegistro = arq.readBoolean();
                len = arq.readInt();

                ba = new byte[len];
                arq.read(ba);

                // Testar se o registro já não foi deletado
                if (lapideRegistro == false) {
                    // Cria um objeto Registro com os registros válidos
                    Registro registro = new Registro();
                    registro.fromByteArray(idRegistro, lapideRegistro, len, ba);

                    registros.add(registro);
                }

                ponteiroBase = arq.getFilePointer();
            }

            arq.close();

            return registros; // Retorna a lista de registros válidos de ANIMES.DB
        } catch (Exception e) {
            e.printStackTrace();

            return null; // Retorna 'null' caso a operação dê errado
        }
    }

    // Função para arranjar a palavra de maneira adequada
    public String substituirCaracteresEspeciais(String input) {
        // Usar uma expressão regular para encontrar todos os caracteres especiais
        String regex = "[^a-zA-Z0-9 ]";

        // Substituir os caracteres especiais por uma string vazia
        String resultado = input.replaceAll(regex, "");

        return resultado.toLowerCase(); // Retornar a palavra em letras minúsculas
    }

    // Optamos por retirar palavras muito vagas ou repetidas em mais de 50
    // registros, visto que poderia gerar uma pesquisa muito ineficiente
    public boolean palavraMuitoGenerica(String termo) {
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

    // Função de READ com PESQUISA da Lista Invertida
    public void pesquisaListaInvertida(String pesquisaNome, String pesquisaTipo) throws IOException {
        // Criar uma ArrayList de ArrayLists com os possíveis casos de teste
        ArrayList<ArrayList<Integer>> conjuntoTeste = new ArrayList<ArrayList<Integer>>();

        boolean resultadoNomeEncontrado = false;
        boolean resultadoTipoEncontrado = false;

        // Caso haja uma pesquisa de NOME
        if (!pesquisaNome.equals("")) {
            // Ler o arquivo .db com a lista de NOMES
            ArrayList<ListaInvertida> listaInvertida = leituraArquivoListaInvertida(
                    "indexacao/listainvertida_nomes.db");

            String palavras = substituirCaracteresEspeciais(pesquisaNome);
            String[] termos = palavras.split(" ");

            for (String termo : termos) {
                for (ListaInvertida tupla : listaInvertida) {
                    if (tupla.getTermo().equals(termo)) {
                        conjuntoTeste.add(tupla.getIdRegistro()); // Armazenar os Conjuntos de Teste
                        resultadoNomeEncontrado = true;
                    }
                }
            }
        } else {
            resultadoNomeEncontrado = true; // Validar a ausência de pesquisa
        }

        // Caso haja uma pesquisa de TIPO
        if (!pesquisaTipo.equals("")) {
            // Ler o arquivo .db com a lista de TIPOS
            ArrayList<ListaInvertida> listaInvertida = leituraArquivoListaInvertida(
                    "indexacao/listainvertida_types.db");

            for (ListaInvertida tupla : listaInvertida) {
                if (tupla.getTermo().equals(pesquisaTipo.toLowerCase())) {
                    conjuntoTeste.add(tupla.getIdRegistro());
                    resultadoTipoEncontrado = true;
                }
            }
        } else {
            resultadoTipoEncontrado = true; // Validar a ausência de pesquisa
        }

        // Fazer a interseção entre os Conjuntos Teste
        Set<Integer> intersecao = new HashSet<>(conjuntoTeste.get(0));

        for (int i = 1; i < conjuntoTeste.size(); i++) {
            intersecao.retainAll(conjuntoTeste.get(i));
        }

        List<Integer> resultado = new ArrayList<>(intersecao);

        // Pode ser que um dos termos exista mas o outro não, gerando uma interseção
        // vazia
        if (resultado.size() == 0) {
            System.out.println("\nNão existem Animes que correspondam a esses termos!");
        } else if (resultadoNomeEncontrado && resultadoTipoEncontrado) {
            System.out.println("\nO Anime que você está procurando é o de ID: " + resultado);
        } else {
            System.out.println("\nNão existem Animes que correspondam a esses termos!");
        }
    }

    // Função de CREATE da Lista Invertida
    public void createTupla(Anime anime) throws IOException {
        ArrayList<ListaInvertida> listaInvertidaNomes = new ArrayList<ListaInvertida>();
        ArrayList<ListaInvertida> listaInvertidaTypes = new ArrayList<ListaInvertida>();

        // Ler os arquivos .db de Listas Invertidas
        listaInvertidaNomes = leituraArquivoListaInvertida("indexacao/listainvertida_nomes.db");
        listaInvertidaTypes = leituraArquivoListaInvertida("indexacao/listainvertida_types.db");

        // Limpar os caracteres especiais
        String nome = substituirCaracteresEspeciais(anime.nome);
        String[] termosNome = nome.split(" "); // Quebrar o nome do novo Anime

        // Fazer a repetição baseado na quantidade de termos
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

        // Limpar os caracteres especiais
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

        // Chamar a função que grava a Lista Invertida em um arquivo .db
        gerarArquivoListaInvertida(listaInvertidaNomes, "indexacao/listainvertida_nomes.db");
        gerarArquivoListaInvertida(listaInvertidaTypes, "indexacao/listainvertida_types.db");
    }

    // Função de UPDATE da Lista Invertida
    public void updateTupla(ArrayList<Anime> animes) throws IOException {
        // Optamos por deletar o registro antigo e recriar o atualizado

        // Deleta as tuplas antigas
        deleteTupla(animes.get(0)); // Posição onde está o registro antigo

        // Recria as tuplas atualizadas
        createTupla(animes.get(1)); // Posição onde está o registro atualizado
    }

    // Função de DELETE da Lista Invertida
    public void deleteTupla(Anime anime) throws IOException {
        ArrayList<ListaInvertida> listaInvertidaNomes = new ArrayList<ListaInvertida>();
        ArrayList<ListaInvertida> listaInvertidaTypes = new ArrayList<ListaInvertida>();

        listaInvertidaNomes = leituraArquivoListaInvertida("indexacao/listainvertida_nomes.db");
        listaInvertidaTypes = leituraArquivoListaInvertida("indexacao/listainvertida_types.db");

        // Limpar os caracteres especiais
        String nome = substituirCaracteresEspeciais(anime.nome);
        String[] termosNome = nome.split(" ");

        for (int i = 0; i < termosNome.length; i++) {
            for (ListaInvertida item : listaInvertidaNomes) {
                if (termosNome[i].equals(item.getTermo())) {
                    // Remover o ID da lista de IDs
                    item.getIdRegistro().remove(Integer.valueOf(anime.idAnime));

                    // Caso o termo tenha ficado vazio de IDs, é preciso deletá-lo
                    if (item.getIdRegistro().size() == 0) {
                        listaInvertidaNomes.remove(item);
                    }

                    break;
                }
            }
        }

        // Limpar os caracteres especiais
        String tipo = substituirCaracteresEspeciais(anime.type);

        for (ListaInvertida item : listaInvertidaTypes) {
            if (tipo.equals(item.getTermo())) {
                // Remover o ID da lista de IDs
                item.getIdRegistro().remove(Integer.valueOf(anime.idAnime));

                // Caso o termo tenha ficado vazio de IDs, é preciso deletá-lo
                if (item.getIdRegistro().size() == 0) {
                    listaInvertidaNomes.remove(item);
                }

                break;
            }
        }

        // Chamar a função que grava a Lista Invertida em um arquivo .db
        gerarArquivoListaInvertida(listaInvertidaNomes, "indexacao/listainvertida_nomes.db");
        gerarArquivoListaInvertida(listaInvertidaTypes, "indexacao/listainvertida_types.db");
    }
}
