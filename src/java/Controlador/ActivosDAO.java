package Controlador;

import Conexion.Conexion;
import Modelo.Activos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ActivosDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuActivos(int opcion) {
        Activos act = new Activos();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR ACTIVO ---");
                System.out.print("Codigo activo: ");
                act.setCodigo_act(sc.nextLine());
                System.out.print("Nombre activo: ");
                act.setNombre_activos(sc.nextLine());
                System.out.print("Valor: ");
                act.setValor(sc.nextLine());
                System.out.print("Fecha adquisicion (YYYY-MM-DD): ");
                act.setFecha_adquma(sc.nextLine());
                System.out.print("Fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
                act.setFecha_devolucion(sc.nextLine());
                System.out.print("Vida util en años: ");
                act.setVida_util(Integer.parseInt(sc.nextLine()));
                System.out.print("Descripcion: ");
                act.setDescripcion(sc.nextLine());
                System.out.print("ID Estado Activo: ");
                act.setEstado_Activo_idEstado_Activo(Integer.parseInt(sc.nextLine()));
                System.out.print("ID Categoria: ");
                act.setCategorias_idCategorias(Integer.parseInt(sc.nextLine()));
                System.out.print("ID Proveedor: ");
                act.setProveedores_idProveedores(Integer.parseInt(sc.nextLine()));

                if (insertar(act)) {
                    System.out.println("Activo registrado con exito.");
                } else {
                    System.out.println("Error al registrar activo.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del activo a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Activos encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getId_activos());
                    System.out.println("Codigo: " + encontrado.getCodigo_act());
                    System.out.println("Nombre: " + encontrado.getNombre_activos());
                    System.out.println("Valor: " + encontrado.getValor());
                    System.out.println("Fecha adquisicion: " + encontrado.getFecha_adquma());
                    System.out.println("Fecha devolucion: " + encontrado.getFecha_devolucion());
                    System.out.println("Vida util: " + encontrado.getVida_util());
                    System.out.println("Descripcion: " + encontrado.getDescripcion());
                    System.out.println("ID Estado: " + encontrado.getEstado_Activo_idEstado_Activo());
                    System.out.println("ID Categoria: " + encontrado.getCategorias_idCategorias());
                    System.out.println("ID Proveedor: " + encontrado.getProveedores_idProveedores());
                } else {
                    System.out.println("No se encontro activo con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE ACTIVOS ---");

                List<Activos> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay activos registrados");
                } else {
                    for (Activos a : lista) {
                        System.out.println("ID: " + a.getId_activos());
                        System.out.println("Codigo: " + a.getCodigo_act());
                        System.out.println("Nombre: " + a.getNombre_activos());
                        System.out.println("Valor: " + a.getValor());
                        System.out.println("Fecha adquisicion: " + a.getFecha_adquma());
                        System.out.println("Fecha devolucion: " + a.getFecha_devolucion());
                        System.out.println("Vida util: " + a.getVida_util());
                        System.out.println("Descripcion: " + a.getDescripcion());
                        System.out.println("ID Estado: " + a.getEstado_Activo_idEstado_Activo());
                        System.out.println("ID Categoria: " + a.getCategorias_idCategorias());
                        System.out.println("ID Proveedor: " + a.getProveedores_idProveedores());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR ACTIVO ---");
                System.out.print("Ingrese el ID del activo a modificar: ");
                act.setId_activos(Integer.parseInt(sc.nextLine()));

                System.out.print("Nuevo codigo activo: ");
                act.setCodigo_act(sc.nextLine());
                System.out.print("Nuevo nombre activo: ");
                act.setNombre_activos(sc.nextLine());
                System.out.print("Nuevo valor: ");
                act.setValor(sc.nextLine());
                System.out.print("Nueva fecha adquisicion (YYYY-MM-DD): ");
                act.setFecha_adquma(sc.nextLine());
                System.out.print("Nueva fecha devolucion (YYYY-MM-DD o vacio si no aplica): ");
                act.setFecha_devolucion(sc.nextLine());
                System.out.print("Nueva vida util en anios: ");
                act.setVida_util(Integer.parseInt(sc.nextLine()));
                System.out.print("Nueva descripcion: ");
                act.setDescripcion(sc.nextLine());
                System.out.print("Nuevo ID Estado Activo: ");
                act.setEstado_Activo_idEstado_Activo(Integer.parseInt(sc.nextLine()));
                System.out.print("Nuevo ID Categoria: ");
                act.setCategorias_idCategorias(Integer.parseInt(sc.nextLine()));
                System.out.print("Nuevo ID Proveedor: ");
                act.setProveedores_idProveedores(Integer.parseInt(sc.nextLine()));

                if (actualizar(act)) {
                    System.out.println("Activo actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar activo.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del activo a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Activo eliminado.");
                } else {
                    System.out.println("No se pudo eliminar activo.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Activos act) {
        String sql = "INSERT INTO Activos (codigo_act, nombre_activos, valor, fecha_adquma, fecha_devolucion, vida_util, descripcion, Estado_Activo_idEstado_Activo, Categorias_idCategorias, Proveedores_idProveedores) "
                + "SELECT ?, ?, ?, ?, ?, ?, ?, ?, ?, ? WHERE NOT EXISTS "
                + "(SELECT 1 FROM Activos WHERE LOWER(TRIM(codigo_act)) = LOWER(TRIM(?)))";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, act.getCodigo_act());
            ps.setString(2, act.getNombre_activos());
            ps.setString(3, act.getValor());
            setDateOrNull(ps, 4, act.getFecha_adquma());
            setDateOrNull(ps, 5, act.getFecha_devolucion());
            ps.setInt(6, act.getVida_util());
            ps.setString(7, act.getDescripcion());
            ps.setInt(8, act.getEstado_Activo_idEstado_Activo());
            ps.setInt(9, act.getCategorias_idCategorias());
            ps.setInt(10, act.getProveedores_idProveedores());
            ps.setString(11, act.getCodigo_act());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Activos consultar(int id_activos) {
        Activos act = null;
        String sql = "SELECT * FROM Activos WHERE id_activos = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_activos);
            rs = ps.executeQuery();

            if (rs.next()) {
                act = new Activos();
                act.setId_activos(rs.getInt("id_activos"));
                act.setCodigo_act(rs.getString("codigo_act"));
                act.setNombre_activos(rs.getString("nombre_activos"));
                act.setValor(rs.getString("valor"));
                act.setFecha_adquma(rs.getString("fecha_adquma"));
                act.setFecha_devolucion(rs.getString("fecha_devolucion"));
                act.setVida_util(rs.getInt("vida_util"));
                act.setDescripcion(rs.getString("descripcion"));
                act.setEstado_Activo_idEstado_Activo(rs.getInt("Estado_Activo_idEstado_Activo"));
                act.setCategorias_idCategorias(rs.getInt("Categorias_idCategorias"));
                act.setProveedores_idProveedores(rs.getInt("Proveedores_idProveedores"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar activo: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return act;
    }

    public List<Activos> listar() {
        return listarPorEstado(true);
    }

    /**
     * Activos que se pueden seleccionar para una nueva asignación. Excluye los
     * retirados, los que no están en estado DISPONIBLE, los que tienen un
     * mantenimiento activo y los que ya están asignados. Al editar, se admite
     * el activo de la propia asignación para conservar su valor actual.
     */
    public List<Activos> listarDisponiblesParaAsignacion(Integer idAsignacionActual) {
        List<Activos> lista = new ArrayList<>();
        String sql = "SELECT a.* FROM Activos a "
                + "INNER JOIN Estado_Activo ea ON ea.idEstado_Activo = a.Estado_Activo_idEstado_Activo "
                + "WHERE a.activo = true "
                + "AND UPPER(TRIM(ea.descripcion_activo)) = 'DISPONIBLE' "
                + "AND NOT EXISTS (SELECT 1 FROM Mantenimiento m "
                + "WHERE m.Activos_id_activos = a.id_activos AND m.activo = true) "
                + "AND NOT EXISTS (SELECT 1 FROM Asignaciones asi "
                + "WHERE asi.Activos_id_activos = a.id_activos "
                + "AND asi.anulado = false AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE()) "
                + "AND (? IS NULL OR asi.id_asignaciones <> ?)) "
                + "ORDER BY a.nombre_activos, a.codigo_act";

        try (Connection conexion = cn.getConexion();
             PreparedStatement consulta = conexion == null ? null : conexion.prepareStatement(sql)) {
            if (consulta == null) return lista;
            if (idAsignacionActual == null) {
                consulta.setNull(1, java.sql.Types.INTEGER);
                consulta.setNull(2, java.sql.Types.INTEGER);
            } else {
                consulta.setInt(1, idAsignacionActual);
                consulta.setInt(2, idAsignacionActual);
            }
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) lista.add(mapearActivo(resultado));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar activos disponibles: " + e.getMessage());
        }
        return lista;
    }

    /** Lista los activos retirados para las consultas administrativas. */
    public List<Activos> listarInactivos() {
        return listarPorEstado(false);
    }

    private List<Activos> listarPorEstado(boolean activos) {
        List<Activos> lista = new ArrayList<>();
        String sql = "SELECT a.*, CASE "
                + "WHEN a.activo = false THEN 'RETIRADO' "
                + "WHEN EXISTS (SELECT 1 FROM Mantenimiento m "
                + "WHERE m.Activos_id_activos = a.id_activos AND m.activo = true) THEN 'EN MANTENIMIENTO' "
                + "WHEN EXISTS (SELECT 1 FROM Asignaciones asi "
                + "WHERE asi.Activos_id_activos = a.id_activos AND asi.anulado = false "
                + "AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE())) THEN 'ASIGNADO' "
                + "ELSE 'DISPONIBLE' END AS estado_actual "
                + "FROM Activos a WHERE a.activo = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setBoolean(1, activos);
            rs = ps.executeQuery();

            while (rs.next()) {
                Activos activo = mapearActivo(rs);
                activo.setEstadoActual(rs.getString("estado_actual"));
                lista.add(activo);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar activos: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Activos act) {
        String sql = "UPDATE Activos SET codigo_act = ?, nombre_activos = ?, valor = ?, fecha_adquma = ?, fecha_devolucion = ?, vida_util = ?, descripcion = ?, Estado_Activo_idEstado_Activo = ?, Categorias_idCategorias = ?, Proveedores_idProveedores = ? "
                + "WHERE id_activos = ? AND activo = true AND NOT EXISTS (SELECT 1 FROM Activos duplicado "
                + "WHERE LOWER(TRIM(duplicado.codigo_act)) = LOWER(TRIM(?)) AND duplicado.id_activos <> ?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, act.getCodigo_act());
            ps.setString(2, act.getNombre_activos());
            ps.setString(3, act.getValor());
            setDateOrNull(ps, 4, act.getFecha_adquma());
            setDateOrNull(ps, 5, act.getFecha_devolucion());
            ps.setInt(6, act.getVida_util());
            ps.setString(7, act.getDescripcion());
            ps.setInt(8, act.getEstado_Activo_idEstado_Activo());
            ps.setInt(9, act.getCategorias_idCategorias());
            ps.setInt(10, act.getProveedores_idProveedores());
            ps.setInt(11, act.getId_activos());
            ps.setString(12, act.getCodigo_act());
            ps.setInt(13, act.getId_activos());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_activos) {
        String sql = "UPDATE Activos SET activo = false, fecha_baja = NOW() WHERE id_activos = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_activos);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar activo: " + e.getMessage());
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

    private Activos mapearActivo(ResultSet resultado) throws SQLException {
        Activos act = new Activos();
        act.setId_activos(resultado.getInt("id_activos"));
        act.setCodigo_act(resultado.getString("codigo_act"));
        act.setNombre_activos(resultado.getString("nombre_activos"));
        act.setValor(resultado.getString("valor"));
        act.setFecha_adquma(resultado.getString("fecha_adquma"));
        act.setFecha_devolucion(resultado.getString("fecha_devolucion"));
        act.setVida_util(resultado.getInt("vida_util"));
        act.setDescripcion(resultado.getString("descripcion"));
        act.setEstado_Activo_idEstado_Activo(resultado.getInt("Estado_Activo_idEstado_Activo"));
        act.setCategorias_idCategorias(resultado.getInt("Categorias_idCategorias"));
        act.setProveedores_idProveedores(resultado.getInt("Proveedores_idProveedores"));
        return act;
    }

    /**
     * Verifica si un activo puede retirarse sin perder la trazabilidad de sus
     * relaciones. El valor null indica que el retiro es permitido.
     */
    public String motivoBloqueoBaja(int idActivos) {
        String sql = "SELECT a.activo, "
                + "EXISTS(SELECT 1 FROM Mantenimiento m "
                + "WHERE m.Activos_id_activos = a.id_activos AND m.activo = true) AS en_mantenimiento, "
                + "EXISTS(SELECT 1 FROM Asignaciones asi "
                + "WHERE asi.Activos_id_activos = a.id_activos "
                + "AND asi.anulado = false "
                + "AND (asi.fecha_devolucion IS NULL OR asi.fecha_devolucion >= CURDATE())) AS asignado "
                + "FROM Activos a WHERE a.id_activos = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idActivos);
            rs = ps.executeQuery();

            if (!rs.next() || !rs.getBoolean("activo")) {
                return "activo_retirado";
            }
            if (rs.getBoolean("en_mantenimiento")) {
                return "mantenimiento";
            }
            if (rs.getBoolean("asignado")) {
                return "asignado";
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error al validar baja de activo: " + e.getMessage());
            return "validacion";
        } finally {
            cerrarConexiones();
        }
    }

    public boolean codigoDisponible(String codigo, int idActual) {
        String sql = "SELECT 1 FROM Activos WHERE LOWER(TRIM(codigo_act)) = LOWER(TRIM(?)) AND id_activos <> ?";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, codigo);
            ps.setInt(2, idActual);
            rs = ps.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            System.out.println("Error al validar codigo de activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean estaActivo(int idActivos) {
        String sql = "SELECT activo FROM Activos WHERE id_activos = ?";
        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idActivos);
            rs = ps.executeQuery();
            return rs.next() && rs.getBoolean("activo");
        } catch (SQLException e) {
            System.out.println("Error al validar estado del activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
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
