
public class Diretorio {
    private int profundidadeGlobal;
    private Bucket enderecoBucket;

    public Diretorio() {
        this.profundidadeGlobal = 1;
        this.enderecoBucket = new Bucket();
    }

    public int getProfundidadeGlobal() {
        return profundidadeGlobal;
    }

    public void setProfundidadeGlobal(int profundidadeGlobal) {
        this.profundidadeGlobal = profundidadeGlobal;
    }

    public Bucket getEnderecoBucket() {
        return enderecoBucket;
    }

    public void setEnderecoBucket(Bucket enderecoBucket) {
        this.enderecoBucket = enderecoBucket;
    }

}
