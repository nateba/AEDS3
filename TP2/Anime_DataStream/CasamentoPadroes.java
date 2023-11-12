public class CasamentoPadroes {
    String caminhoArquivoDb = Menu.enderecoDB;

    public void pesquisarPadraoTextoKMP(String padrao) throws Exception {
        String stringBaseAnimes = BaseString.gerarStringDaBaseDeAnimes(caminhoArquivoDb);

        System.out.println("\n•._.••´¯``•.,,.•` CASAMENTO DE PADRÕES - ALGORITMO [KMP] `•.,,.•´´¯`••._.•\n");

        KMP kmp = new KMP();
        if (!kmp.KMPSearch(padrao, stringBaseAnimes)) {
            System.out.println("\nInfelizmente, o padrão não foi encontrado!");
        }
    }
}
