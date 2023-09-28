import java.util.*;

public class Bucket {
    private int profundidadeLocal;
    private ArrayList<Registro> registros;

    public Bucket() {
        this.profundidadeLocal = 1;
        this.registros = new ArrayList<>();
    }

    public int getProfundidadeLocal() {
        return profundidadeLocal;
    }

    public void setProfundidadeLocal(int profundidadeLocal) {
        this.profundidadeLocal = profundidadeLocal;
    }

    public ArrayList<Registro> getRegistros() {
        return registros;
    }

    public void setRegistros(ArrayList<Registro> registros) {
        this.registros = registros;
    }
}