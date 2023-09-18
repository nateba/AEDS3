import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Menu menuDoPrograma = new Menu();

        // menuDoPrograma.TratamentoMenu();

        IntercalacaoSimples intercalacao = new IntercalacaoSimples();
        intercalacao.intercalacaoBalanceada();
    }
}