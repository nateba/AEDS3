
import java.io.*;
import java.text.*;
import java.util.*;

public class Anime {
    protected int idAnime;
    protected String nome;
    protected float score;
    protected String genres;
    protected String type;
    protected int episodes;
    protected long aired;

    DecimalFormat df = new DecimalFormat("#,##0.00");// formata o valor dos pontos

    public int getIdAnime() {
        return idAnime;
    }

    public void setIdAnime(int idAnime) {
        this.idAnime = idAnime;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public long getAired() {
        return aired;
    }

    public void setAired(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);
            aired = date.getTime() / 1000; // Converte mili -> sec
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Anime(int idAnime, String nome, float score, String genres, String type, int episodes, String airedDate) {
        this(); // Chama o construtor padrão para configurar campos padrões
        setIdAnime(idAnime);
        setNome(nome);
        setScore(score);
        setGenres(genres);
        setType(type);
        setEpisodes(episodes);
        setAired(airedDate); // Converte a data no formato "yyyy-MM-dd"
    }

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

    public void fromByteArray(byte[] ba) throws IOException {
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