package Seguridad;

import Modelo.Personal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/** Datos del usuario que realiza una operacion auditable durante la solicitud actual. */
public final class AuditoriaContexto {
    private static final ThreadLocal<Datos> CONTEXTO = new ThreadLocal<>();

    private AuditoriaContexto() { }

    public static void establecer(Personal usuario, String motivo) {
        Integer id = usuario == null ? null : usuario.getIdPersonal();
        String nombre = usuario == null ? "Sistema" : (usuario.getNombre() + " " + usuario.getApellidos()).trim();
        CONTEXTO.set(new Datos(id, nombre.isEmpty() ? "Sistema" : nombre, limpiar(motivo)));
    }

    public static void limpiar() { CONTEXTO.remove(); }

    public static void prepararConexion(Connection conexion) throws SQLException {
        Datos datos = CONTEXTO.get();
        try (PreparedStatement ps = conexion.prepareStatement(
                "SET @aud_usuario_id = ?, @aud_usuario_nombre = ?, @aud_motivo = ?")) {
            if (datos == null || datos.idUsuario == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setString(2, "Sistema");
                ps.setString(3, null);
            } else {
                ps.setInt(1, datos.idUsuario);
                ps.setString(2, datos.nombreUsuario);
                ps.setString(3, datos.motivo);
            }
            ps.execute();
        }
    }

    private static String limpiar(String valor) {
        if (valor == null) return null;
        String limpio = valor.trim();
        return limpio.isEmpty() ? null : limpio;
    }

    private static final class Datos {
        private final Integer idUsuario;
        private final String nombreUsuario;
        private final String motivo;
        private Datos(Integer idUsuario, String nombreUsuario, String motivo) {
            this.idUsuario = idUsuario;
            this.nombreUsuario = nombreUsuario;
            this.motivo = motivo;
        }
    }
}
