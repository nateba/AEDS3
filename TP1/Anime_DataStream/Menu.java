import java.io.*;
import java.util.*;

public class Menu {
    // Endreços dos arquivos externos
    String enderecoDB = "dados/animes.db";
    String enderecoCSV = "dados/animes_tratados.csv";
    String enderecoTXT = "dados/animes_impressos.txt";

    Scanner input = new Scanner(System.in);

    // A variavel opção será colocada como global para poder ser acessada por todas
    // as funções dos diferentes escopos
    int opcao;

    public void tratamentoMenu() throws IOException {
        // Exibir o menu de início do programa
        exibirMenuInicial();

        opcao = input.nextInt();

        // Enquanto a opção não for válida, o menu inicial continuará sendo exibido para
        // o usuário do programa
        menuInicial();

        // Se a opção inicial for válida, o programa pode prosseguir
        if (opcao == 1) {
            try {
                // Passar o path dos arquivos DB e CSV, respectivamente
                escreverArquivoDB(enderecoDB, enderecoCSV);

                System.out.println("\n•._.••´¯``•.,,.•` VOCÊ ACABOU DE GERAR O ARQUIVO ANIMES.DB! `•.,,.•´´¯`••._.•");
                System.out.println("\nAgora, vamos te dar algumas opções. O que você gostaria de fazer?");

                // Exibir o menu principal que dará seguimento com o programa
                while (opcao != 0) {
                    exibirMenuPrincipal();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Caso o usuário termine as interações com o programa
        if (opcao == 0) {
            System.out.println("\n•._.••´¯``•.,,.•` OBRIGADO POR UTILIZAR O NOSSO PROGRAMA! `•.,,.•´´¯`••._.•\n\n");
        }

        input.close();
    }

    public void exibirMenuInicial() {
        System.out.println(
                "                                                                                                               \r\n"
                        + //
                        "   _|_|    _|      _|  _|_|_|  _|      _|  _|_|_|_|    _|_|_|                      _|_|_|_|_|  _|_|_|      _|  \r\n"
                        + //
                        " _|    _|  _|_|    _|    _|    _|_|  _|_|  _|        _|                                _|      _|    _|  _|_|  \r\n"
                        + //
                        " _|_|_|_|  _|  _|  _|    _|    _|  _|  _|  _|_|_|      _|_|        _|_|_|_|_|          _|      _|_|_|      _|  \r\n"
                        + //
                        " _|    _|  _|    _|_|    _|    _|      _|  _|              _|                          _|      _|          _|  \r\n"
                        + //
                        " _|    _|  _|      _|  _|_|_|  _|      _|  _|_|_|_|  _|_|_|                            _|      _|          _|  \r\n"
                        + //
                        "                                                                                                               \r\n"
                        + //
                        "                                                                                                               ");

        System.out.println("Bem-vindo a nossa base de Animes do Trabalho Prático 1!");
        System.out.println("Antes de continuar, o que você gostaria de fazer?");
        System.out.println("[1] Gerar arquivo animes.db");
        System.out.println("[0] Sair do programa");
    }

    public void menuInicial() {
        // Caso opção digitada seja inválida
        while (opcao != 0 && opcao != 1) {
            System.out.println("\nOpção inválida! Tente uma opção existente:");
            System.out.println("[1] Gerar arquivo animes.db");
            System.out.println("[0] Sair do programa");

            opcao = input.nextInt();
        }
    }

    public void exibirMenuPrincipal() throws IOException {
        System.out.println("\n==X==X== MENU PRINCIPAL ==X==X==");
        System.out.println("[1] Acessar o Menu CRUD");
        System.out.println("[2] Acessar o Menu Ordenação Externa");
        System.out.println("[3] Acessar o Menu Indexação");
        System.out.println("[0] Sair do programa");

        opcao = input.nextInt();

        if (opcao != 0) {
            menuPrincipal();
        }
    }

    public void menuPrincipal() throws IOException {
        switch (opcao) {
            case 1:
                exibirMenuCRUD();
                break;
            case 2:
                exibirMenuOrdenacao();
                break;
            case 3:
                exibirMenuIndexacao();
                break;
            default:
                System.out.println("Opção inválida! Tente novamente!");
        }
    }

    public void exibirMenuCRUD() throws IOException {
        System.out.println("\n==X==X== MENU CRUD ==X==X==");
        System.out.println("[1] Criar um novo Anime");
        System.out.println("[2] Ler as informações de um Anime");
        System.out.println("[3] Atualizar um Anime existente");
        System.out.println("[4] Deletar um Anime");
        System.out.println("[5] Listar todos os Animes em um arquivo .txt");
        System.out.println("[0] Voltar");

        opcao = input.nextInt();

        // Chamar o menu com as configurações internas de CRUD
        if (opcao != 0) {
            menuCRUD();
        }
    }

    public void menuCRUD() throws IOException {
        switch (opcao) {
            case 1:
                System.out.println("\nVocê quer criar um novo registro. Vamos começar!");
                menuCreateRegistro();
                break;
            case 2:
                System.out.println("\nVocê quer pesquisar o ID de um registro. Vamos lá!");
                menuReadRegistro();
                break;
            case 3:
                System.out.println("\nVocê quer atualizar as informações de um registro.");
                menuUpdateRegistro();
                break;
            case 4:
                System.out.println("\nVocê quer deletar um registro. Tome cuidado!");
                menuDeleteRegistro();
                break;
            case 5:
                menuListarRegistro();
                break;
            default:
                System.out.println("\nOpção inválida! Tente novamente.");
        }
    }

    public Anime menuCreateRegistro() {
        Crud crud = new Crud();

        Anime anime = new Anime();
        input.nextLine();
        System.out.println("[1/6] Digite o NOME do novo Anime:");
        anime.setNome(input.nextLine());
        System.out.println("[2/6] Digite o SCORE do novo Anime:");
        anime.setScore(input.nextFloat());
        input.nextLine();
        System.out.println("[3/6] Digite os GÊNEROS do novo Anime:");
        anime.setGenres(input.nextLine());
        System.out.println("[4/6] Digite o TIPO do novo Anime:");
        anime.setType(input.nextLine());
        System.out.println("[5/6] Digite a quantidade de EPISÓDIOS do novo Anime:");
        anime.setEpisodes(input.nextInt());
        input.nextLine();
        System.out.println("[6/6] Digite a DATA de exibição do novo Anime (AAAA-MM-DD):");
        anime.setAired(input.nextLine());

        if (crud.createRegistro(enderecoDB, anime)) {
            System.out.println("\nSeu novo Anime foi criado!");

            return anime;
        } else {
            System.out.println("\nHouve um erro no programa. Tente novamente!");
        }

        return null;
    }

    public void menuReadRegistro() throws IOException {
        Crud crud = new Crud();

        Registro registro = new Registro();
        System.out.println("Digite o ID do Anime que você quer pesquisar:");
        int idPesquisa = input.nextInt();

        registro = crud.readRegistro(enderecoDB, idPesquisa); // Retorna 'null' caso não encontre

        if (registro != null) {
            System.out.println("\n•._.••´¯``•.,,.•` REGISTRO ENCONTRADO! `•.,,.•´´¯`••._.•");
            System.out.println(registro.getAnime().toString(registro.getAnime().getAired()));
        } else {
            System.out.println("\nRegistro não encontrado! Tente novamente!");
        }
    }

    public ArrayList<Anime> menuUpdateRegistro() throws IOException {
        Crud crud = new Crud();

        Registro registro = new Registro();
        Anime animeAtualizado = new Anime();
        System.out.println("Digite o ID do Anime que você quer atualizar:");
        int idUpdate = input.nextInt();

        registro = crud.readRegistro(enderecoDB, idUpdate);

        if (registro != null) {
            System.out.println("\n•._.••´¯``•.,,.•` REGISTRO ENCONTRADO! `•.,,.•´´¯`••._.•");
            System.out.println(registro.getAnime().toString(registro.getAnime().getAired()));
            animeAtualizado = registro.getAnime();

            System.out.println("\nQual informação você deseja atualizar?");
            System.out.println("1 - Nome");
            System.out.println("2 - Score");
            System.out.println("3 - Genres");
            System.out.println("4 - Type");
            System.out.println("5 - Episodes");
            System.out.println("6 - Aired");

            int opcao = input.nextInt();

            switch (opcao) {
                case 1:
                    input.nextLine();
                    System.out.println("Digite o novo NOME do Anime:");
                    animeAtualizado.setNome(input.nextLine());
                    break;
                case 2:
                    System.out.println("Digite o novo SCORE do Anime:");
                    animeAtualizado.setScore(input.nextFloat());
                    break;
                case 3:
                    input.nextLine();
                    System.out.println("Digite os novos GÊNEROS do Anime:");
                    animeAtualizado.setGenres(input.nextLine());
                    break;
                case 4:
                    input.nextLine();
                    System.out.println("Digite o novo TIPO do novo Anime:");
                    animeAtualizado.setType(input.nextLine());
                    break;
                case 5:
                    System.out.println("Digite a quantidade nova de EPISÓDIOS do Anime:");
                    animeAtualizado.setEpisodes(input.nextInt());
                    break;
                case 6:
                    input.nextLine();
                    System.out.println("Digite a nova DATA de exibição do Anime (AAAA-MM-DD):");
                    animeAtualizado.setAired(input.nextLine());
                    break;
            }

            if (crud.updateRegistro(enderecoDB, animeAtualizado)) {
                System.out.println("\nO registro de ID " + idUpdate + " foi atualizado!");

                ArrayList<Anime> resultadoUpdate = new ArrayList<Anime>();
                resultadoUpdate.add(registro.getAnime());
                resultadoUpdate.add(animeAtualizado);

                return resultadoUpdate;
            } else {
                System.out.println("\nAlgo deu errado! O registro de ID " + idUpdate + " não foi atualizado.");
            }
        } else {
            System.out.println("\nRegistro não encontrado! Tente novamente!");
        }

        return null;
    }

    public void menuDeleteRegistro() {
        Crud crud = new Crud();

        System.out.println("Digite o ID do Anime que você quer deletar:");
        int idDelete = input.nextInt();

        if (crud.deleteRegistro(enderecoDB, idDelete)) {
            System.out.println("\nO registro de ID " + idDelete + " foi deletado!");
        } else {
            System.out.println("\nO registro de ID " + idDelete + " não existe em nosso sistema!");
        }
    }

    public void menuListarRegistro() {
        try {
            // Passar o path dos arquivos DB e TXT, respectivamente
            escreverArquivoTXT(enderecoDB, enderecoTXT);
            System.out.println("\n•._.••´¯``•.,,.•` SEU ARQUIVO .TXT DE ANIMES FOI CRIADO! `•.,,.•´´¯`••._.•");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void exibirMenuOrdenacao() throws IOException {
        System.out.println("\n==X==X== MENU ORDENAÇÃO EXTERNA ==X==X==");
        System.out.println("Você deseja fazer a Ordenação Externa por qual método?");
        System.out.println("[1] intercalacao balanceada comum");
        System.out.println("[2] intercalacao balanceada com blocos de tamanho variavel");
        System.out.println("[3] intercalacao balanceada com seleção por substituição");
        System.out.println("[0] Voltar");

        opcao = input.nextInt();

        // Chamar o menu que controla a Ordenação Externa
        if (opcao != 0) {
            menuOrdenacao();
        }
    }

    public void menuOrdenacao() throws IOException {
        switch (opcao) {
            case 1:
                System.out.println("\nintercalacao BALANCEADA COMUM SELECIONADA!");
                OrdenacaoExterna intercalacaoSimples = new OrdenacaoExterna();
                intercalacaoSimples.intercalacaoBalanceadaComum();
                break;
            case 2:
                System.out.println("\nintercalacao BALANCEADA COM BLOCOS DE TAMANHO variavel SELECIONADA!");
                OrdenacaoExterna intercalacaoVariavel = new OrdenacaoExterna();
                intercalacaoVariavel.intercalacaoBalanceadaVariavel();
                break;
            case 3:
                System.out.println("\nintercalacao BALANCEADA COM SELEÇÃO POR SUBSTITUIÇÃO SELECIONADA!");
                OrdenacaoExterna intercalacaoSubstituicao = new OrdenacaoExterna();
                intercalacaoSubstituicao.intercalacaoBalanceadaSubstituicao();
                break;
            default:
                System.out.println("\nOpção inválida! Tente novamente.");
        }
    }

    public void exibirMenuIndexacao() throws IOException {
        System.out.println("\n==X==X== MENU INDEXAÇÃO ==X==X==");
        System.out.println("Você deseja acessar qual Estrutura de Indexação?");
        System.out.println("[1] Hash Extensível");
        System.out.println("[2] Lista Invertida");
        System.out.println("[0] Voltar");

        opcao = input.nextInt();

        // Chamar o menu que levará à uma Estrutura de Indexação
        if (opcao != 0) {
            menuIndexacao();
        }
    }

    public void menuIndexacao() throws IOException {
        switch (opcao) {
            case 1:
                // exibirMenuHash();
                break;
            case 2:
                exibirMenuLista();
                break;
            default:
                System.out.println("Opção inválida! Tente novamente!");
        }
    }

    public void exibirMenuLista() throws IOException {
        try {
            // Antes de exibir o Menu Lista Invertida, deve-se criar os dois arquivos de
            // lista solicitados, tanto de nomes quanto de tipos
            System.out.println("\n[ Aguarde... Criando Listas Invertidas... ]");
            IndexacaoLista listaInvertida = new IndexacaoLista();
            listaInvertida.criarListaInvertidaNomes();
            listaInvertida.criarListaInvertidaTypes();

            System.out.println("\n•._.••´¯``•.,,.•` OS ARQUIVOS DE LISTA INVERTIDA FORAM CRIADOS! `•.,,.•´´¯`••._.•");

            // Depois de criar os arquivos de maneira correta, o programa pode continuar
            System.out.println("\n==X==X== MENU LISTA INVERTIDA ==X==X==");
            System.out.println("Você deseja realizar qual operação na Lista Invertida?");
            System.out.println("[1] Pesquisar um Anime");
            System.out.println("[2] Criar um Anime [Lista Invertida]");
            System.out.println("[3] Atualizar um Anime [Lista Invertida]");
            System.out.println("[4] Deletar um Anime [Lista Invertida]");
            System.out.println("[0] Voltar");

            opcao = input.nextInt();

            // Chamar o menu que controla a Lista Invertida
            if (opcao != 0) {
                menuLista();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void menuLista() throws IOException {
        switch (opcao) {
            case 1:
                IndexacaoLista liRead = new IndexacaoLista();
                System.out.println("\nQual o NOME do Anime que você quer pesquisar? [OPCIONAL]");
                input.nextLine();
                String pesquisaNome = input.nextLine();
                System.out.println("\nQual o TYPE do Anime que você quer pesquisar? [OPCIONAL]");
                String pesquisaType = input.nextLine();

                liRead.pesquisaListaInvertida(pesquisaNome, pesquisaType);
                break;
            case 2:
                IndexacaoLista liCreate = new IndexacaoLista();
                Anime animeCreate = menuCreateRegistro();
                if (animeCreate != null) {
                    liCreate.createTupla(animeCreate);
                }
                break;
            case 3:
                IndexacaoLista liUpdate = new IndexacaoLista();
                ArrayList<Anime> animeUpdate = menuUpdateRegistro();
                if (animeUpdate != null) {
                    liUpdate.updateTupla(animeUpdate);
                }
                break;
            case 4:
                IndexacaoLista liDelete = new IndexacaoLista();
                Crud crudDelete = new Crud();
                System.out.println("\nQual o ID do Anime que você quer deletar?");
                int idDel = input.nextInt();
                Registro registro = crudDelete.readRegistro("dados/animes.db", idDel);

                if (registro != null) {
                    crudDelete.deleteRegistro("dados/animes.db", idDel);
                    liDelete.deleteTupla(registro.getAnime());
                    System.out.println("\nAnime de ID " + idDel + " foi deletado!");
                } else {
                    System.out.println("\nO Anime de ID " + idDel + "não existe em nossas Listas Invertidas!");
                }
                break;
            default:
                System.out.println("Opção inválida! Tente novamente!");
        }
    }

    // Funções para lidar com os arquivos externos
    public void escreverArquivoDB(String enderecoDB, String enderecoCSV) throws Exception {
        // Abrir o arquivo ANIMES.DB para leitura e escrita
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw");

        byte[] ba;

        // Chamar a função que lê o arquivo .csv
        Anime[] animes = lerArquivoCSV(enderecoCSV);

        // Armazenar o último ID baseado em nosso .csv
        int ultimoId;
        ultimoId = animes[animes.length - 1].idAnime;

        arq.writeInt(ultimoId);

        for (Anime anime : animes) {
            Registro registro = new Registro(anime);

            ba = registro.toByteArray(); // Converter o registro para bytes

            arq.write(ba);
        }

        arq.close();
    }

    public void escreverArquivoTXT(String enderecoDB, String enderecoTXT) throws Exception {
        // Abrir o arquivo ANIMES.DB somente para leitura
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        // Abrir o arquivo .txt para escrita
        BufferedWriter writer = new BufferedWriter(new FileWriter(enderecoTXT));

        byte[] ba;
        int idTeste;
        int len;
        boolean lapis;

        long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início
        arq.seek(ponteiroBase + 4); // Saltar os bytes com a informação do maior ID

        // Enquanto não chegar no fim do arquivo, a repetição acontece
        while (ponteiroBase < arq.length()) {
            idTeste = arq.readInt();
            lapis = arq.readBoolean();
            len = arq.readInt();

            ba = new byte[len];
            arq.read(ba);

            // Gerar o objeto Registro
            Registro registro = new Registro();
            registro.fromByteArray(idTeste, lapis, len, ba);

            ponteiroBase = arq.getFilePointer();

            // Escrever no arquivo de impressão o resultado
            writer.write(registro.toString(ba));
            writer.newLine();
        }

        writer.close();
        arq.close();
    }

    public static String header; // variavel para separar os cabeçalhos

    public static String[] dividirLinhaCSV(String linha) {
        List<String> colunas = new ArrayList<>();
        boolean entreAspas = false;
        StringBuilder colunaAtual = new StringBuilder();

        // Repetição para quebrar os parâmetros do .csv
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

        // Adicionar o resultado em formato de String
        colunas.add(colunaAtual.toString());

        return colunas.toArray(new String[0]);
    }

    public static Anime[] lerArquivoCSV(String caminhoCSV) {
        ArrayList<Anime> animeList = new ArrayList<>();

        try {
            BufferedReader leitorCSV = new BufferedReader(new FileReader(caminhoCSV));
            header = leitorCSV.readLine(); // Separar os cabeçalhos
            String linha;

            while ((linha = leitorCSV.readLine()) != null) {
                String[] data = dividirLinhaCSV(linha);
                // Quebrar os dados obtidos na quantidade de parâmetros
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