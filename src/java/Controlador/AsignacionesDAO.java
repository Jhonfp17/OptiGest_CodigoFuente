package Controlador;

import Conexion.Conexion;
import Modelo.AsignacionConsulta;
import Modelo.Asignaciones;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AsignacionesDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuAsignaciones(int opcion) {
        Asignaciones asig = new Asignaciones();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR ASIGNACION ---");
                System.out.print("Fecha asignacion (YYYY-MM-DD): ");
                asig.setFecha_asignacion(sc.nextLine());
                System.out.print("Fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
                asig.setFecha_devolucion(sc.nextLine());
                System.out.print("Observaciones: ");
                asig.setObservaciones(sc.nextLine());
                System.out.print("ID Personal: ");
                asig.setPersonal_id_personal(Integer.parseInt(sc.nextLine()));
                System.out.print("ID Activo: ");
                asig.setActivos_id_activos(Integer.parseInt(sc.nextLine()));

                if (insertar(asig)) {
                    System.out.println("Asignacion registrada con exito.");
                } else {
                    System.out.println("Error al registrar asignacion.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID de la asignacion a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Asignaciones encontrada = consultar(idBuscar);

                if (encontrada != null) {
                    System.out.println("ID: " + encontrada.getId_asignaciones());
                    System.out.println("Fecha asignacion: " + encontrada.getFecha_asignacion());
                    System.out.println("Fecha devolucion: " + encontrada.getFecha_devolucion());
                    System.out.println("Observaciones: " + encontrada.getObservaciones());
                    System.out.println("ID Personal: " + encontrada.getPersonal_id_personal());
                    System.out.println("ID Activo: " + encontrada.getActivos_id_activos());
                } else {
                    System.out.println("No se encontro asignacion con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE ASIGNACIONES ---");

                List<Asignaciones> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay asignaciones registradas");
                } else {
                    for (Asignaciones a : lista) {
                        System.out.println("ID: " + a.getId_asignaciones());
                        System.out.println("Fecha asignacion: " + a.getFecha_asignacion());
                        System.out.println("Fecha devolucion: " + a.getFecha_devolucion());
                        System.out.println("Observaciones: " + a.getObservaciones());
                        System.out.println("ID Personal: " + a.getPersonal_id_personal());
                        System.out.println("ID Activo: " + a.getActivos_id_activos());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR ASIGNACION ---");
                System.out.print("Ingrese el ID de la asignacion a modificar: ");
                asig.setId_asignaciones(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva fecha asignacion (YYYY-MM-DD): ");
                asig.setFecha_asignacion(sc.nextLine());
                System.out.print("Nueva fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
                asig.setFecha_devolucion(sc.nextLine());
                System.out.print("Nuevas observaciones: ");
                asig.setObservaciones(sc.nextLine());
                System.out.print("Nuevo ID Personal: ");
                asig.setPersonal_id_personal(Integer.parseInt(sc.nextLine()));
                System.out.print("Nuevo ID Activo: ");
                asig.setActivos_id_activos(Integer.parseInt(sc.nextLine()));

                if (actualizar(asig)) {
                    System.out.println("Asignacion actualizada con exito.");
                } else {
                    System.out.println("Error al actualizar asignacion.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID de la asignacion a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Asignacion eliminada.");
                } else {
                    System.out.println("No se pudo eliminar asignacion.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Asignaciones asig) {
        String sql = "INSERT INTO Asignaciones (fecha_asignacion, fecha_devolucion, observaciones, Personal_id_personal, Activos_id_activos) "
                + "SELECT ?, ?, ?, ?, ? WHERE EXISTS (SELECT 1 FROM Activos WHERE id_activos = ? AND activo = true) "
                + "AND EXISTS (SELECT 1 FROM Personal WHERE id_personal = ? AND activo = true) "
                + "AND NOT EXISTS (SELECT 1 FROM Asignaciones WHERE Activos_id_activos = ? AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE()) AND anulado = false)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            setDateOrNull(ps, 1, asig.getFecha_asignacion());
            setDateOrNull(ps, 2, asig.getFecha_devolucion());
            ps.setString(3, asig.getObservaciones());
            ps.setInt(4, asig.getPersonal_id_personal());
            ps.setInt(5, asig.getActivos_id_activos());
            ps.setInt(6, asig.getActivos_id_activos());
            ps.setInt(7, asig.getPersonal_id_personal());
            ps.setInt(8, asig.getActivos_id_activos());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar asignacion: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Asignaciones consultar(int id_asignaciones) {
        Asignaciones asig = null;
        String sql = "SELECT * FROM Asignaciones WHERE id_asignaciones = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_asignaciones);
            rs = ps.executeQuery();

            if (rs.next()) {
                asig = new Asignaciones();
                asig.setId_asignaciones(rs.getInt("id_asignaciones"));
                asig.setFecha_asignacion(rs.getString("fecha_asignacion"));
                asig.setFecha_devolucion(rs.getString("fecha_devolucion"));
                asig.setObservaciones(rs.getString("observaciones"));
                asig.setPersonal_id_personal(rs.getInt("Personal_id_personal"));
                asig.setActivos_id_activos(rs.getInt("Activos_id_activos"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar asignacion: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return asig;
    }

    /** Una asignación cerrada o anulada conserva historial, pero no se edita. */
    public boolean estaDisponibleParaEdicion(int idAsignacion) {
        String sql = "SELECT 1 FROM Asignaciones WHERE id_asignaciones = ? "
                + "AND anulado = false AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE())";
        try (Connection conexion = cn.getConexion();
             PreparedStatement consulta = conexion == null ? null : conexion.prepareStatement(sql)) {
            if (consulta == null) return false;
            consulta.setInt(1, idAsignacion);
            try (ResultSet resultado = consulta.executeQuery()) { return resultado.next(); }
        } catch (SQLException e) {
            System.out.println("Error al validar disponibilidad de asignacion: " + e.getMessage());
            return false;
        }
    }

    public List<Asignaciones> listar() {
        List<Asignaciones> lista = new ArrayList<>();
        String sql = "SELECT * FROM Asignaciones";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Asignaciones a = new Asignaciones();
                a.setId_asignaciones(rs.getInt("id_asignaciones"));
                a.setFecha_asignacion(rs.getString("fecha_asignacion"));
                a.setFecha_devolucion(rs.getString("fecha_devolucion"));
                a.setObservaciones(rs.getString("observaciones"));
                a.setPersonal_id_personal(rs.getInt("Personal_id_personal"));
                a.setActivos_id_activos(rs.getInt("Activos_id_activos"));
                lista.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar asignaciones: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public List<AsignacionConsulta> consultarPorDocumentoYRango(String documento, String fechaDesde, String fechaHasta) {
        return consultarPorFiltroYRango("documento", documento, fechaDesde, fechaHasta);
    }

    public List<AsignacionConsulta> consultarPorFiltroYRango(String tipoBusqueda, String valorBusqueda, String fechaDesde, String fechaHasta) {
        return consultarPorFiltroYRango(tipoBusqueda, valorBusqueda, fechaDesde, fechaHasta, 0);
    }

    public List<AsignacionConsulta> listarUltimasConsulta(int limite) {
        List<AsignacionConsulta> lista = new ArrayList<>();
        String sql = "SELECT asi.id_asignaciones, asi.fecha_asignacion, asi.fecha_devolucion, "
                + "asi.observaciones, p.identificacion, p.nombre, p.apellidos, "
                + "act.nombre_activos, act.codigo_act, "
                + "CASE WHEN EXISTS (SELECT 1 FROM Asignaciones actual "
                + "WHERE actual.Activos_id_activos = act.id_activos "
                + "AND (actual.fecha_devolucion IS NULL OR actual.fecha_devolucion >= CURDATE()) AND actual.anulado = false) "
                + "THEN 'ASIGNADO' ELSE COALESCE(ea.descripcion_activo, 'NO DISPONIBLE') END AS estado_activo "
                + "FROM Asignaciones asi "
                + "INNER JOIN Personal p ON p.id_personal = asi.Personal_id_personal "
                + "INNER JOIN Activos act ON act.id_activos = asi.Activos_id_activos "
                + "LEFT JOIN Estado_Activo ea ON ea.idEstado_Activo = act.Estado_Activo_idEstado_Activo "
                + "ORDER BY asi.fecha_asignacion DESC, asi.id_asignaciones DESC";

        if (limite > 0) {
            sql += " LIMIT ?";
        }

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            if (limite > 0) {
                ps.setInt(1, limite);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                AsignacionConsulta item = new AsignacionConsulta();
                item.setIdAsignacion(rs.getInt("id_asignaciones"));
                item.setFechaAsignacion(rs.getString("fecha_asignacion"));
                item.setFechaDevolucion(rs.getString("fecha_devolucion"));
                item.setObservaciones(rs.getString("observaciones"));
                item.setIdentificacion(rs.getString("identificacion"));
                item.setNombrePersona(rs.getString("nombre"));
                item.setApellidosPersona(rs.getString("apellidos"));
                item.setNombreActivo(rs.getString("nombre_activos"));
                item.setCodigoActivo(rs.getString("codigo_act"));
                item.setEstadoActivo(rs.getString("estado_activo"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar ultimas asignaciones: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public List<AsignacionConsulta> consultarPorFiltroYRango(String tipoBusqueda, String valorBusqueda, String fechaDesde, String fechaHasta, int limite) {
        List<AsignacionConsulta> lista = new ArrayList<>();
        boolean buscarPorActivo = "activo".equalsIgnoreCase(tipoBusqueda);
        String filtroSql = buscarPorActivo
                ? "(act.nombre_activos LIKE ? OR act.codigo_act LIKE ?) "
                : "p.identificacion = ? ";

        String sql = "SELECT asi.id_asignaciones, asi.fecha_asignacion, asi.fecha_devolucion, "
                + "asi.observaciones, p.identificacion, p.nombre, p.apellidos, "
                + "act.nombre_activos, act.codigo_act, "
                + "CASE WHEN EXISTS (SELECT 1 FROM Asignaciones actual "
                + "WHERE actual.Activos_id_activos = act.id_activos "
                + "AND (actual.fecha_devolucion IS NULL OR actual.fecha_devolucion >= CURDATE()) AND actual.anulado = false) "
                + "THEN 'ASIGNADO' ELSE COALESCE(ea.descripcion_activo, 'NO DISPONIBLE') END AS estado_activo "
                + "FROM Asignaciones asi "
                + "INNER JOIN Personal p ON p.id_personal = asi.Personal_id_personal "
                + "INNER JOIN Activos act ON act.id_activos = asi.Activos_id_activos "
                + "LEFT JOIN Estado_Activo ea ON ea.idEstado_Activo = act.Estado_Activo_idEstado_Activo "
                + "WHERE " + filtroSql
                + "AND (? IS NULL OR asi.fecha_asignacion >= ?) "
                + "AND (? IS NULL OR asi.fecha_asignacion <= ? OR asi.fecha_devolucion <= ?) "
                + "ORDER BY asi.fecha_asignacion DESC, asi.id_asignaciones DESC";

        if (limite > 0) {
            sql += " LIMIT ?";
        }

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            int parametroFecha = 2;
            if (buscarPorActivo) {
                String valorActivo = "%" + valorBusqueda + "%";
                ps.setString(1, valorActivo);
                ps.setString(2, valorActivo);
                parametroFecha = 3;
            } else {
                ps.setString(1, valorBusqueda);
            }

            setDateOrNull(ps, parametroFecha, fechaDesde);
            setDateOrNull(ps, parametroFecha + 1, fechaDesde);
            setDateOrNull(ps, parametroFecha + 2, fechaHasta);
            setDateOrNull(ps, parametroFecha + 3, fechaHasta);
            setDateOrNull(ps, parametroFecha + 4, fechaHasta);
            if (limite > 0) {
                ps.setInt(parametroFecha + 5, limite);
            }
            rs = ps.executeQuery();

            while (rs.next()) {
                AsignacionConsulta item = new AsignacionConsulta();
                item.setIdAsignacion(rs.getInt("id_asignaciones"));
                item.setFechaAsignacion(rs.getString("fecha_asignacion"));
                item.setFechaDevolucion(rs.getString("fecha_devolucion"));
                item.setObservaciones(rs.getString("observaciones"));
                item.setIdentificacion(rs.getString("identificacion"));
                item.setNombrePersona(rs.getString("nombre"));
                item.setApellidosPersona(rs.getString("apellidos"));
                item.setNombreActivo(rs.getString("nombre_activos"));
                item.setCodigoActivo(rs.getString("codigo_act"));
                item.setEstadoActivo(rs.getString("estado_activo"));
                lista.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar asignaciones por filtro: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    /** Asignaciones vigentes (sin devolucion ni anulacion) de un empleado, para su propio perfil. */
    public List<AsignacionConsulta> listarActivasPorPersonal(int idPersonal) {
        List<AsignacionConsulta> lista = new ArrayList<>();
        String sql = "SELECT asi.id_asignaciones, asi.fecha_asignacion, asi.fecha_devolucion, "
                + "asi.observaciones, p.identificacion, p.nombre, p.apellidos, "
                + "act.nombre_activos, act.codigo_act, "
                + "CASE WHEN EXISTS (SELECT 1 FROM Asignaciones actual "
                + "WHERE actual.Activos_id_activos = act.id_activos "
                + "AND (actual.fecha_devolucion IS NULL OR actual.fecha_devolucion >= CURDATE()) AND actual.anulado = false) "
                + "THEN 'ASIGNADO' ELSE COALESCE(ea.descripcion_activo, 'NO DISPONIBLE') END AS estado_activo "
                + "FROM Asignaciones asi "
                + "INNER JOIN Personal p ON p.id_personal = asi.Personal_id_personal "
                + "INNER JOIN Activos act ON act.id_activos = asi.Activos_id_activos "
                + "LEFT JOIN Estado_Activo ea ON ea.idEstado_Activo = act.Estado_Activo_idEstado_Activo "
                + "WHERE asi.Personal_id_personal = ? AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE()) AND asi.anulado = false "
                + "ORDER BY asi.fecha_asignacion DESC, asi.id_asignaciones DESC";

        try (Connection con = cn.getConexion();
             PreparedStatement ps = con == null ? null : con.prepareStatement(sql)) {
            if (ps == null) return lista;
            ps.setInt(1, idPersonal);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AsignacionConsulta item = new AsignacionConsulta();
                    item.setIdAsignacion(rs.getInt("id_asignaciones"));
                    item.setFechaAsignacion(rs.getString("fecha_asignacion"));
                    item.setFechaDevolucion(rs.getString("fecha_devolucion"));
                    item.setObservaciones(rs.getString("observaciones"));
                    item.setIdentificacion(rs.getString("identificacion"));
                    item.setNombrePersona(rs.getString("nombre"));
                    item.setApellidosPersona(rs.getString("apellidos"));
                    item.setNombreActivo(rs.getString("nombre_activos"));
                    item.setCodigoActivo(rs.getString("codigo_act"));
                    item.setEstadoActivo(rs.getString("estado_activo"));
                    lista.add(item);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar activos asignados del empleado: " + e.getMessage());
        }

        return lista;
    }

    public boolean actualizar(Asignaciones asig) {
        // Una asignacion devuelta, anulada o cerrada es evidencia: no se reescribe.
        // Ante un error se debe anular mientras este abierta y crear la operacion correcta.
        String sql = "UPDATE Asignaciones SET fecha_asignacion = ?, fecha_devolucion = ?, observaciones = ?, Personal_id_personal = ?, Activos_id_activos = ? "
                + "WHERE id_asignaciones = ? AND anulado = false AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE())";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            setDateOrNull(ps, 1, asig.getFecha_asignacion());
            setDateOrNull(ps, 2, asig.getFecha_devolucion());
            ps.setString(3, asig.getObservaciones());
            ps.setInt(4, asig.getPersonal_id_personal());
            ps.setInt(5, asig.getActivos_id_activos());
            ps.setInt(6, asig.getId_asignaciones());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar asignacion: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_asignaciones) {
        return anularPorId(id_asignaciones);
    }

    /** Devuelve el motivo de rechazo o null cuando se permite asignar el activo. */
    public String validarNuevaAsignacion(int idPersonal, int idActivo) {
        String sql = "SELECT "
                + "EXISTS(SELECT 1 FROM Activos a "
                + "INNER JOIN Estado_Activo ea ON ea.idEstado_Activo = a.Estado_Activo_idEstado_Activo "
                + "WHERE a.id_activos = ? AND a.activo = true "
                + "AND UPPER(TRIM(ea.descripcion_activo)) = 'DISPONIBLE' "
                + "AND NOT EXISTS (SELECT 1 FROM Mantenimiento m "
                + "WHERE m.Activos_id_activos = a.id_activos AND m.activo = true)) AS activo_valido, "
                + "EXISTS(SELECT 1 FROM Personal WHERE id_personal = ? AND activo = true) AS personal_valido, "
                + "EXISTS(SELECT 1 FROM Asignaciones WHERE Activos_id_activos = ? AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE()) AND anulado = false) AS activo_asignado";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idActivo);
            ps.setInt(2, idPersonal);
            ps.setInt(3, idActivo);
            rs = ps.executeQuery();
            if (!rs.next()) return "validacion_asignacion";
            if (!rs.getBoolean("activo_valido")) return "activo_no_disponible";
            if (!rs.getBoolean("personal_valido")) return "personal_retirado";
            if (rs.getBoolean("activo_asignado")) return "activo_ya_asignado";
            return null;
        } catch (SQLException e) {
            System.out.println("Error al validar disponibilidad del activo: " + e.getMessage());
            return "validacion_asignacion";
        } finally {
            cerrarConexiones();
        }
    }

    /**
     * Aplica las mismas reglas de disponibilidad al editar, excluyendo la
     * asignacion que se esta modificando para que conserve su propio activo.
     */
    public String validarEdicionAsignacion(int idAsignacion, int idPersonal, int idActivo) {
        String sql = "SELECT "
                + "EXISTS(SELECT 1 FROM Activos a "
                + "INNER JOIN Estado_Activo ea ON ea.idEstado_Activo = a.Estado_Activo_idEstado_Activo "
                + "WHERE a.id_activos = ? AND a.activo = true "
                + "AND UPPER(TRIM(ea.descripcion_activo)) = 'DISPONIBLE' "
                + "AND NOT EXISTS (SELECT 1 FROM Mantenimiento m "
                + "WHERE m.Activos_id_activos = a.id_activos AND m.activo = true)) AS activo_valido, "
                + "EXISTS(SELECT 1 FROM Personal WHERE id_personal = ? AND activo = true) AS personal_valido, "
                + "EXISTS(SELECT 1 FROM Asignaciones WHERE Activos_id_activos = ? "
                + "AND id_asignaciones <> ? AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE()) AND anulado = false) AS activo_asignado";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idActivo);
            ps.setInt(2, idPersonal);
            ps.setInt(3, idActivo);
            ps.setInt(4, idAsignacion);
            rs = ps.executeQuery();
            if (!rs.next()) return "validacion_asignacion";
            if (!rs.getBoolean("activo_valido")) return "activo_no_disponible";
            if (!rs.getBoolean("personal_valido")) return "personal_retirado";
            if (rs.getBoolean("activo_asignado")) return "activo_ya_asignado";
            return null;
        } catch (SQLException e) {
            System.out.println("Error al validar edicion de asignacion: " + e.getMessage());
            return "validacion_asignacion";
        } finally {
            cerrarConexiones();
        }
    }

    /** Conserva una asignacion abierta y deja una marca visible en su historial. */
    public boolean anularPorId(int idAsignaciones) {
        String sql = "UPDATE Asignaciones SET anulado = true, "
                + "observaciones = CASE "
                + "WHEN COALESCE(observaciones, '') LIKE '%[ANULADA - registro conservado]%' THEN observaciones "
                + "WHEN COALESCE(observaciones, '') = '' THEN '[ANULADA - registro conservado]' "
                + "ELSE CONCAT(observaciones, ' [ANULADA - registro conservado]') END "
                + "WHERE id_asignaciones = ? AND anulado = false AND (fecha_devolucion IS NULL OR fecha_devolucion >= CURDATE())";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idAsignaciones);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al anular asignacion: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
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
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }

    private void setDateOrNull(PreparedStatement ps, int index, String fecha) throws SQLException {
        if (fecha == null || fecha.trim().isEmpty()) {
            ps.setNull(index, Types.DATE);
        } else {
            ps.setDate(index, Date.valueOf(fecha));
        }
    }
}

