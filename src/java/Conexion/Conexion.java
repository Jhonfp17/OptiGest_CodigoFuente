package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Seguridad.AuditoriaContexto;

public class Conexion {

    private String driver = "com.mysql.cj.jdbc.Driver";

    // Si existen las variables de entorno de Railway, se usan.
    // Si no (por ejemplo, corriendo local en tu PC), cae en los valores de localhost.
    private String host     = getEnvOrDefault("MYSQLHOST", "localhost");
    private String port     = getEnvOrDefault("MYSQLPORT", "3306");
    // Railway suele publicar esta variable como MYSQL_DATABASE; se mantiene
    // compatibilidad con MYSQLDATABASE para los entornos ya configurados.
    private String database = getEnvOrDefault("MYSQLDATABASE",
            getEnvOrDefault("MYSQL_DATABASE", "OptiGest"));
    private String user     = getEnvOrDefault("MYSQLUSER", "root");
    private String password = getEnvOrDefault("MYSQLPASSWORD", "");

    private String url = "jdbc:mysql://" + host + ":" + port + "/" + database
            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            + "&connectTimeout=5000&socketTimeout=10000";

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    public Connection getConexion() {
        Connection con = null;

        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, user, password);
            AuditoriaContexto.prepararConexion(con);
            System.out.println("Conexion establecida correctamente.");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error de conexion a MySQL en " + host + ":" + port
                    + "/" + database + " con el usuario " + user + ": " + e.getMessage());
        }

        return con;
    }
}
