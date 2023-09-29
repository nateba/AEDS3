
public class Diretorio {

    int profundidadeGlobal;
    private Bucket Bucket;

    public Diretorio() {
        this.profundidadeGlobal = 1;
        this.Bucket = new Bucket();
    }

    public int getProfundidadeGlobal() {
        return profundidadeGlobal;
    }

    public void setProfundidadeGlobal(int profundidadeGlobal) {
        this.profundidadeGlobal = profundidadeGlobal;
    }

    public Bucket getBucket() {
        return Bucket;
    }

    public void setBucket(Bucket Bucket) {
        this.Bucket = Bucket;
    }

}
