import java.util.HashMap;

public class BoyerMoore {

    // --------------- Atributos ---------------

    private HashMap<Character, Integer> charRuim;
    private int[] sufBom;

    private String padrao;
    private int padraoLength;

    private int instancias;
    private int posPrimeiraInstancia;
    private int numOperacoes;
    private long tempoExecucao;

    private barraProgresso barra = new barraProgresso();

    // --------------- Construtor ---------------

    public BoyerMoore(String padrao) {
        this.charRuim = new HashMap<Character, Integer>();
        this.padrao = padrao;
        this.padraoLength = padrao.length();
        this.sufBom = new int[padraoLength];
        this.instancias = 0;
        this.posPrimeiraInstancia = 0;
        this.numOperacoes = 0;
        this.tempoExecucao = 0;
    }

    // --------------- Metodos ---------------

    public void setTabelas() {
        String sufixo = ""; // sufixo do padrao na posicao i
        String restoPadrao = ""; // padrao da posicao 0 ate i
        String aux = ""; // auxiliar para comparacoes
        int sufixoLength = 0; // tamanho do sufixo

        // char ruim
        for (int i = padraoLength - 2; i >= 0; i--) { // comeca no penultimo caractere do padrao
            charRuim.putIfAbsent(padrao.charAt(i), i); // se nao existir na tabela, adiciona
        }
        charRuim.put('*', -1); // adiciona o caractere * (default) na tabela

        // suf bom
        sufBom[padraoLength - 1] = 1; // ultimo char da tabela sempre sera 1

        for (int i = padraoLength - 2; i >= 0; i--) {
            sufixo = padrao.substring(i + 1); // pega o sufixo do padrao na posicao i
            restoPadrao = padrao.substring(0, i + 1); // pega o padrao da posicao 0 ate i
            sufixoLength = sufixo.length(); // tamanho do sufixo

            // caso 1:
            // o sufixo aparece de novo no padrao antecedido por um caractere diferente?
            // itera o padrao comecando pelo fim
            for (int j = (i + 1); j >= 0; j--) {
                aux = padrao.substring(j, (j + sufixoLength)); // pega o sufixo do padrao na posicao j

                // se o sufixo for igual ao auxiliar e o caractere anterior ao sufixo for
                // diferente do caractere anterior ao padrao
                if (j != 0 && (aux.equals(sufixo) && restoPadrao.charAt(j - 1) != padrao.charAt(i))) {
                    sufBom[i] = (i + 1) - j;
                    j = -1; // sai do loop
                } else if (j == 0 && aux.equals(sufixo)) { // se o sufixo for igual ao auxiliar e estivermos no primeiro
                                                           // char do padrao
                    sufBom[i] = (i + 1) - j;
                    j = -1; // sai do loop
                }
            }

            // se sufBom[i] ainda nao foi setado, caso 2:
            // parte do sufixo = prefixo?
            if (sufBom[i] == 0) {
                int index = i + 1; // index de inicio do sufixo

                while (index < (padraoLength - 1)) {
                    sufixo = sufixo.substring(1); // diminui o sufixo por 1
                    sufixoLength--;
                    index++; // se o sufixo diminuiu, o index aumenta
                    restoPadrao = padrao.substring(0, sufixoLength); // pega o prefixo do padrao com o mesmo tamanho do
                                                                     // sufixo

                    if (sufixo.equals(restoPadrao)) {
                        sufBom[i] = index;
                    }
                }
            }

            // se sufBom[i] ainda nao foi setado, caso 3:
            // o sufixo nao se repete no padrao?
            if (sufBom[i] == 0) {
                sufBom[i] = padraoLength; // sufBom[i] recebe o tamanho do padrao para pular o padrao inteiro
            }
        }
    }

    public void findPadrao(String banco) {
        int bancoLength = banco.length();
        int shift = 0;

        long startTime = System.currentTimeMillis(); // tempo inicial
        setTabelas(); // inicia as tabelas de charRuim e sufBom

        System.out.println("Procurando...");
        // itera o banco do inicio ao fim
        for (int i = 0; i < bancoLength; i++) {
            // itera o padrao comecando pelo final
            for (int j = (padraoLength - 1); j >= 0; j--) {
                if ((i + j) < (bancoLength - 1)) { // se o indice do banco nao estourar
                    if ((padrao.charAt(j) != banco.charAt(i + j))) { // se o caractere do padrao for diferente do
                                                                     // caractere do banco
                        if (charRuim.containsKey(banco.charAt(i + j))) { // se o caractere do banco estiver na tabela
                                                                         // charRuim
                            shift = Math.max(sufBom[j], (j - charRuim.get(banco.charAt(i + j)))); // shift recebe o
                                                                                                  // maior valor entre
                                                                                                  // as tabelas de
                                                                                                  // sufBom e charRuim
                        } else {
                            shift = Math.max(sufBom[j], (j - (-1))); // se o caractere do banco nao estiver na tabela
                                                                     // charRuim, usar -1 como default
                        }

                        if ((i + shift + padraoLength) <= bancoLength) { // se o indice do banco nao estourar
                            i += (shift - 1); // i recebe o shift - 1 para compensar o i++ do loop
                            numOperacoes++; // incrementa o numero de operacoes
                        } else {
                            i = bancoLength;
                        } // sai do loop
                        j = -1; // sai do loop
                    } else if (j == 0) { // se o caractere do padrao for igual ao caractere do banco e estivermos no
                                         // primeiro caractere do padrao
                        instancias++; // incrementa o numero de instancias
                        if (instancias == 1) { // se for a primeira instancia
                            posPrimeiraInstancia = i; // posPrimeiraInstancia recebe o indice do banco
                        }
                    }
                } else {
                    j = -1; // sai do loop
                }
            }
            barra.atualizar(i, bancoLength);
        }
        tempoExecucao = (System.currentTimeMillis() - startTime); // tempo de execução do programa em milissegundos
        System.out.println();
        System.out.println("Numero de instancias encontradas: " + instancias);
        System.out.println("Posicao da primeira instancia: " + posPrimeiraInstancia);
        System.out.println("Numero de operacoes: " + numOperacoes);
        System.out.println("Tempo de execucao: " + tempoExecucao + "ms");
    }
}
