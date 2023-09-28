
public class Diretorio {
    private int profundidadeGlobal;
    private long enderecoBucket;

    public Diretorio() {
        this.profundidadeGlobal = 0;
        this.enderecoBucket = 0;
    }

    public int getProfundidadeGlobal() {
        return profundidadeGlobal;
    }

    public void setProfundidadeGlobal(int profundidadeGlobal) {
        this.profundidadeGlobal = profundidadeGlobal;
    }

    public long getEnderecoBucket() {
        return enderecoBucket;
    }

    public void setEnderecoBucket(long enderecoBucket) {
        this.enderecoBucket = enderecoBucket;
    }

}
