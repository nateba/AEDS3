import java.io.*;
import java.text.*;
import java.util.*;

public class Registro extends Crud {
    private int idRegistro;
    private boolean lapide;
    private int tamanho;
    private Anime anime;

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public boolean isLapide() {
        return lapide;
    }

    public void setLapide(boolean lapide) {
        this.lapide = lapide;
    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public Registro() {
        idRegistro = -1;
        lapide = false;
        tamanho = -1;
        anime = new Anime();
    }

    public Registro(Anime anime) throws IOException {
        idRegistro = anime.idAnime;
        lapide = false;
        tamanho = anime.toByteArray().length;
        this.anime = anime;
    }

    public Registro(Anime anime, int id) throws IOException {
        idRegistro = id;
        lapide = false;
        tamanho = anime.toByteArray().length;
        this.anime = anime;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(idRegistro);
        dos.writeBoolean(lapide);
        dos.writeInt(tamanho);
        dos.write(anime.toByteArray());

        return baos.toByteArray();
    }

    public void fromByteArray(int id, boolean lapis, int len, byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);

        idRegistro = id;
        lapide = lapis;
        tamanho = len;

        anime = anime.fromByteArray(dis.readNBytes(tamanho));
    }

    @Override
    public String toString() {
        return "Registro [idRegistro=" + idRegistro + ", lapide=" + lapide + ", tamanho=" + tamanho + ", anime=" + anime
                + "]";
    }
}
