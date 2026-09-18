package Controlador;

import Conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import Modelo.MensajeContacto;

public class ContactoDAO {
    public boolean guardar(MensajeContacto solicitud) {
        String sql = "INSERT INTO MensajesContacto(nombre, apellidos, documento, tipo_documento, email, telefono, direccion, tipo_solicitud, usuario_deseado, asunto, mensaje) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return false;
            ps.setString(1, solicitud.getNombre()); ps.setString(2, solicitud.getApellidos()); ps.setString(3, solicitud.getDocumento());
            ps.setString(4, solicitud.getTipoDocumento()); ps.setString(5, solicitud.getEmail()); ps.setString(6, solicitud.getTelefono()); ps.setString(7, solicitud.getDireccion()); ps.setString(8, solicitud.getTipoSolicitud());
            ps.setString(9, solicitud.getUsuarioDeseado()); ps.setString(10, solicitud.getAsunto()); ps.setString(11, solicitud.getMensaje());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("No se pudo guardar el mensaje de contacto: " + e.getMessage());
            return false;
        }
    }

    public List<MensajeContacto> listar() {
        List<MensajeContacto> mensajes = new ArrayList<>();
        String sql = "SELECT m.id_mensaje, m.nombre, m.apellidos, m.documento, m.tipo_documento, m.email, m.telefono, m.direccion, m.tipo_solicitud, m.usuario_deseado, m.asunto, m.mensaje, m.fecha, m.fecha_atencion, m.atendido, m.Personal_id_personal, COALESCE(p.debe_cambiar_clave, false) AS personal_debe_cambiar_clave FROM MensajesContacto m LEFT JOIN Personal p ON p.id_personal = m.Personal_id_personal WHERE m.atendido = false OR m.fecha_atencion IS NULL OR m.fecha_atencion >= DATE_SUB(NOW(), INTERVAL 24 HOUR) ORDER BY m.atendido ASC, m.fecha DESC";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return mensajes;
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MensajeContacto item = new MensajeContacto();
                    item.setId(rs.getLong("id_mensaje"));
                    item.setNombre(rs.getString("nombre"));
                    item.setApellidos(rs.getString("apellidos")); item.setDocumento(rs.getString("documento")); item.setTipoDocumento(rs.getString("tipo_documento"));
                    item.setEmail(rs.getString("email"));
                    item.setTelefono(rs.getString("telefono")); item.setDireccion(rs.getString("direccion")); item.setTipoSolicitud(rs.getString("tipo_solicitud"));
                    item.setUsuarioDeseado(rs.getString("usuario_deseado"));
                    item.setAsunto(rs.getString("asunto"));
                    item.setMensaje(rs.getString("mensaje"));
                    item.setFecha(rs.getString("fecha"));
                    item.setFechaAtencion(rs.getString("fecha_atencion"));
                    item.setAtendido(rs.getBoolean("atendido"));
                    int personalId = rs.getInt("Personal_id_personal"); item.setPersonalId(rs.wasNull() ? null : personalId);
                    item.setPersonalDebeCambiarClave(rs.getBoolean("personal_debe_cambiar_clave"));
                    mensajes.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("No se pudieron consultar los mensajes: " + e.getMessage());
        }
        return mensajes;
    }

    /** Solicitudes atendidas hace más de 24 horas, conservadas como historial. */
    public List<MensajeContacto> listarArchivadas() {
        List<MensajeContacto> mensajes = new ArrayList<>();
        String sql = "SELECT m.id_mensaje, m.nombre, m.apellidos, m.documento, m.tipo_documento, m.email, m.telefono, m.direccion, m.tipo_solicitud, m.usuario_deseado, m.asunto, m.mensaje, m.fecha, m.fecha_atencion, m.atendido, m.Personal_id_personal, COALESCE(p.debe_cambiar_clave, false) AS personal_debe_cambiar_clave FROM MensajesContacto m LEFT JOIN Personal p ON p.id_personal = m.Personal_id_personal WHERE m.atendido = true AND m.fecha_atencion < DATE_SUB(NOW(), INTERVAL 24 HOUR) ORDER BY m.fecha_atencion DESC";
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return mensajes;
            try (ResultSet rs = ps.executeQuery()) { while (rs.next()) mensajes.add(mapearMensaje(rs)); }
        } catch (SQLException e) { System.out.println("No se pudo consultar el historial de solicitudes: " + e.getMessage()); }
        return mensajes;
    }

    /** Obtiene una solicitud concreta para precargar el registro de personal. */
    public MensajeContacto consultarPorId(long id) {
        String sql = "SELECT id_mensaje, nombre, apellidos, documento, tipo_documento, email, telefono, direccion, tipo_solicitud, usuario_deseado, asunto, mensaje, fecha, atendido, Personal_id_personal "
                + "FROM MensajesContacto WHERE id_mensaje = ?";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return null;
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                MensajeContacto item = new MensajeContacto();
                item.setId(rs.getLong("id_mensaje"));
                item.setNombre(rs.getString("nombre"));
                item.setApellidos(rs.getString("apellidos"));
                item.setDocumento(rs.getString("documento"));
                item.setTipoDocumento(rs.getString("tipo_documento"));
                item.setEmail(rs.getString("email"));
                item.setTelefono(rs.getString("telefono"));
                item.setDireccion(rs.getString("direccion"));
                item.setTipoSolicitud(rs.getString("tipo_solicitud"));
                item.setUsuarioDeseado(rs.getString("usuario_deseado"));
                item.setAsunto(rs.getString("asunto"));
                item.setMensaje(rs.getString("mensaje"));
                item.setFecha(rs.getString("fecha"));
                item.setAtendido(rs.getBoolean("atendido"));
                int personalId = rs.getInt("Personal_id_personal"); item.setPersonalId(rs.wasNull() ? null : personalId);
                return item;
            }
        } catch (SQLException e) {
            System.out.println("No se pudo consultar la solicitud de contacto: " + e.getMessage());
            return null;
        }
    }

    public boolean marcarAtendido(long id) {
        String sql = "UPDATE MensajesContacto SET atendido = true, fecha_atencion = NOW() WHERE id_mensaje = ? AND atendido = false";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return false;
            ps.setLong(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("No se pudo actualizar el mensaje: " + e.getMessage());
            return false;
        }
    }

    public boolean estaVigenteParaRegistro(long id) {
        String sql = "SELECT 1 FROM MensajesContacto WHERE id_mensaje = ? "
                + "AND atendido = false AND fecha >= DATE_SUB(NOW(), INTERVAL 24 HOUR)";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return false;
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("No se pudo validar la vigencia de la solicitud: " + e.getMessage());
            return false;
        }
    }

    private MensajeContacto mapearMensaje(ResultSet rs) throws SQLException {
        MensajeContacto item = new MensajeContacto();
        item.setId(rs.getLong("id_mensaje")); item.setNombre(rs.getString("nombre")); item.setApellidos(rs.getString("apellidos"));
        item.setDocumento(rs.getString("documento")); item.setTipoDocumento(rs.getString("tipo_documento")); item.setEmail(rs.getString("email"));
        item.setTelefono(rs.getString("telefono")); item.setDireccion(rs.getString("direccion")); item.setTipoSolicitud(rs.getString("tipo_solicitud"));
        item.setUsuarioDeseado(rs.getString("usuario_deseado")); item.setAsunto(rs.getString("asunto")); item.setMensaje(rs.getString("mensaje"));
        item.setFecha(rs.getString("fecha")); item.setFechaAtencion(rs.getString("fecha_atencion")); item.setAtendido(rs.getBoolean("atendido"));
        int personalId = rs.getInt("Personal_id_personal"); item.setPersonalId(rs.wasNull() ? null : personalId);
        item.setPersonalDebeCambiarClave(rs.getBoolean("personal_debe_cambiar_clave"));
        return item;
    }

    public boolean asociarPersonal(long solicitudId, int personalId) {
        String sql = "UPDATE MensajesContacto SET Personal_id_personal = ? WHERE id_mensaje = ?";
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return false;
            ps.setInt(1, personalId); ps.setLong(2, solicitudId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) { return false; }
    }

    /** Alias solicitado al crear la cuenta; se usa como nombre visible del panel. */
    public String consultarUsuarioDeseadoPorPersonal(int personalId) {
        String sql = "SELECT usuario_deseado FROM MensajesContacto WHERE Personal_id_personal = ? "
                + "AND usuario_deseado <> '' ORDER BY id_mensaje DESC LIMIT 1";
        try (Connection con = new Conexion().getConexion(); PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return "";
            ps.setInt(1, personalId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getString("usuario_deseado") : ""; }
        } catch (SQLException e) { return ""; }
    }

    public int contarPendientes() {
        String sql = "SELECT COUNT(*) FROM MensajesContacto WHERE atendido = false";
        try (Connection con = new Conexion().getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return 0;
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            System.out.println("No se pudieron contar los mensajes pendientes: " + e.getMessage());
            return 0;
        }
    }
}
