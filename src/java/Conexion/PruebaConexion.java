
package Conexion;

import java.sql.Connection;

public class PruebaConexion {

    
    public static void main(String[] args) {

        Conexion con = new Conexion();
        Connection reg = con.getConexion();
    }

}
