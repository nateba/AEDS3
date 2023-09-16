import java.io.*;

public class Crud {

    public boolean createRegistro(String enderecoDB, Anime anime) {
        try {
            RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw"); // Abre arquivo para leitura e escrita
            byte[] ba;
            arq.seek(0); // Posiciona o ponteiro no início do arquivo
            int ultimoId = arq.readInt(); // Lê o último id
            anime.idAnime = ultimoId + 1; // Atribui o próximo id ao anime
            Registro registro = new Registro(anime, ultimoId + 1); // Cria o registro com o próximo id
            ba = registro.toByteArray();
            arq.seek(0); // Volta o ponteiro para o início do arquivo
            arq.writeInt(ultimoId + 1); // Atualiza o maior ID do arquivo
            arq.seek(arq.length()); // Posiciona o ponteiro no final do arquivo
            arq.write(ba); // Escreve o registro no arquivo
            arq.close();
            return true; // Retorna true se o registro foi criado com sucesso
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Registro readRegistro(String enderecoDB, int idRegistro) throws IOException {
        // Leitura dos registros
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r")) {
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
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();

                ponteiroBase = arq.getFilePointer();

                arq.seek(ponteiroBase + tamanhoRegistro);

                ponteiroBase = arq.getFilePointer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean updateRegistro(String enderecoDB, Anime animeAtualizado) {
        // Abre arquivo para leitura e escrita
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw");) {
            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            while (ponteiroBase < arq.length()) {
                // Conferir se é o mesmo ID do anime passado por parâmetro
                long posicao = arq.getFilePointer();
                if (arq.readInt() == animeAtualizado.idAnime) {
                    // Conferir se o arquivo não foi apagado
                    if (arq.readBoolean() == false) { // Checagem dupla lápide
                        int tamanho = arq.readInt(); // Lê o tamanho do registro
                        if (animeAtualizado.toByteArray().length == tamanho) {
                            arq.write(animeAtualizado.toByteArray());
                        } else if (animeAtualizado.toByteArray().length < tamanho) {
                            arq.write(animeAtualizado.toByteArray());
                            int dif = tamanho - animeAtualizado.toByteArray().length;
                            for (int i = 0; i < dif; i++) {
                                arq.writeByte(0);
                            }
                        } else {
                            arq.seek(posicao + 4);
                            arq.writeBoolean(true);
                            arq.seek(arq.length());
                            Registro registro = new Registro(animeAtualizado);
                            arq.write(registro.toByteArray());
                        }
                        return true;
                    } else {
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();

                ponteiroBase = arq.getFilePointer();

                arq.seek(ponteiroBase + tamanhoRegistro);

                ponteiroBase = arq.getFilePointer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteRegistro(String enderecoDB, int idRegistro) {
        // Ler o arquivo com todos os registros
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw")) {
            // Endereço do ponteiro de início
            long ponteiroBase = arq.getFilePointer();
            arq.seek(ponteiroBase + 4);

            while (ponteiroBase < arq.length()) {
                // Conferir se é o mesmo ID passado por parâmetro
                if (arq.readInt() == idRegistro) {
                    long posicao = arq.getFilePointer(); // Armazenar a posição da lápide
                    // Conferir se o arquivo não foi apagado
                    if (arq.readBoolean() == false) {
                        arq.seek(posicao);

                        arq.writeBoolean(true);

                        return true;
                    } else {
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();

                ponteiroBase = arq.getFilePointer();

                arq.seek(ponteiroBase + tamanhoRegistro);

                ponteiroBase = arq.getFilePointer();
            }

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
