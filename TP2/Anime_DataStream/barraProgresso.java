public class barraProgresso {
    private StringBuilder progresso;

    public barraProgresso() {
        init();
    }

    public void atualizar(int done, int total) {
        char[] charsProgresso = { '|', '/', '-', '\\' };
        String formatacao = "\r%3d%% %s %c";

        int porcentagem = (++done * 100) / total;
        int charsExtras = (porcentagem / 2) - this.progresso.length();

        while (charsExtras-- > 0) {
            progresso.append("loading");
        }

        System.out.printf(formatacao, porcentagem, progresso, charsProgresso[done % charsProgresso.length]);
        if (done == total) {
            System.out.flush();
            System.out.println();
            init();
        }
    }

    private void init() {
        this.progresso = new StringBuilder(60);
    }
}