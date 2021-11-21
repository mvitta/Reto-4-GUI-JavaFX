public class ObjetoGeografico {

    protected String tipoDeAgua;
    protected String tipoDeCuerpo;

    public ObjetoGeografico(String tipoDeAgua, String tipoDeCuerpo) {
        this.tipoDeAgua = tipoDeAgua;
        this.tipoDeCuerpo = tipoDeCuerpo;
    }

    public String getTipoDeAgua() {
        return this.tipoDeAgua;
    }

    public void setTipoDeAgua(String tipoDeAgua) {
        this.tipoDeAgua = tipoDeAgua;
    }

    public String getTipoDeCuerpo() {
        return this.tipoDeCuerpo;
    }

    public void setTipoDeCuerpo(String tipoDeCuerpo) {
        this.tipoDeCuerpo = tipoDeCuerpo;
    }

    @Override
    public String toString() {
        return " tipoDeAgua='" + getTipoDeAgua() + "'" + ", tipoDeCuerpo='" + getTipoDeCuerpo() + "'" + "}";
    }
}