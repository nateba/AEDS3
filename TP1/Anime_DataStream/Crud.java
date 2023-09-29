import java.io.*;

public class Crud {

    public boolean createRegistro(String enderecoDB, Anime anime) {
        try {
            // Abrir o arquivo ANIMES.DB para leitura e escrita
            RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw");
            byte[] ba;
            arq.seek(0); // Posiciona o ponteiro no início do arquivo
            int ultimoId = arq.readInt(); // Lê o último ID no início do arquivo de registros
            anime.idAnime = ultimoId + 1; // Atribui o próximo ID ao Anime
            Registro registro = new Registro(anime, ultimoId + 1); // Cria o registro com o próximo ID
            ba = registro.toByteArray();
            arq.seek(0); // Volta o ponteiro para o início do arquivo de registros
            arq.writeInt(ultimoId + 1); // Atualiza o maior ID do arquivo
            arq.seek(arq.length()); // Posiciona o ponteiro no final do arquivo de registros
            arq.write(ba); // Escreve o registro no arquivo de registros
            arq.close();

            return true; // Retorna 'true' se o registro foi criado com sucesso
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
    }

    public Registro readRegistro(String enderecoDB, int idRegistro) throws IOException {
        // Abrir o arquivo ANIMES.DB somente para leitura
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "r")) {

            long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início
            arq.seek(ponteiroBase + 4); // Saltar os bytes com a informação do maior ID

            // Enquanto não chegar no fim do arquivo, a repetição acontece
            while (ponteiroBase < arq.length()) {
                // Conferir se é o mesmo ID passado por parâmetro
                if (arq.readInt() == idRegistro) {
                    // Conferir se o arquivo não foi apagado
                    if (arq.readBoolean() == false) {
                        int tamanho = arq.readInt();

                        byte[] ba = new byte[tamanho];
                        arq.read(ba);

                        // Cria um objeto Registro com o registro procurado
                        Registro registro = new Registro();
                        registro.fromByteArray(idRegistro, false, tamanho, ba);

                        return registro; // Retorna o registro encontrado
                    } else {
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                // Os códigos abaixo servem para saltar os bytes dos registros que não precisam
                // ser lidos
                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();
                ponteiroBase = arq.getFilePointer();
                arq.seek(ponteiroBase + tamanhoRegistro); // Salta os bytes
                ponteiroBase = arq.getFilePointer(); // Atualiza o valor do Ponteiro Base
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Retorna 'null' caso não encontre nada
    }

    public boolean updateRegistro(String enderecoDB, Anime animeAtualizado) {
        // Abrir o arquivo ANIMES.DB somente para leitura
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw");) {

            long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início
            arq.seek(ponteiroBase + 4); // Saltar os bytes com a informação do maior ID

            // Enquanto não chegar no fim do arquivo, a repetição acontece
            while (ponteiroBase < arq.length()) {

                long posicao = arq.getFilePointer(); // Armazenar a posição do início do registro

                // Conferir se é o mesmo ID passado por parâmetro
                if (arq.readInt() == animeAtualizado.idAnime) {
                    // Conferir se o arquivo não foi apagado
                    if (arq.readBoolean() == false) {
                        int tamanho = arq.readInt(); // Lê o tamanho do registro

                        // Conferir se a atualização gerou alterações no tamanho do registro
                        if (animeAtualizado.toByteArray().length == tamanho) {
                            arq.write(animeAtualizado.toByteArray());
                        } else {
                            // Onde gerar variação de tamanho, colocar lápide no registro e gravar o
                            // atualizado no final do arquivo
                            arq.seek(posicao + 4);
                            arq.writeBoolean(true);
                            arq.seek(arq.length());
                            Registro registro = new Registro(animeAtualizado);
                            arq.write(registro.toByteArray());
                        }

                        return true;
                    } else {
                        // Caso seja lápide, o ponteiro volta um byte
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                // Os códigos abaixo servem para saltar os bytes dos registros que não precisam
                // ser lidos
                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();
                ponteiroBase = arq.getFilePointer();
                arq.seek(ponteiroBase + tamanhoRegistro); // Salta os bytes
                ponteiroBase = arq.getFilePointer(); // Atualiza o valor do Ponteiro Base
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Retorna 'false' caso a operação dê errado
    }

    public boolean deleteRegistro(String enderecoDB, int idRegistro) {
        // Abrir o arquivo ANIMES.DB somente para leitura
        try (RandomAccessFile arq = new RandomAccessFile(enderecoDB, "rw")) {

            long ponteiroBase = arq.getFilePointer(); // Pegar o endereço do ponteiro de início
            arq.seek(ponteiroBase + 4); // Saltar os bytes com a informação do maior ID

            // Enquanto não chegar no fim do arquivo, a repetição acontece
            while (ponteiroBase < arq.length()) {
                // Conferir se é o mesmo ID passado por parâmetro
                if (arq.readInt() == idRegistro) {
                    long posicao = arq.getFilePointer(); // Armazenar a posição da lápide do registro
                    // Conferir se o arquivo não foi apagado
                    if (arq.readBoolean() == false) {
                        arq.seek(posicao); // Retornar para a posição da lápide

                        arq.writeBoolean(true); // Colocar uma lápide no arquivo

                        return true;
                    } else {
                        // Caso seja lápide, o ponteiro volta um byte
                        ponteiroBase = arq.getFilePointer();
                        arq.seek(ponteiroBase - 1);
                    }
                }

                // Os códigos abaixo servem para saltar os bytes dos registros que não precisam
                // ser lidos
                // A repetição continua mesmo após encontrar uma lápide, pois o registro pode
                // ter sido alterado e colocado no final do arquivo de registros
                arq.readBoolean();
                int tamanhoRegistro = arq.readInt();
                ponteiroBase = arq.getFilePointer();
                arq.seek(ponteiroBase + tamanhoRegistro); // Salta os bytes
                ponteiroBase = arq.getFilePointer(); // Atualiza o valor do Ponteiro Base
            }

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false; // Retorna 'false' caso a operação dê errado
    }
}
