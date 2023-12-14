public class Decifragem {
    public void decifrarVinhete(String chave) throws Exception {

        System.out.println("\n•._.••´¯``•.,,.•` CIFRAGEM - ALGORITMO [VIGENERE] `•.,,.•´´¯`••._.•\n");

        VinheteDecifra vinheteDecifra = new VinheteDecifra();
        if (!vinheteDecifra.setChave(chave)) {
            System.out.println("\nInfelizmente, o padrão não foi encontrado!");
        }
    }

    public void decifrarColuna(String chave) throws Exception {

        System.out.println("\n•._.••´¯``•.,,.•` CIFRAGEM - ALGORITMO [TRANSPOSICAO] `•.,,.•´´¯`••._.•\n");

        ColunaDecifra decifracoluna = new ColunaDecifra();
        if (!decifracoluna.setChave(chave)) {
            System.out.println("\nInfelizmente, o padrão não foi encontrado!");
        }
    }
}
