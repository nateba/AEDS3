import java.util.ArrayList;

public class ListaInvertida {
    private String termo;
    private ArrayList<Integer> listaDeIds; // Armazenar todos os IDs em uma lista

    public ListaInvertida() {
        this.termo = "";
        this.listaDeIds = new ArrayList<Integer>();
    }

    public ListaInvertida(String termo, int idRegistro) {
        this.termo = termo;
        this.listaDeIds = new ArrayList<Integer>();
        this.listaDeIds.add(idRegistro);
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public ArrayList<Integer> getIdRegistro() {
        return listaDeIds;
    }

    public void setIdRegistro(int idRegistro) {
        this.listaDeIds.add(idRegistro);
    }
}
