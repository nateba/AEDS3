public class No {
    private int segmento; // Armazenar o valor do segmento para o Heap
    private Registro registro;

    public No() {
        this.segmento = 0;
        this.registro = new Registro();
    }

    public No(int segmento) {
        this.segmento = segmento;
        this.registro = new Registro();
    }

    public No(int segmento, Registro registro) {
        this.segmento = segmento;
        this.registro = registro;
    }

    public int getSegmento() {
        return segmento;
    }

    public void setSegmento(int segmento) {
        this.segmento = segmento;
    }

    public Registro getRegistro() {
        return registro;
    }

    public void setRegistro(Registro registro) {
        this.registro = registro;
    }
}