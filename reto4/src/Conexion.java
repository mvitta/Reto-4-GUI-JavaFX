import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    public static Connection conectar() {

        String url = "jdbc:sqlite:C:\\BasesDeDatos\\Empresa\\CuerpodeAgua.db";
        Connection conx = null;

        try {

            conx = DriverManager.getConnection(url);
            System.out.println("Conexion Exitosa");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error de conexion con la base de datos");
        }

        return conx;
    }



}
