import java.io.*;
import java.util.*;

public class Menu {
    // Endreços dos arquivos externos
    static String enderecoDB = "dados/animes.db";
    static String enderecoCSV = "dados/animes_tratados.csv";
    static String enderecoTXT = "dados/animes_impressos.txt";

    Scanner input = new Scanner(System.in);

    // A variável opção será colocada como global para poder ser acessada por todas
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
                "                                                                                                                   \r\n"
                        + //
                        "   _|_|    _|      _|  _|_|_|  _|      _|  _|_|_|_|    _|_|_|                      _|_|_|_|_|  _|_|_|      _|_|    \r\n"
                        + //
                        " _|    _|  _|_|    _|    _|    _|_|  _|_|  _|        _|                                _|      _|    _|  _|    _|  \r\n"
                        + //
                        " _|_|_|_|  _|  _|  _|    _|    _|  _|  _|  _|_|_|      _|_|        _|_|_|_|_|          _|      _|_|_|        _|    \r\n"
                        + //
                        " _|    _|  _|    _|_|    _|    _|      _|  _|              _|                          _|      _|          _|      \r\n"
                        + //
                        " _|    _|  _|      _|  _|_|_|  _|      _|  _|_|_|_|  _|_|_|                            _|      _|        _|_|_|_|  \r\n"
                        + //
                        "                                                                                                                   \r\n"
                        + //
                        "                                                                                                                   ");

        System.out.println("Bem-vindo a nossa base de Animes do Trabalho Prático 2!");
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

    public void exibirMenuPrincipal() throws Exception {
        System.out.println("\n==X==X== MENU PRINCIPAL ==X==X==");
        System.out.println("[1] Acessar o Menu Compressão e Descompressão");
        System.out.println("[2] Acessar o Menu Casamento de Padrões");
        System.out.println("[0] Sair do programa");

        opcao = input.nextInt();

        if (opcao != 0) {
            menuPrincipal();
        }
    }

    public void menuPrincipal() throws Exception {
        switch (opcao) {
            case 1:
                exibirMenuCompressao();
                break;
            case 2:
                exibirMenuCasamentoPadroes();
                break;
            default:
                System.out.println("Opção inválida! Tente novamente!");
        }
    }

    public void exibirMenuCompressao() throws Exception {
        System.out.println("\n==X==X== MENU COMPRESSÃO E DESCOMPRESSÃO ==X==X==");
        System.out.println("[1] Comprimir o arquivo 'animes.db' utilizando Huffman e LZW");
        System.out.println("[2] Descomprimir os arquivos utilizando Huffman e LZW");
        System.out.println("[0] Sair do programa");

        opcao = input.nextInt();

        if (opcao != 0) {
            menuCompressao();
        }
    }

    public void menuCompressao() throws Exception {
        switch (opcao) {
            case 1:
                Compressao compressao = new Compressao();
                compressao.comprimirArquivo();
                break;
            case 2:
                System.out.println("Qual versão do arquivo você deseja descompactar?");
                int versaoUsuario = input.nextInt();

                Descompressao descompressao = new Descompressao();
                descompressao.descomprimirArquivo(versaoUsuario);
                break;
            default:
                System.out.println("Opção inválida! Tente novamente!");
        }
    }

    public void exibirMenuCasamentoPadroes() throws Exception {
        System.out.println("\n==X==X== MENU CASAMENTO DE PADRÕES ==X==X==");
        System.out.println("[1] Pesquisar um padrão pelo algoritmo KMP");
        System.out.println("[2] Pesquisar um padrão pelo algoritmo Boyer-Moore");
        System.out.println("[0] Sair do programa");

        opcao = input.nextInt();

        if (opcao != 0) {
            menuCasamentoPadroes();
        }
    }

    public void menuCasamentoPadroes() throws Exception {
        switch (opcao) {
            case 1:
                System.out.println("Qual termo você está buscando?");
                input.nextLine();
                String padraoPesquisa = input.nextLine();

                if (padraoPesquisa.length() > 0) {
                    CasamentoPadroes casamentoPadroes = new CasamentoPadroes();
                    casamentoPadroes.pesquisarPadraoTextoKMP(padraoPesquisa);
                }
                break;
            case 2:
                // [IMPLEMENTAR]
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

    public static String header; // Variável para separar os cabeçalhos

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