import java.net.URL;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class InterfazController {

    @FXML
    private TabPane tabPane;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField textNombre;

    @FXML
    private TextField textID;

    @FXML
    private TextField textMunicipio;

    @FXML
    private TextField textTipoCuerpo;

    @FXML
    private TextField textTipoAgua;

    @FXML
    private TextField textIrca;

    @FXML
    private Button btnIngresar;

    @FXML
    private Button btnObtenerDatos;

    @FXML
    private Button btnProcesarDatos;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnComprobarConexion;

    @FXML
    protected static Label labelEstado;

    @FXML
    private TextArea areaObtenerDatos;

    @FXML
    private TextField textIdBuscar;

    @FXML
    private TextField muestraNombre;

    @FXML
    private TextField muestraMunicipio;

    @FXML
    private TextField muestraCuerpoAgua;

    @FXML
    private TextField muestraId;

    @FXML
    private TextField muestraTipoAgua;

    @FXML
    private TextField muestraIrca;

    @FXML
    private TextArea areaProcesarDatos;

    @FXML
    private Label estadoRegistro;

    @FXML

    ArrayList<CuerpoDeAgua> registros = new ArrayList<>();

    @FXML
    void eventoComprobarConexion(ActionEvent event) {
        Conexion.conectar();
    }

    public void ventanaEmergente(String ecabezado, String contenido, String urlImagen) {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setGraphic(new ImageView(urlImagen));
        a.setTitle("Error !");
        a.setHeaderText(ecabezado);
        a.setContentText(contenido);
        a.showAndWait();
    }

    @FXML
    void eventoBuscar(ActionEvent event) {

        if (this.textIdBuscar.getText().isEmpty()) {
            ventanaEmergente("", "No se permite un campo vacio", "busqueda.png");
        } else {
            try {
                int valorBusqueda = Integer.parseInt(this.textIdBuscar.getText());
                if (buscarIdArrayList(valorBusqueda) == -1) {
                    ventanaEmergente("", "El ID no existe en la base de datos", "busqueda.png");
                }

                String Sql = "SELECT Nombre, ID, Municipio, Tipo_de_cuerpo_de_agua, Tipo_de_agua, IRCA FROM cuerpos WHERE ID='"
                        + valorBusqueda + "'";

                try {
                    Statement stBuscar = Conexion.conectar().createStatement();
                    ResultSet rs = stBuscar.executeQuery(Sql);
                    while (rs.next()) {
                        this.muestraNombre.setText(rs.getString(1));
                        this.muestraId.setText(rs.getString(2));
                        this.muestraMunicipio.setText(rs.getString(3));
                        this.muestraCuerpoAgua.setText(rs.getString(4));
                        this.muestraTipoAgua.setText(rs.getString(5));
                        this.muestraIrca.setText(rs.getString(6));
                    }
                    bloquearTextFieldBusqueda(true);
                    bloquearBotones(false);
                } catch (Exception e) {
                    System.out.println("Error" + e.getMessage());
                }
            } catch (NumberFormatException e) {
                ventanaEmergente("", "debe ingresar un numero entero", "numeros.png");
            }
        }

    }

    @FXML
    void eventoEditar(ActionEvent event) {

        int ID = 0;
        double irca = 0;
        String estado = "";
        String error = "";
        // valida nombre
        String nombre = this.muestraNombre.getText();
        if (nombre.equals("") || !verificarCadena(nombre)) {
            error += mensajeError(this.muestraNombre);

        } else {
            estado += "Ok";
        }
        // valida municipio
        String municipio = this.muestraMunicipio.getText();
        if (municipio.equals("") || !verificarCadena(municipio)) {
            error += mensajeError(this.muestraMunicipio);
        } else {
            estado += "Ok";
        }
        // valida cuerpo de agua
        String tipoCuerpoAgua = this.muestraCuerpoAgua.getText();
        if (tipoCuerpoAgua.equals("") || !verificarCadena(tipoCuerpoAgua)) {
            error += mensajeError(this.muestraCuerpoAgua);
        } else {
            estado += "Ok";
        }
        // valida tipo de agua
        String tipoAgua = this.muestraTipoAgua.getText();
        if (tipoAgua.equals("") || !verificarCadena(tipoAgua)) {
            error += mensajeError(this.muestraTipoAgua);
        } else {
            estado += "Ok";
        }

        try {
            // valida el ID
            ID = Integer.parseInt(this.muestraId.getText());
            if (ID >= 0) {
                estado += "Ok";
            }

        } catch (NumberFormatException e) {
            error += mensajeError(this.muestraId);

        }

        try {
            // valida el irca
            irca = Float.parseFloat(this.muestraIrca.getText());
            if (irca >= 0) {
                estado += "Ok";
            }

        } catch (NumberFormatException e) {
            error += mensajeError(this.muestraIrca);
        }

        if (estado.equals("OkOkOkOkOkOk")) {
            try {
                String Sql = "UPDATE cuerpos SET Nombre='" + nombre + "', " + "ID=" + ID + ", " + "Municipio='"
                        + municipio + "', " + "Tipo_de_cuerpo_de_agua='" + tipoCuerpoAgua + "', " + "Tipo_de_agua='"
                        + tipoAgua + "', " + "IRCA=" + irca + " WHERE ID='" + this.textIdBuscar.getText() + "'";

                Statement pstModificar = Conexion.conectar().createStatement();
                pstModificar.executeUpdate(Sql);
                editarValoresArraylist(nombre, ID, municipio, irca, tipoCuerpoAgua, tipoAgua);
                borrarContenidoTextFlieds(2);
                ventanaEmergente("Editado correctamente", "", "comprobado.png");
            } catch (Exception e) {
                if (e.getMessage().substring(0, 30).equals("[SQLITE_CONSTRAINT_PRIMARYKEY]")) {
                    ventanaEmergente("", "La ID ya existe", "sql.png");
                } else {
                    ventanaEmergente("", e.getMessage(), "sql.png");
                }

            }
        } else {
            ventanaEmergente("Error en los siquientes campos", error, "error.png");
        }
    }

    @FXML
    void eventoEliminar(ActionEvent event) {

        if (textIdBuscar.getText().isEmpty()) {
            ventanaEmergente("Error !", "Se debe ingresar un ID para eliminar un registro", "oops.png");
        } else {
            try {
                String Sql = "DELETE FROM cuerpos WHERE ID='" + this.textIdBuscar.getText() + "'";
                Statement stEliminar = Conexion.conectar().createStatement();
                stEliminar.execute(Sql);
                this.eliminarDatoArrayList();
                borrarContenidoTextFlieds(2);
                ventanaEmergente("Eliminado correctamente", "", "comprobado.png");
            } catch (Exception e) {
                System.out.println("Debe Ingresar un Numero" + e.getMessage());
                System.out.println();
            }
        }
    }

    public boolean verificarCadena(String cadena) {
        boolean aceptada = false;
        Pattern p = Pattern.compile("[a-zA-Z]*");
        Matcher m = p.matcher(cadena);
        if (m.matches()) {
            aceptada = true;
        }
        return aceptada;
    }

    public String mensajeError(TextField componente) {
        return componente.getId().substring(4, componente.getId().length()).replace("tra", "") + "\n";

    }

    public boolean validarID(int id) {
        boolean repetido = false;
        for (CuerpoDeAgua cuerpos : registros) {
            if (cuerpos.getId() == id) {
                repetido = true;
                break;
            }
        }
        return repetido;
    }

    @FXML
    void eventoIngresar(ActionEvent event) {

        int ID = -1;
        double irca = 0;
        String estado = "";
        String error = "";
        // valida nombre
        String nombre = this.textNombre.getText();
        if (nombre.equals("") || !verificarCadena(nombre)) {
            error += mensajeError(textNombre);

        } else {
            estado += "Ok";
        }
        // valida municipio
        String municipio = this.textMunicipio.getText();
        if (municipio.equals("") || !verificarCadena(municipio)) {
            error += mensajeError(textMunicipio);
        } else {
            estado += "Ok";
        }
        // valida cuerpo de agua
        String tipoCuerpoAgua = this.textTipoCuerpo.getText();
        if (tipoCuerpoAgua.equals("") || !verificarCadena(tipoCuerpoAgua)) {
            error += mensajeError(textTipoCuerpo);
        } else {
            estado += "Ok";
        }
        // valida tipo de agua
        String tipoAgua = this.textTipoAgua.getText();
        if (tipoAgua.equals("") || !verificarCadena(tipoAgua)) {
            error += mensajeError(textTipoAgua);
        } else {
            estado += "Ok";
        }

        try {
            // valida el irca
            irca = Float.parseFloat(this.textIrca.getText());
            if (irca >= 0) {
                estado += "Ok";
            }

        } catch (NumberFormatException e) {
            error += mensajeError(textIrca);
        }

        try {
            // valida el ID
            ID = Integer.parseInt(this.textID.getText());
            if (ID >= 0) {
                estado += "Ok";
            }

        } catch (NumberFormatException e) {
            error += mensajeError(textID);
        }

        if (validarID(ID)) {
            ventanaEmergente("Error en ID", "el ID ingresado ya se encuentra registrado", "clave.png");
        } else {
            // verifica que todos los estados esten Ok
            if (estado.equals("OkOkOkOkOkOk")) {

                try {

                    String Sql = "INSERT INTO cuerpos (Nombre,ID,Municipio,Tipo_de_cuerpo_de_agua,Tipo_de_agua,IRCA) VALUES ("
                            + "'" + nombre + "', " + ID + ", " + "'" + municipio + "', " + "'" + tipoCuerpoAgua + "', "
                            + "'" + tipoAgua + "', " + irca + " )";

                    Statement stInsertar = Conexion.conectar().createStatement();
                    stInsertar.executeUpdate(Sql);
                    registros.add(new CuerpoDeAgua(nombre, ID, municipio, irca, tipoAgua, tipoCuerpoAgua));
                    estadoRegistro.setText(" - Registro Exitoso - ");
                    estadoRegistro.setTextFill(Color.GREEN);
                    borrarContenidoTextFlieds(1);
                    ventanaEmergente("Registro exitoso", "se a realizado con exito el registro en la base de datos",
                            "comprobado.png");

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("ERROR !!");
                }
            } else {
                estadoRegistro.setText(" Error en el Registro ");
                estadoRegistro.setTextFill(Color.RED);
                ventanaEmergente("Error en los siquientes campos", error, "error.png");
            }
        }
    }

    @FXML
    void eventoObtenerDatos(ActionEvent event) {
        if (registros.size() == 0) {
            areaObtenerDatos.setText("");
            areaProcesarDatos.setText("");
            ventanaEmergente("Oops...", "la base de datos no tiene registros", "oops.png");
        } else {
            try {

                Statement stmt = Conexion.conectar().createStatement();

                ResultSet rs = stmt.executeQuery("SELECT * FROM cuerpos;");
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnas = rsmd.getColumnCount();
                String producto = "";
                while (rs.next()) {
                    producto += "Registro -> " + rs.getRow() + "\n";
                    for (int i = 1; i <= columnas; i++) {
                        String contenido = rs.getString(i);
                        producto = producto + rsmd.getColumnName(i) + ": " + contenido + "\n";
                    }
                    producto += "\n";
                    this.areaObtenerDatos.setText(producto.replace("_", " "));
                }
                btnProcesarDatos.setDisable(false);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // salida de cuerpos por consola
        for (CuerpoDeAgua cuerpo : registros) {
            System.out.println(cuerpo.toString());
        }
    }

    // salidas
    @FXML
    void eventoProcesarDatos(ActionEvent event) {

        areaProcesarDatos.setText("");
        mostrarNombre_Municipio();
        areaProcesarDatos.appendText(contar() + "\n");
        riesgoAlto();
        maximoNivel();

    }

    public void cargarBaseDeDatos() {

        try {
            Statement stmt = Conexion.conectar().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM cuerpos;");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnas = rsmd.getColumnCount();
            String salidaVector = "";
            while (rs.next()) {
                salidaVector = "";
                for (int i = 1; i <= columnas; i++) {
                    String contenido = rs.getString(i);
                    salidaVector = salidaVector + contenido + " ";
                }
                String[] temporal = salidaVector.split(" ");
                registros.add(new CuerpoDeAgua(temporal[0], Integer.parseInt(temporal[1]), temporal[2],
                        Float.parseFloat(temporal[5]), temporal[4], temporal[3]));
            }

        } catch (Exception e) {
            System.out.println("Errooorr !! " + e.getMessage());
        }
    }

    public void riesgoAlto() {
        String salida = "NA";
        for (CuerpoDeAgua cuerpo : registros) {
            String riesgo = CuerpoDeAgua.nivel(cuerpo.getIrca());
            if (riesgo.equals("ALTO")) {
                salida += cuerpo.getMunicipio() + " ";
            }
        }

        if (!salida.equals("NA")) {
            areaProcesarDatos.appendText(salida.replace("NA", "") + "\n");

        } else {
            areaProcesarDatos.appendText("NA" + "\n");
        }
    }

    public void maximoNivel() {
        double maximo = 0;
        for (CuerpoDeAgua cuerpo : registros) {
            if (cuerpo.getIrca() > maximo) {
                maximo = cuerpo.getIrca();
            }
        }
        areaProcesarDatos.appendText(CuerpoDeAgua.nivel(maximo));
    }

    public void mostrarNombre_Municipio() {
        for (CuerpoDeAgua cuerpo : registros) {
            areaProcesarDatos.appendText(cuerpo.getNombre() + " " + cuerpo.getMunicipio() + "\n");

        }
    }

    public String contar() {
        double cont = 0;
        for (CuerpoDeAgua cuerpo : registros) {
            String riesgo = CuerpoDeAgua.nivel(cuerpo.getIrca());
            if (riesgo.equals("ALTO") || riesgo.equals("INVIABLE SANITARIAMENTE")) {
                cont++;
            }
        }
        return String.valueOf(new DecimalFormat("0.00").format(cont));
    }

    public int buscarIdArrayList(int valorId) {
        int pos = -1;

        for (int i = 0; i < registros.size(); i++) {
            CuerpoDeAgua cuerpo = registros.get(i);
            if (valorId == cuerpo.getId()) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    public void eliminarDatoArrayList() {
        try {
            int idBuscar = Integer.parseInt(textIdBuscar.getText());
            registros.remove(buscarIdArrayList(idBuscar));
        } catch (Exception e) {
            System.out.println("Debe ingresar un numero");
        }
    }

    public void editarValoresArraylist(String nombre, int Id, String muni, double irca, String tipoDeCuerpo,
            String tipoDeAgua) {

        try {
            int idBuscar = Integer.parseInt(textIdBuscar.getText());
            CuerpoDeAgua cuerpo = registros.get(buscarIdArrayList(idBuscar));
            // Modifica
            cuerpo.setNombre(nombre);
            cuerpo.setId(Id);
            cuerpo.setMunicipio(muni);
            cuerpo.setIrca(irca);
            cuerpo.setTipoDeCuerpo(tipoDeCuerpo);
            cuerpo.setTipoDeAgua(tipoDeAgua);

        } catch (Exception e) {
            System.out.println("debe ingresar un numero");
        }

    }

    public void bloquearTextFieldBusqueda(boolean estado) {
        muestraNombre.setEditable(estado);
        muestraMunicipio.setEditable(estado);
        muestraCuerpoAgua.setEditable(estado);
        muestraTipoAgua.setEditable(estado);
        muestraIrca.setEditable(estado);
        muestraId.setEditable(estado);
    }

    public void borrarContenidoTextFlieds(int op) {

        switch (op) {
            case 1:
                this.textID.setText("");
                this.textTipoAgua.setText("");
                this.textIrca.setText("");
                this.textMunicipio.setText("");
                this.textNombre.setText("");
                this.textTipoCuerpo.setText("");
                break;

            case 2:
                this.muestraCuerpoAgua.setText("");
                this.muestraId.setText("");
                this.muestraIrca.setText("");
                this.muestraMunicipio.setText("");
                this.muestraNombre.setText("");
                this.muestraTipoAgua.setText("");
                this.textIdBuscar.setText("");
                textIdBuscar.setText("");
                break;
        }

    }

    public void bloquearBotones(boolean estado) {
        btnEditar.setDisable(estado);
        btnEliminar.setDisable(estado);
    }

    @FXML
    void initialize() {

        cargarBaseDeDatos();
        bloquearTextFieldBusqueda(false);
        bloquearBotones(true);
        btnProcesarDatos.setDisable(true);

        // agregando estilo a los demas botones
        btnEditar.setStyle(btnIngresar.getStyle());
        btnObtenerDatos.setStyle(btnIngresar.getStyle());
        btnProcesarDatos.setStyle(btnIngresar.getStyle());
        btnComprobarConexion.setStyle(btnIngresar.getStyle());
        btnBuscar.setStyle(btnIngresar.getStyle());
        btnEliminar.setStyle(btnIngresar.getStyle());

        textNombre.requestFocus();

    }
}
