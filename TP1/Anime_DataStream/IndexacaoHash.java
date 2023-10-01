import java.io.*;
import java.util.ArrayList;

public class IndexacaoHash {
    public static int tamMaxBucket = 686; // 5% dos tamanhos dos registros

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
                } else {
                    // Caso 2: Profundidade local do bucket não corresponde à profundidade global
                    // Dividir o bucket em dois buckets
                    Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
                    dividirBucket(bucket, hashFlexivel);

                    // Recalcular o índice para o registro após a divisão
                    resultHash = Hash(registro, diretorio);
                    bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();

                    // Adicionar o registro ao novo bucket
                    bucket.getRegistros().add(registro);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void dividirBucket(Bucket bucket, HashFlexivel hashFlexivel) {
        int profundidadeLocal = bucket.getProfundidadeLocal();

        // Crie um novo bucket e atualize a profundidade local
        Bucket novoBucket = new Bucket();
        novoBucket.setProfundidadeLocal(profundidadeLocal + 1);

        // Atualize a profundidade local do bucket existente
        bucket.setProfundidadeLocal(profundidadeLocal + 1);

        // Redistribua os registros entre os dois buckets
        ArrayList<Registro> registrosOriginais = new ArrayList<>(bucket.getRegistros());
        bucket.getRegistros().clear();

        for (Registro registro : registrosOriginais) {
            int resultHash = Hash(registro, hashFlexivel.getDiretorios().get(0)); // Use o primeiro diretório para
                                                                                  // recalcular o índice

            if ((resultHash & (1 << profundidadeLocal)) == 0) {
                bucket.getRegistros().add(registro);
            } else {
                novoBucket.getRegistros().add(registro);
            }
        }

        // Atualize os diretórios com os novos buckets
        for (int i = 0; i < hashFlexivel.getTamanho(); i++) {
            Diretorio dir = hashFlexivel.getDiretorios().get(i);
            if (dir.getBucket() == bucket) {
                dir.setBucket(novoBucket);
            }
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

    public static Registro recuperarRegistro(int idRegistro, HashFlexivel hashFlexivel) {
        int resultHash = idRegistro % ((int) Math.pow(2, hashFlexivel.getDiretorios().get(0).getProfundidadeGlobal()));
        Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();

        for (Registro registro : bucket.getRegistros()) {
            if (registro.getIdRegistro() == idRegistro) {
                return registro;
            }
        }

        return null; // Registro não encontrado
    }

    public static void atualizarRegistro(Registro novoRegistro, HashFlexivel hashFlexivel) {
        int idRegistro = novoRegistro.getIdRegistro();
        int resultHash = idRegistro % ((int) Math.pow(2, hashFlexivel.getDiretorios().get(0).getProfundidadeGlobal()));
        Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();

        for (int i = 0; i < bucket.getRegistros().size(); i++) {
            Registro registro = bucket.getRegistros().get(i);
            if (registro.getIdRegistro() == idRegistro) {
                bucket.getRegistros().set(i, novoRegistro);
                return; // Registro atualizado com sucesso
            }
        }
    }

    public static boolean excluirRegistro(int idRegistro, HashFlexivel hashFlexivel) {
        int resultHash = idRegistro % ((int) Math.pow(2, hashFlexivel.getDiretorios().get(0).getProfundidadeGlobal()));
        Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();

        for (Registro registro : bucket.getRegistros()) {
            if (registro.getIdRegistro() == idRegistro) {
                bucket.getRegistros().remove(registro);
                return true; // Registro excluído com sucesso
            }
        }

        return false; // Registro não encontrado
    }

    public static void inserirRegistro(Registro registro, HashFlexivel hashFlexivel) {
        int resultHash = Hash(registro, hashFlexivel.getDiretorios().get(0)); // Use o primeiro diretório para calcular
                                                                              // o índice

        if (hashFlexivel.getDiretorios().get(resultHash).getBucket().getProfundidadeLocal() == hashFlexivel
                .getTamanho()) {
            // Caso 1: O bucket está cheio, então precisamos dividir e redistribuir
            Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
            if (bucket.getRegistros().size() < tamMaxBucket) {
                bucket.getRegistros().add(registro);
            } else {
                // Se o bucket estiver cheio, aumente a profundidade global
                aumentaProfundidade(hashFlexivel);
                // Recalcula o índice com a nova profundidade global
                resultHash = Hash(registro, hashFlexivel.getDiretorios().get(0));
                // Obtém o novo bucket
                bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
                // Adiciona o registro ao novo bucket
                bucket.getRegistros().add(registro);
            }
        } else {
            // Caso 2: Profundidade local do bucket não corresponde à profundidade global
            // Dividir o bucket em dois buckets
            Bucket bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
            if (bucket.getProfundidadeLocal() == hashFlexivel.getDiretorios().get(resultHash).getProfundidadeGlobal()) {
                // Se a profundidade local do bucket for igual à profundidade global do
                // diretório,
                // precisamos aumentar a profundidade global e redistribuir os registros
                aumentaProfundidade(hashFlexivel);
                // Recalcula o índice com a nova profundidade global
                resultHash = Hash(registro, hashFlexivel.getDiretorios().get(0));
                // Obtém o novo bucket
                bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();
            }

            // Agora dividimos o bucket
            dividirBucket(bucket, hashFlexivel);

            // Recalcular o índice para o registro após a divisão
            resultHash = Hash(registro, hashFlexivel.getDiretorios().get(0));
            bucket = hashFlexivel.getDiretorios().get(resultHash).getBucket();

            // Adicionar o registro ao novo bucket
            bucket.getRegistros().add(registro);
        }
    }

}
