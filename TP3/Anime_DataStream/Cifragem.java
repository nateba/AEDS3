public class Cifragem {

    public void cifrarVinhete(String chave) throws Exception {

        System.out.println("\n•._.••´¯``•.,,.•` CIFRAGEM - ALGORITMO [VIGENERE] `•.,,.•´´¯`••._.•\n");

        Vinhete vinhete = new Vinhete();
        if (!vinhete.setChave(chave)) {
            System.out.println("\nInfelizmente, o padrão não foi encontrado!");
        }
    }

    public void cifrarColuna(String chave) throws Exception {

        System.out.println("\n•._.••´¯``•.,,.•` CIFRAGEM - ALGORITMO [TRANSPOSICAO] `•.,,.•´´¯`••._.•\n");

        CifraColuna cifracoluna = new CifraColuna();
        if (!cifracoluna.setChave(chave)) {
            System.out.println("\nInfelizmente, o padrão não foi encontrado!");
        }
    }
}
