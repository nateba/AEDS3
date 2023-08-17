import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import java.io.IOException;
import java.text.DecimalFormat;

public class Anime {

    protected int idAnime;
    protected String nome;
    protected float score;
    protected String genres;
    protected String type;
    protected int episodes;
    protected long aired = System.currentTimeMillis() / 1000L;

    // Construtor
    public Anime(int i, String n, float s, String g, String t, int e, long a) {
        idAnime = i;
        nome = n;
        score = s;
        genres = g;
        type = t;
        episodes = e;
        aired = a;
    }

    // Construtor
    public Anime() {
        idAnime = -1;
        nome = "";
        score = 0F;
        genres = "";
        type = "";
        episodes = 0;
        aired = 0L;
    }

    // Devolve uma string com os dados do jogador
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");// formata o valor dos pontos
        return "\nID:" + idAnime +
                "\nNomes:" + nome +
                "\nScores:" + df.format(score) +
                "\nGenres:" + genres +
                "\nType:" + type +
                "\nEpisodes:" + episodes +
                "\nAired:" + aired;
    }

    public byte[] toByteArray() throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idAnime);
        dos.writeUTF(nome);
        dos.writeFloat(score);
        dos.writeUTF(genres);
        dos.writeUTF(type);
        dos.writeInt(episodes);
        dos.writeLong(aired);

        return baos.toByteArray();
    }

    public void fromByteArray(byte ba[]) throws IOException {

        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        idAnime = dis.readInt();
        nome = dis.readUTF();
        score = dis.readFloat();
        genres = dis.readUTF();
        type = dis.readUTF();
        episodes = dis.readInt();
        aired = dis.readLong();

    }
}