public class CuerpoDeAgua extends ObjetoGeografico {

    private String nombre;
    private int Id = 0;
    private String municipio;
    private double irca = 0;

    public CuerpoDeAgua(String nombre, int Id, String municipio, double irca, String tipoDeAgua, String tipoDeCuerpo) {
        super(tipoDeAgua, tipoDeCuerpo);
        this.nombre = nombre;
        this.Id = Id;
        this.municipio = municipio;
        this.irca = irca;

    }

    public static String nivel(double irca) {

        if (irca >= 80.1 && irca <= 100) {
            return "INVIABLE SANITARIAMENTE";
        } else if (irca >= 35.1 && irca <= 80) {
            return "ALTO";
        } else if (irca >= 14.1 && irca <= 35) {
            return "MEDIO";
        } else if (irca >= 5.1 && irca <= 14) {
            return "BAJO";
        } else {
            return "SIN RIESGO";
        }
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public double getIrca() {
        return this.irca;
    }

    public void setIrca(double irca) {
        this.irca = irca;
    }

    @Override
    public String toString() {
        return "{" + " nombre='" + getNombre() + "'" + ", Id='" + getId() + "'" + ", municipio='" + getMunicipio() + "'"
                + ", irca='" + getIrca() + "'" + super.toString() + "}";
    }

}