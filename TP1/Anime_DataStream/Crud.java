import java.io.IOException;
import java.io.RandomAccessFile;

public class Crud {
    // make a function to create a new register
    public static boolean createRegistro(String enderecoDB, Anime anime) {
        try {
            RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw"); // Abre arquivo para leitura e escrita
            byte[] ba;
            arq.seek(0); // Posiciona o ponteiro no início do arquivo
            int ultimoId = arq.readInt(); // Lê o último id
            Registro registro = new Registro(anime, ultimoId + 1); // Cria o registro com o próximo id
            ba = registro.toByteArray();
            arq.seek(arq.length()); // Posiciona o ponteiro no final do arquivo
            arq.write(ba); // Escreve o registro no arquivo
            arq.close();
            return true; // Retorna true se o registro foi criado com sucesso
        } catch (Exception e) {
            System.out.println("Erro ao criar registro");
            e.printStackTrace();
            return false;
        }
    }

    public Registro readRegistro(String enderecoDB, int idRegistro) throws IOException {
        // Leitura dos registros
        RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r");

        // Endereço do ponteiro de início
        long ponteiroBase = arq.getFilePointer();
        arq.seek(ponteiroBase + 4);

        while (ponteiroBase < arq.length()) {
            // Conferir se é o mesmo ID passado por parâmetro
            if (arq.readInt() == idRegistro) {
                // Conferir se o arquivo não foi apagado
                if (arq.readBoolean() == false) {
                    int tamanho = arq.readInt();

                    byte[] ba = new byte[tamanho];
                    arq.read(ba);

                    Registro registro = new Registro();
                    registro.fromByteArray(idRegistro, false, tamanho, ba);

                    return registro;
                } else {
                    return null;
                }
            }

            arq.readBoolean();
            int tamanhoRegistro = arq.readInt();

            ponteiroBase = arq.getFilePointer();

            arq.seek(ponteiroBase + tamanhoRegistro);

            ponteiroBase = arq.getFilePointer();
        }

        return null;
    }
}
