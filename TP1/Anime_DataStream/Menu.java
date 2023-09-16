import java.io.*;
import java.util.*;

public class Menu {
    // Endreços dos arquivos externos
    String enderecoDB = "dados/animes.db";
    String enderecoCSV = "dados/animes_tratados.csv";
    String enderecoTXT = "dados/animes_impressos.txt";

    Scanner input = new Scanner(System.in);

    public void TratamentoMenu() throws IOException {
        exibirMenuInicial();

        int opcao = input.nextInt();

        // Caso opção digitada seja inválida
        while (opcao != 0 && opcao != 1) {
            System.out.println("Opção inválida! Tente uma opção existente:");
            System.out.println("1 - Gerar arquivo animes.db");
            System.out.println("0 - Sair do programa");

            opcao = input.nextInt();
        }

        // Caso opção inicial seja válida
        if (opcao == 1) {
            try {
                // Passar o path dos arquivos DB e CSV, respectivamente
                escreverArquivoDB(enderecoDB, enderecoCSV);
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("\nVocê acabou de gerar o arquivo animes.db!");
            System.out.println("Agora, vamos te dar algumas opções. O que você gostaria de fazer?");

            exibirMenuCRUD();

            opcao = input.nextInt();

            while (opcao != 0) {
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
                }

                System.out.println("\nEscolha outra opção:");
                exibirMenuCRUD();
                opcao = input.nextInt();
            }
        } else if (opcao == 0) {
            System.out.println("Obrigado por utilizar o nosso programa!");
        }

        input.close();
    }

    public void exibirMenuInicial() {
        System.out.println(
                " #####      ##      ####    ######            ####     ######              ##     ##  ##    ####    ##   ##  ######    ####\r\n"
                        +
                        " ##  ##    ####    ##  ##   ##                ## ##    ##                 ####    ### ##     ##     ### ###  ##       ##  ##\r\n"
                        +
                        " ##  ##   ##  ##   ##       ##                ##  ##   ##                ##  ##   ######     ##     #######  ##       ##\r\n"
                        +
                        " #####    ######    ####    ####              ##  ##   ####              ######   ######     ##     ## # ##  ####      ####\r\n"
                        +
                        " ##  ##   ##  ##       ##   ##                ##  ##   ##                ##  ##   ## ###     ##     ##   ##  ##           ##\r\n"
                        +
                        " ##  ##   ##  ##   ##  ##   ##                ## ##    ##                ##  ##   ##  ##     ##     ##   ##  ##       ##  ##\r\n"
                        +
                        " #####    ##  ##    ####    ######            ####     ######            ##  ##   ##  ##    ####    ##   ##  ######    ####\r\n"
                        +
                        "\r" +
                        "");

        System.out.println("Bem-vindo a nossa base de Animes do Trabalho Prático 1!");
        System.out.println("Antes de continuar, o que você gostaria de fazer?");
        System.out.println("1 - Gerar arquivo animes.db");
        System.out.println("0 - Sair do programa");
    }

    public void exibirMenuCRUD() {
        System.out.println("1 - Criar um novo Anime");
        System.out.println("2 - Ler as informações de um Anime");
        System.out.println("3 - Atualizar um Anime existente");
        System.out.println("4 - Deletar um Anime");
        System.out.println("5 - Listar todos os Animes em um arquivo .txt");
        System.out.println("0 - Sair do programa");
    }

    public void menuCreateRegistro() {
        Crud crud = new Crud();

        Anime anime = new Anime();
        input.nextLine();
        System.out.println("2.1 - Digite o NOME do novo Anime:");
        anime.setNome(input.nextLine());
        System.out.println("2.2 - Digite o SCORE do novo Anime:");
        anime.setScore(input.nextFloat());
        input.nextLine();
        System.out.println("2.3 - Digite os GÊNEROS do novo Anime:");
        anime.setGenres(input.nextLine());
        System.out.println("2.4 - Digite o TIPO do novo Anime:");
        anime.setType(input.nextLine());
        System.out.println("2.5 - Digite a quantidade de EPISÓDIOS do novo Anime:");
        anime.setEpisodes(input.nextInt());
        input.nextLine();
        System.out.println("2.6 - Digite a DATA de exibição do novo Anime (AAAA-MM-DD):");
        anime.setAired(input.nextLine());

        if (crud.createRegistro(enderecoDB, anime)) {
            System.out.println("\nSeu novo Anime foi criado!");
        } else {
            System.out.println("\nHouve um erro no programa. Tente novamente!");
        }
    }

    public void menuReadRegistro() throws IOException {
        Crud crud = new Crud();

        Registro registro = new Registro();
        System.out.println("Digite o ID do Anime que você quer pesquisar:");
        int idPesquisa = input.nextInt();

        registro = crud.readRegistro(enderecoDB, idPesquisa);

        if (registro != null) {
            System.out.println("\nRegistro encontrado!");
            System.out.println(registro.getAnime().toString(registro.getAnime().getAired()));
        } else {
            System.out.println("\nRegistro não encontrado! Tente novamente!");
        }
    }

    public void menuUpdateRegistro() throws IOException {
        Crud crud = new Crud();

        Registro registro = new Registro();
        Anime animeAtualizado = new Anime();
        System.out.println("Digite o ID do Anime que você quer atualizar:");
        int idUpdate = input.nextInt();

        registro = crud.readRegistro(enderecoDB, idUpdate);

        if (registro != null) {
            System.out.println("\nRegistro encontrado!");
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
            } else {
                System.out.println("\nAlgo deu errado! O registro de ID " + idUpdate + " não foi atualizado.");
            }
        } else {
            System.out.println("\nRegistro não encontrado! Tente novamente!");
        }
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
            System.out.println("\nSua lista de animes .txt foi criada!");
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    // Funções para lidar com os arquivos externos
    public void escreverArquivoDB(String enderecoDB, String enderecoCSV) throws Exception {
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

    public static void escreverArquivoTXT(String enderecoDB, String enderecoTXT) throws Exception {
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
            writer.write(registro.toString(ba));
            writer.newLine();
        }

        writer.close();
        arq.close();
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
