package Controlador;

import Conexion.Conexion;
import Modelo.HistorialCambio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Consulta de solo lectura para la bitacora que mantienen los triggers de MySQL. */
public class HistorialCambiosDAO {
    /** Historial exacto de un registro, usado desde su pantalla de edición. */
    public List<HistorialCambio> listarPorRegistro(String entidad, int idRegistro) {
        List<HistorialCambio> resultado = new ArrayList<>();
        if (!entidadPermitida(entidad)) return resultado;
        String sql = "SELECT h.id_historial, h.fecha, h.entidad, h.id_registro, h.accion, h.datos_antes, h.datos_despues, h.usuario_nombre, h.motivo, h.usuario_bd, "
                + "CASE WHEN h.entidad = 'Personal' THEN CONCAT(p.nombre, ' ', p.apellidos) "
                + "WHEN h.entidad = 'Activos' THEN a.nombre_activos "
                + "WHEN h.entidad = 'Asignaciones' THEN CONCAT(pa.nombre, ' — ', aa.codigo_act) "
                + "WHEN h.entidad = 'Mantenimiento' THEN CONCAT(am.codigo_act, ' — ', m.descripcion) "
                + "WHEN h.entidad = 'Proveedores' THEN pr.nombre "
                + "ELSE h.entidad END AS registro_descripcion "
                + "FROM HistorialCambios h "
                + "LEFT JOIN Personal p ON h.entidad = 'Personal' AND p.id_personal = h.id_registro "
                + "LEFT JOIN Activos a ON h.entidad = 'Activos' AND a.id_activos = h.id_registro "
                + "LEFT JOIN Asignaciones asi ON h.entidad = 'Asignaciones' AND asi.id_asignaciones = h.id_registro "
                + "LEFT JOIN Personal pa ON asi.Personal_id_personal = pa.id_personal "
                + "LEFT JOIN Activos aa ON asi.Activos_id_activos = aa.id_activos "
                + "LEFT JOIN Mantenimiento m ON h.entidad = 'Mantenimiento' AND m.id_mantenimiento = h.id_registro "
                + "LEFT JOIN Activos am ON m.Activos_id_activos = am.id_activos "
                + "LEFT JOIN Proveedores pr ON h.entidad = 'Proveedores' AND pr.idProveedores = h.id_registro "
                + "WHERE h.entidad = ? AND h.id_registro = ? ORDER BY h.fecha DESC, h.id_historial DESC";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return resultado;
            ps.setString(1, entidad);
            ps.setInt(2, idRegistro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) resultado.add(desdeResultado(rs));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo consultar el historial del registro: " + e.getMessage());
        }
        return resultado;
    }

    public List<HistorialCambio> listar(String entidad, String busqueda) {
        return listar(entidad, null, busqueda);
    }

    public List<HistorialCambio> listar(String entidad, String accion, String busqueda) {
        List<HistorialCambio> resultado = new ArrayList<>();
        if (entidad != null && !entidad.trim().isEmpty() && !entidadPermitida(entidad.trim())) {
            return resultado;
        }
        StringBuilder sql = new StringBuilder("SELECT h.id_historial, h.fecha, h.entidad, h.id_registro, h.accion, h.datos_antes, h.datos_despues, h.usuario_nombre, h.motivo, h.usuario_bd, "
                + "CASE WHEN h.entidad = 'Personal' THEN CONCAT(p.nombre, ' ', p.apellidos) "
                + "WHEN h.entidad = 'Activos' THEN a.nombre_activos "
                + "WHEN h.entidad = 'Asignaciones' THEN CONCAT(pa.nombre, ' ', pa.apellidos, ' — ', aa.codigo_act) "
                + "WHEN h.entidad = 'Mantenimiento' THEN CONCAT(am.codigo_act, ' — ', m.descripcion) "
                + "WHEN h.entidad = 'Proveedores' THEN pr.nombre "
                + "ELSE h.entidad END AS registro_descripcion "
                + "FROM HistorialCambios h "
                + "LEFT JOIN Personal p ON h.entidad = 'Personal' AND p.id_personal = h.id_registro "
                + "LEFT JOIN Activos a ON h.entidad = 'Activos' AND a.id_activos = h.id_registro "
                + "LEFT JOIN Asignaciones asi ON h.entidad = 'Asignaciones' AND asi.id_asignaciones = h.id_registro "
                + "LEFT JOIN Personal pa ON asi.Personal_id_personal = pa.id_personal "
                + "LEFT JOIN Activos aa ON asi.Activos_id_activos = aa.id_activos "
                + "LEFT JOIN Mantenimiento m ON h.entidad = 'Mantenimiento' AND m.id_mantenimiento = h.id_registro "
                + "LEFT JOIN Activos am ON m.Activos_id_activos = am.id_activos "
                + "LEFT JOIN Proveedores pr ON h.entidad = 'Proveedores' AND pr.idProveedores = h.id_registro WHERE 1 = 1");
        List<Object> parametros = new ArrayList<>();
        if (entidad != null && !entidad.trim().isEmpty()) {
            sql.append(" AND entidad = ?");
            parametros.add(entidad.trim());
        } else {
            sql.append(" AND h.entidad IN ('Activos', 'Personal', 'Asignaciones', 'Mantenimiento', 'Proveedores')");
        }
        if ("CREADO".equalsIgnoreCase(accion) || "ACTUALIZADO".equalsIgnoreCase(accion)) {
            sql.append(" AND h.accion = ?");
            parametros.add(accion.toUpperCase());
        } else if ("DESACTIVADO".equalsIgnoreCase(accion)) {
            // Los activos se retiran y el personal se desactiva: ambos son bajas lógicas.
            sql.append(" AND h.accion IN ('DESACTIVADO', 'RETIRADO')");
        }
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append(" AND (p.nombre LIKE ? OR p.apellidos LIKE ? OR p.identificacion LIKE ? "
                    + "OR a.nombre_activos LIKE ? OR a.codigo_act LIKE ? "
                    + "OR pa.nombre LIKE ? OR pa.apellidos LIKE ? OR pa.identificacion LIKE ? "
                    + "OR aa.nombre_activos LIKE ? OR aa.codigo_act LIKE ? "
                    + "OR m.descripcion LIKE ? OR am.nombre_activos LIKE ? OR am.codigo_act LIKE ? "
                    + "OR pr.nombre LIKE ?)");
            String texto = "%" + busqueda.trim() + "%";
            for (int i = 0; i < 14; i++) parametros.add(texto);
        }
        sql.append(" ORDER BY fecha DESC, id_historial DESC LIMIT 300");

        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql.toString())) {
            if (ps == null) return resultado;
            for (int i = 0; i < parametros.size(); i++) ps.setObject(i + 1, parametros.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(desdeResultado(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudo consultar el historial: " + e.getMessage());
        }
        return resultado;
    }

    private HistorialCambio desdeResultado(ResultSet rs) throws SQLException {
        HistorialCambio item = new HistorialCambio();
        item.setId(rs.getInt("id_historial"));
        item.setFecha(rs.getString("fecha"));
        item.setEntidad(rs.getString("entidad"));
        item.setIdRegistro(rs.getInt("id_registro"));
        item.setAccion(rs.getString("accion"));
        item.setDatosAntes(rs.getString("datos_antes"));
        item.setDatosDespues(rs.getString("datos_despues"));
        item.setUsuarioNombre(rs.getString("usuario_nombre"));
        item.setMotivo(rs.getString("motivo"));
        item.setUsuarioBaseDatos(rs.getString("usuario_bd"));
        item.setRegistroDescripcion(rs.getString("registro_descripcion"));
        return item;
    }

    private boolean entidadPermitida(String entidad) {
        return "Activos".equals(entidad) || "Personal".equals(entidad)
                || "Asignaciones".equals(entidad) || "Mantenimiento".equals(entidad)
                || "Proveedores".equals(entidad);
    }
}
