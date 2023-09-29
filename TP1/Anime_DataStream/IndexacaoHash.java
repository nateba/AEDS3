import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class IndexacaoHash {
    public static int tamMaxBucket = 686;

    public static ArrayList<Registro> geraListaRegistros() {
        try {
            RandomAccessFile arq = new RandomAccessFile("dados/animes.db", "r"); // Abre arquivo para leitura e escrita

            byte[] ba;
            int idRegistro;
            int len;
            boolean lapis;

            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            ArrayList<Registro> registros = new ArrayList<Registro>();

            while (ponteiroBase < arq.length()) {
                idRegistro = arq.readInt();
                lapis = arq.readBoolean();
                len = arq.readInt();

                ba = new byte[len];
                arq.read(ba);

                if (lapis == false) {
                    Registro registro = new Registro();
                    registro.fromByteArray(idRegistro, lapis, len, ba);

                    registros.add(registro);
                }

                ponteiroBase = arq.getFilePointer();
            }

            arq.close();

            return registros;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int Hash(Registro registro, Diretorio diretorio) {

        return registro.getIdRegistro() % ((int) Math.pow(2, diretorio.getProfundidadeGlobal()));
    }

    public static void criarHash() {
        try {

            ArrayList<Registro> registros = new ArrayList<Registro>();
            registros = geraListaRegistros();

            HashFlexivel hashFlexivel = new HashFlexivel();

            Diretorio diretorio = new Diretorio();

            for (Registro registro : registros) {

                int resultHash = Hash(registro, diretorio);

                if (hashFlexivel.getDiretorios().get(resultHash).getBucket().getProfundidadeLocal() == hashFlexivel
                        .getTamanho()) {
                    Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
                    if (bucket.getRegistros().size() < tamMaxBucket) {
                        bucket.getRegistros().add(registro);
                    } else {
                        // Se o bucket estiver cheio, aumente a profundidade global
                        aumentaProfundidade(hashFlexivel);
                        // Recalcula o índice com a nova profundidade global
                        resultHash = Hash(registro, diretorio);
                        // Obtém o novo bucket
                        bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
                        // Adiciona o registro ao novo bucket
                        bucket.getRegistros().add(registro);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void aumentaProfundidade(HashFlexivel hashFlexivel) {
        try {
            int novoTamanho = hashFlexivel.getTamanho() * 2; // Novo tamanho é o dobro do tamanho atual
            ArrayList<Diretorio> novosDiretorios = new ArrayList<>(novoTamanho);

            // Copia os diretórios existentes para os novos diretórios
            for (int i = 0; i < novoTamanho; i++) {
                if (i < hashFlexivel.getTamanho()) {
                    novosDiretorios.add(hashFlexivel.getDiretorios().get(i));
                } else {
                    novosDiretorios.add(hashFlexivel.getDiretorios().get(i - hashFlexivel.getTamanho()));
                }
            }

            // Atualiza o tamanho e os diretórios
            hashFlexivel.setTamanho(novoTamanho);
            hashFlexivel.setDiretorios(novosDiretorios);

            // Atualiza a profundidade global em todos os diretórios
            for (Diretorio diretorio : hashFlexivel.getDiretorios()) {
                diretorio.setProfundidadeGlobal(hashFlexivel.getTamanho());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void gerarArquivoHashFlexivel()
            throws IOException {
        try {

            RandomAccessFile arqIndice = new RandomAccessFile("arquivoIndice", "w"); // Criou um arquivo para escrita do
                                                                                     // indice
            RandomAccessFile arqHash = new RandomAccessFile("arqHash", "rw");

            Diretorio diretorio = new Diretorio();

            ArrayList<Registro> registros = geraListaRegistros();

            for (Registro registro : registros) {
                arqIndice.seek(0);
                arqIndice.writeInt(0);

            }

            arqIndice.close();
            arqHash.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}