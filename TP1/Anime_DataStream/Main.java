import java.io.*;

public class Main {
    // Intruções para rodar o nosso programa:
    // 1. Abrir o terminal;
    // 2. Digitar javac *.java;
    // 3. Apertar Enter;
    // 4. Digitar java Main;
    // 5. Apertar Enter.
    public static void main(String[] args) throws IOException {
        // Criação de um objeto Menu
        Menu menuDoPrograma = new Menu();

        // Todo o programa será tratado por meio dos diferentes menus existentes
        menuDoPrograma.tratamentoMenu();
    }
}