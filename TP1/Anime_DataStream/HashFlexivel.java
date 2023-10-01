
import java.util.*;

public class HashFlexivel {
    int tamanho;
    ArrayList<Diretorio> diretorios;

    public HashFlexivel(int tamanho, ArrayList<Diretorio> diretorios) {
        this.tamanho = tamanho;
        this.diretorios = diretorios;
    }

    public HashFlexivel() {

        this.tamanho = 1;

        for (int i = 0; i < Math.pow(2, tamanho); i++) {
            Diretorio diretorio = new Diretorio();
            this.diretorios.add(diretorio);
        }

    }

    public int getTamanho() {
        return tamanho;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }

    public ArrayList<Diretorio> getDiretorios() {
        return diretorios;
    }

    public void setDiretorios(ArrayList<Diretorio> diretorios) {
        this.diretorios = diretorios;
    }

}
