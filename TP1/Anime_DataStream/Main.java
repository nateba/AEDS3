import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Menu menuDoPrograma = new Menu();

        menuDoPrograma.TratamentoMenu();

        IntercalacaoSubstituicao intercalacao = new IntercalacaoSubstituicao();
        intercalacao.intercalacaoSelecaoSubstituicao();
    }
}