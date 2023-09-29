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
                if (resultHash == diretorio.getBucket().getProfundidadeLocal()) {
                    if (diretorio.getBucket().getRegistros().size() < tamMaxBucket) {
                        diretorio.getBucket().getRegistros().add(registro);
                    }

                }
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

            li.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
