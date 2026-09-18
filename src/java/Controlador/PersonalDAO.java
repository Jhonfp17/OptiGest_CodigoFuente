package Controlador;

import Conexion.Conexion;
import Modelo.Personal;
import Modelo.Roles;
import Seguridad.PasswordHasher;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PersonalDAO {

    // Cuenta protegida: Luis Diaz, identificacion 1000222333.
    // El ID evita que una edicion futura de sus datos personales quite su proteccion.
    private static final int ID_ADMINISTRADOR_MAESTRO = 3;
    private static final String IDENTIFICACION_ADMINISTRADOR_MAESTRO = "1000222333";
    private static final int ROL_ADMINISTRADOR = 1;

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuPersonal(int opcion) {
        Personal per = new Personal();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR NUEVO PERSONAL ---");
                System.out.print("Nombre: ");
                per.setNombre(sc.nextLine());
                System.out.print("Apellidos: ");
                per.setApellidos(sc.nextLine());
                System.out.print("Identificacion: ");
                per.setIdentificacion(sc.nextLine());
                System.out.print("Email: ");
                per.setEmail(sc.nextLine());
                System.out.print("Telefono: ");
                per.setTelefono(sc.nextLine());
                System.out.print("Direccion: ");
                per.setDireccion(sc.nextLine());
                System.out.print("Clave: ");
                per.setClave(sc.nextLine());
                System.out.print("Observaciones: ");
                per.setObservaciones(sc.nextLine());
                System.out.print("Puede acceder? 1=Si, 0=No: ");
                per.setPuede_acceder(sc.nextInt() == 1);
                sc.nextLine();
                System.out.print("Fecha contratacion (YYYY-MM-DD): ");
                per.setFecha_contratacion(sc.nextLine());
                System.out.print("ID Documento: ");
                per.setDocumento_id_documento(sc.nextInt());
                System.out.print("ID Rol: ");
                per.setRoles_idroles(sc.nextInt());
                System.out.print("ID Estado: ");
                per.setEstado_Personal_id_estado(sc.nextInt());
                sc.nextLine();

                if (insertar(per)) {
                    System.out.println("Personal registrado con exito.");
                } else {
                    System.out.println("Error al registrar.");
                }
                break;

            case 2:
                System.out.print("\nIngrese la identificacion a buscar: ");
                String idBusq = sc.nextLine();
                Personal encontrado = consultarPorIdentificacion(idBusq);

                if (encontrado != null) {
                    mostrarPersonal(encontrado);
                } else {
                    System.out.println("No se encontro personal con esa identificacion.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO GENERAL DE PERSONAL ---");
                List<Personal> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay personal registrado");
                } else {
                    for (Personal p : lista) {
                        mostrarPersonal(p);
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR DATOS DE PERSONAL ---");
                System.out.print("Ingrese identificacion del personal a modificar: ");
                per.setIdentificacion(sc.nextLine());
                System.out.print("Nuevo nombre: ");
                per.setNombre(sc.nextLine());
                System.out.print("Nuevos apellidos: ");
                per.setApellidos(sc.nextLine());
                System.out.print("Nuevo email: ");
                per.setEmail(sc.nextLine());
                System.out.print("Nuevo telefono: ");
                per.setTelefono(sc.nextLine());
                System.out.print("Nueva direccion: ");
                per.setDireccion(sc.nextLine());
                System.out.print("Nueva clave: ");
                per.setClave(sc.nextLine());
                System.out.print("Nuevas observaciones: ");
                per.setObservaciones(sc.nextLine());
                System.out.print("Puede acceder? 1=Si, 0=No: ");
                per.setPuede_acceder(sc.nextInt() == 1);
                sc.nextLine();
                System.out.print("Nueva fecha contratacion (YYYY-MM-DD): ");
                per.setFecha_contratacion(sc.nextLine());
                System.out.print("Nuevo ID Documento: ");
                per.setDocumento_id_documento(sc.nextInt());
                System.out.print("Nuevo ID Rol: ");
                per.setRoles_idroles(sc.nextInt());
                System.out.print("Nuevo ID Estado: ");
                per.setEstado_Personal_id_estado(sc.nextInt());
                sc.nextLine();

                if (actualizar(per)) {
                    System.out.println("Datos actualizados con exito.");
                } else {
                    System.out.println("Error al actualizar.");
                }
                break;

            case 5:
                System.out.print("\nIngrese la identificacion del personal a eliminar: ");
                String idEliminar = sc.nextLine();

                if (eliminar(idEliminar)) {
                    System.out.println("Registro eliminado.");
                } else {
                    System.out.println("No se pudo eliminar.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Personal per) {
        String sql = "INSERT INTO Personal "
                + "(nombre, apellidos, identificacion, email, telefono, direccion, clave, observaciones, "
                + "puede_acceder, debe_cambiar_clave, clave_temporal_expira_en, fecha_contratacion, Documento_id_documento, roles_idroles, Estado_Personal_id_estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CASE WHEN EXISTS "
                + "(SELECT 1 FROM Estado_Personal ep WHERE ep.id_estado = ? "
                + "AND UPPER(TRIM(ep.descripcion_estado)) = 'RETIRADO') THEN false ELSE ? END, ?, ?, ?, ?, ?, ?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellidos());
            ps.setString(3, per.getIdentificacion());
            ps.setString(4, per.getEmail());
            ps.setString(5, per.getTelefono());
            ps.setString(6, per.getDireccion());
            ps.setString(7, protegerClave(per.getClave()));
            ps.setString(8, per.getObservaciones());
            ps.setInt(9, per.getEstado_Personal_id_estado());
            ps.setBoolean(10, per.isPuede_acceder());
            ps.setBoolean(11, per.isDebeCambiarClave());
            ps.setTimestamp(12, per.getClaveTemporalExpiraEn() == null ? null : java.sql.Timestamp.valueOf(per.getClaveTemporalExpiraEn()));
            setDateOrNull(ps, 13, per.getFecha_contratacion());
            ps.setInt(14, per.getDocumento_id_documento());
            ps.setInt(15, per.getRoles_idroles());
            ps.setInt(16, per.getEstado_Personal_id_estado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Personal consultar(int idPersonal) {
        Personal per = null;
        String sql = "SELECT p.*, r.tipo_acceso FROM Personal p "
                + "LEFT JOIN Roles r ON r.idroles = p.roles_idroles "
                + "WHERE p.id_personal = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPersonal);
            rs = ps.executeQuery();

            if (rs.next()) {
                per = mapearPersonal(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar personal por ID: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return per;
    }

    public Personal consultarPorId(int idPersonal) {
        return consultar(idPersonal);
    }

    public List<Roles> listarRolesPorPersonal(int idPersonal) {
        List<Roles> roles = new ArrayList<>();
        String sql = "SELECT r.idroles, r.descripcion_roles, r.tipo_acceso FROM Personal_Roles pr "
                + "INNER JOIN Roles r ON r.idroles = pr.id_rol WHERE pr.id_personal = ? ORDER BY r.descripcion_roles";
        try (Connection conexion = cn.getConexion(); PreparedStatement consulta = conexion == null ? null : conexion.prepareStatement(sql)) {
            if (consulta == null) return roles;
            consulta.setInt(1, idPersonal);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    Roles rol = new Roles();
                    rol.setIdRoles(resultado.getInt("idroles"));
                    rol.setDescripcion_roles(resultado.getString("descripcion_roles"));
                    rol.setTipo_acceso(resultado.getString("tipo_acceso"));
                    roles.add(rol);
                }
            }
        } catch (SQLException e) { System.out.println("Error al listar roles del personal: " + e.getMessage()); }
        return roles;
    }

    public Personal consultarPorIdentificacion(String identificacion) {
        Personal per = null;
        String sql = "SELECT p.*, r.tipo_acceso FROM Personal p "
                + "LEFT JOIN Roles r ON r.idroles = p.roles_idroles "
                + "WHERE p.identificacion = ? AND p.activo = true "
                + "ORDER BY p.id_personal DESC LIMIT 1";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, identificacion);
            rs = ps.executeQuery();

            if (rs.next()) {
                per = mapearPersonal(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar personal: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return per;
    }

    public Personal ConsultaPersonal(String identificacion) {
        return consultarPorIdentificacion(identificacion);
    }

    public Personal consultarPorEmail(String email) {
        Personal per = null;
        String sql = "SELECT p.*, r.tipo_acceso FROM Personal p "
                + "LEFT JOIN Roles r ON r.idroles = p.roles_idroles "
                + "WHERE p.email = ? AND p.activo = true "
                + "ORDER BY p.id_personal DESC LIMIT 1";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                per = mapearPersonal(rs);
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar personal por email: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return per;
    }

    public List<Personal> listar() {
        return listarPorEstado(true);
    }

    /** Lista el personal retirado para las consultas administrativas. */
    public List<Personal> listarInactivos() {
        return listarPorEstado(false);
    }

    private List<Personal> listarPorEstado(boolean activos) {
        List<Personal> lista = new ArrayList<>();
        String sql = "SELECT p.*, r.tipo_acceso FROM Personal p "
                + "LEFT JOIN Roles r ON r.idroles = p.roles_idroles "
                + "WHERE p.activo = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setBoolean(1, activos);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearPersonal(rs));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar personal: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public List<Personal> consultarTodo() {
        return listar();
    }

    public boolean actualizar(Personal per) {
        boolean esAdministradorMaestro = esAdministradorMaestro(per);
        String sql = "UPDATE Personal SET "
                + "nombre = ?, apellidos = ?, email = ?, telefono = ?, direccion = ?, clave = ?, "
                + "observaciones = ?, puede_acceder = CASE WHEN EXISTS "
                + "(SELECT 1 FROM Estado_Personal ep WHERE ep.id_estado = ? "
                + "AND UPPER(TRIM(ep.descripcion_estado)) = 'RETIRADO') THEN false ELSE ? END, fecha_contratacion = ?, "
                + "Documento_id_documento = ?, roles_idroles = ?, Estado_Personal_id_estado = ? "
                + "WHERE identificacion = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellidos());
            ps.setString(3, per.getEmail());
            ps.setString(4, per.getTelefono());
            ps.setString(5, per.getDireccion());
            ps.setString(6, protegerClave(per.getClave()));
            ps.setString(7, per.getObservaciones());
            ps.setInt(8, per.getEstado_Personal_id_estado());
            ps.setBoolean(9, esAdministradorMaestro || per.isPuede_acceder());
            setDateOrNull(ps, 10, per.getFecha_contratacion());
            ps.setInt(11, per.getDocumento_id_documento());
            ps.setInt(12, esAdministradorMaestro ? ROL_ADMINISTRADOR : per.getRoles_idroles());
            ps.setInt(13, per.getEstado_Personal_id_estado());
            ps.setString(14, per.getIdentificacion());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean actualizarPorId(Personal per) {
        boolean esAdministradorMaestro = esAdministradorMaestro(per);
        String sql = "UPDATE Personal SET "
                + "nombre = ?, apellidos = ?, identificacion = ?, email = ?, telefono = ?, direccion = ?, clave = ?, "
                + "observaciones = ?, puede_acceder = CASE WHEN EXISTS "
                + "(SELECT 1 FROM Estado_Personal ep WHERE ep.id_estado = ? "
                + "AND UPPER(TRIM(ep.descripcion_estado)) = 'RETIRADO') THEN false ELSE ? END, fecha_contratacion = ?, "
                + "Documento_id_documento = ?, roles_idroles = ?, Estado_Personal_id_estado = ? "
                + "WHERE id_personal = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, per.getNombre());
            ps.setString(2, per.getApellidos());
            ps.setString(3, per.getIdentificacion());
            ps.setString(4, per.getEmail());
            ps.setString(5, per.getTelefono());
            ps.setString(6, per.getDireccion());
            ps.setString(7, protegerClave(per.getClave()));
            ps.setString(8, per.getObservaciones());
            ps.setInt(9, per.getEstado_Personal_id_estado());
            ps.setBoolean(10, esAdministradorMaestro || per.isPuede_acceder());
            setDateOrNull(ps, 11, per.getFecha_contratacion());
            ps.setInt(12, per.getDocumento_id_documento());
            ps.setInt(13, esAdministradorMaestro ? ROL_ADMINISTRADOR : per.getRoles_idroles());
            ps.setInt(14, per.getEstado_Personal_id_estado());
            ps.setInt(15, per.getIdPersonal());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar personal por ID: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean actualizarClavePorId(int idPersonal, String nuevaClave) {
        String sql = "UPDATE Personal SET clave = ?, debe_cambiar_clave = false, clave_temporal_expira_en = NULL WHERE id_personal = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, protegerClave(nuevaClave));
            ps.setInt(2, idPersonal);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar clave: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    /** Reemplaza una contraseña temporal y le asigna una nueva vigencia. */
    public boolean actualizarClaveTemporal(int idPersonal, String nuevaClave, java.time.LocalDateTime expiraEn) {
        String sql = "UPDATE Personal SET clave = ?, debe_cambiar_clave = true, clave_temporal_expira_en = ? WHERE id_personal = ? AND activo = true";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, protegerClave(nuevaClave));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(expiraEn));
            ps.setInt(3, idPersonal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al renovar clave temporal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean estaActivo(int idPersonal) {
        String sql = "SELECT activo FROM Personal WHERE id_personal = ?";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPersonal);
            rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("activo");
        } catch (SQLException e) {
            System.out.println("Error al validar estado del personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(String identificacion) {
        if (IDENTIFICACION_ADMINISTRADOR_MAESTRO.equals(identificacion)) {
            System.out.println("No se puede desactivar al administrador maestro.");
            return false;
        }
        String sql = "UPDATE Personal SET puede_acceder = false, activo = false, fecha_baja = NOW() "
                + "WHERE identificacion = ? AND activo = true AND NOT EXISTS "
                + "(SELECT 1 FROM Asignaciones asi INNER JOIN Personal p ON p.id_personal = asi.Personal_id_personal "
                + "WHERE p.identificacion = ? AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE()) AND asi.anulado = false)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, identificacion);
            ps.setString(2, identificacion);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al desactivar personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminarPorId(int idPersonal) {
        return desactivarPorId(idPersonal);
    }

    /** Conserva el registro y únicamente revoca el acceso de la persona. */
    public boolean desactivarPorId(int idPersonal) {
        if (idPersonal == ID_ADMINISTRADOR_MAESTRO) {
            System.out.println("No se puede desactivar al administrador maestro.");
            return false;
        }
        String sql = "UPDATE Personal SET puede_acceder = false, activo = false, fecha_baja = NOW() "
                + "WHERE id_personal = ? AND activo = true AND NOT EXISTS "
                + "(SELECT 1 FROM Asignaciones asi WHERE asi.Personal_id_personal = ? "
                + "AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE()) AND asi.anulado = false)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idPersonal);
            ps.setInt(2, idPersonal);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al desactivar personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean tieneAsignacionesActivas(int idPersonal) {
        String sql = "SELECT 1 FROM Asignaciones WHERE Personal_id_personal = ? "
                + "AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE()) AND anulado = false LIMIT 1";
        try (Connection conexion = cn.getConexion();
             PreparedStatement consulta = conexion == null ? null : conexion.prepareStatement(sql)) {
            if (consulta == null) return false;
            consulta.setInt(1, idPersonal);
            try (ResultSet resultado = consulta.executeQuery()) { return resultado.next(); }
        } catch (SQLException e) {
            System.out.println("Error al validar asignaciones activas del personal: " + e.getMessage());
            return true;
        }
    }

    private Personal mapearPersonal(ResultSet rs) throws SQLException {
        Personal per = new Personal();

        per.setIdPersonal(rs.getInt("id_personal"));
        per.setNombre(rs.getString("nombre"));
        per.setApellidos(rs.getString("apellidos"));
        per.setIdentificacion(rs.getString("identificacion"));
        per.setEmail(rs.getString("email"));
        per.setTelefono(rs.getString("telefono"));
        per.setDireccion(rs.getString("direccion"));
        per.setClave(rs.getString("clave"));
        per.setObservaciones(rs.getString("observaciones"));
        per.setPuede_acceder(rs.getBoolean("puede_acceder"));
        per.setDebeCambiarClave(rs.getBoolean("debe_cambiar_clave"));
        java.sql.Timestamp expiracion = rs.getTimestamp("clave_temporal_expira_en");
        per.setClaveTemporalExpiraEn(expiracion == null ? null : expiracion.toLocalDateTime());
        per.setFecha_contratacion(rs.getString("fecha_contratacion"));
        per.setDocumento_id_documento(rs.getInt("Documento_id_documento"));
        per.setRoles_idroles(rs.getInt("roles_idroles"));
        per.setTipo_acceso(rs.getString("tipo_acceso"));
        per.setEstado_Personal_id_estado(rs.getInt("Estado_Personal_id_estado"));

        return per;
    }

    private boolean esAdministradorMaestro(Personal personal) {
        return personal != null
                && (personal.getIdPersonal() == ID_ADMINISTRADOR_MAESTRO
                || IDENTIFICACION_ADMINISTRADOR_MAESTRO.equals(personal.getIdentificacion()));
    }

    private void mostrarPersonal(Personal p) {
        System.out.println("ID: " + p.getIdPersonal());
        System.out.println("Identificacion: " + p.getIdentificacion());
        System.out.println("Nombre: " + p.getNombre());
        System.out.println("Apellidos: " + p.getApellidos());
        System.out.println("Email: " + p.getEmail());
        System.out.println("Telefono: " + p.getTelefono());
        System.out.println("Direccion: " + p.getDireccion());
        System.out.println("Puede acceder: " + p.isPuede_acceder());
        System.out.println("Fecha contratacion: " + p.getFecha_contratacion());
        System.out.println("ID Documento: " + p.getDocumento_id_documento());
        System.out.println("ID Rol: " + p.getRoles_idroles());
        System.out.println("ID Estado: " + p.getEstado_Personal_id_estado());
    }

    private void setDateOrNull(PreparedStatement ps, int index, String fecha) throws SQLException {
        if (fecha == null || fecha.trim().isEmpty()) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(fecha));
        }
    }

    private String protegerClave(String clave) {
        return PasswordHasher.esHash(clave) ? clave : PasswordHasher.proteger(clave);
    }

    private void cerrarConexiones() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexion: " + e.getMessage());
        }
    }
}
