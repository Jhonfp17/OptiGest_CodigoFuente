package Controlador;

import Conexion.Conexion;
import Modelo.Mantenimiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MantenimientoDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuMantenimiento(int opcion) {
        Mantenimiento mante = new Mantenimiento();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR NUEVO MANTENIMIENTO ---");
                System.out.print("Fecha mantenimiento (YYYY-MM-DD): ");
                mante.setFecha_mante(sc.nextLine());
                System.out.print("Costo: ");
                mante.setCosto(sc.nextLine());
                System.out.print("Descripcion: ");
                mante.setDescripcion(sc.nextLine());
                System.out.print("ID Activo: ");
                mante.setActivos_id_activos(Integer.parseInt(sc.nextLine()));
                System.out.print("ID Proveedor: ");
                mante.setProveedores_idProveedores(Integer.parseInt(sc.nextLine()));

                if (insertar(mante)) {
                    System.out.println("Mantenimiento registrado con exito.");
                } else {
                    System.out.println("Error al registrar mantenimiento.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del mantenimiento a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());
                Mantenimiento encontrado = consultarMantenimiento(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getId_mantenimiento());
                    System.out.println("Fecha mantenimiento: " + encontrado.getFecha_mante());
                    System.out.println("Costo: " + encontrado.getCosto());
                    System.out.println("Descripcion: " + encontrado.getDescripcion());
                    System.out.println("ID Activo: " + encontrado.getActivos_id_activos());
                    System.out.println("ID Proveedor: " + encontrado.getProveedores_idProveedores());
                } else {
                    System.out.println("No se encontro mantenimiento con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO GENERAL DE MANTENIMIENTOS ---");
                List<Mantenimiento> lista = consultar();

                if (lista.isEmpty()) {
                    System.out.println("No hay mantenimientos registrados");
                } else {
                    for (Mantenimiento m : lista) {
                        System.out.println("ID: " + m.getId_mantenimiento());
                        System.out.println("Fecha mantenimiento: " + m.getFecha_mante());
                        System.out.println("Costo: " + m.getCosto());
                        System.out.println("Descripcion: " + m.getDescripcion());
                        System.out.println("ID Activo: " + m.getActivos_id_activos());
                        System.out.println("ID Proveedor: " + m.getProveedores_idProveedores());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR MANTENIMIENTO ---");
                System.out.print("Ingrese el ID del mantenimiento a modificar: ");
                mante.setId_mantenimiento(Integer.parseInt(sc.nextLine()));
                System.out.print("Nueva fecha mantenimiento (YYYY-MM-DD): ");
                mante.setFecha_mante(sc.nextLine());
                System.out.print("Nuevo costo: ");
                mante.setCosto(sc.nextLine());
                System.out.print("Nueva descripcion: ");
                mante.setDescripcion(sc.nextLine());
                System.out.print("Nuevo ID Activo: ");
                mante.setActivos_id_activos(Integer.parseInt(sc.nextLine()));
                System.out.print("Nuevo ID Proveedor: ");
                mante.setProveedores_idProveedores(Integer.parseInt(sc.nextLine()));

                if (actualizar(mante)) {
                    System.out.println("Mantenimiento actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar mantenimiento.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del mantenimiento a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Mantenimiento eliminado.");
                } else {
                    System.out.println("No se pudo eliminar mantenimiento.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Mantenimiento mante) {
        String sql = "INSERT INTO Mantenimiento (fecha_mante, costo, descripcion, Activos_id_activos, Proveedores_idProveedores) VALUES (?, ?, ?, ?, ?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, mante.getFecha_mante());
            ps.setString(2, mante.getCosto());
            ps.setString(3, mante.getDescripcion());
            ps.setInt(4, mante.getActivos_id_activos());
            ps.setInt(5, mante.getProveedores_idProveedores());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Mantenimiento consultarMantenimiento(int id_mantenimiento) {
        Mantenimiento mante = null;
        String sql = "SELECT * FROM Mantenimiento WHERE id_mantenimiento = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_mantenimiento);
            rs = ps.executeQuery();

            if (rs.next()) {
                mante = new Mantenimiento();
                mante.setId_mantenimiento(rs.getInt("id_mantenimiento"));
                mante.setFecha_mante(rs.getString("fecha_mante"));
                mante.setCosto(rs.getString("costo"));
                mante.setDescripcion(rs.getString("descripcion"));
                mante.setActivos_id_activos(rs.getInt("Activos_id_activos"));
                mante.setProveedores_idProveedores(rs.getInt("Proveedores_idProveedores"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar mantenimiento: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return mante;
    }

    public List<Mantenimiento> consultar() {
        List<Mantenimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Mantenimiento";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Mantenimiento m = new Mantenimiento();
                m.setId_mantenimiento(rs.getInt("id_mantenimiento"));
                m.setFecha_mante(rs.getString("fecha_mante"));
                m.setCosto(rs.getString("costo"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setActivos_id_activos(rs.getInt("Activos_id_activos"));
                m.setProveedores_idProveedores(rs.getInt("Proveedores_idProveedores"));
                lista.add(m);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar mantenimientos: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Mantenimiento mante) {
        String sql = "UPDATE Mantenimiento SET fecha_mante = ?, costo = ?, descripcion = ?, Activos_id_activos = ?, Proveedores_idProveedores = ? WHERE id_mantenimiento = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, mante.getFecha_mante());
            ps.setString(2, mante.getCosto());
            ps.setString(3, mante.getDescripcion());
            ps.setInt(4, mante.getActivos_id_activos());
            ps.setInt(5, mante.getProveedores_idProveedores());
            ps.setInt(6, mante.getId_mantenimiento());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar mantenimiento: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_mantenimiento) {
        String sql = "UPDATE Mantenimiento SET activo = false, fecha_baja = NOW() WHERE id_mantenimiento = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_mantenimiento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar mantenimiento: " + e.getMessage());
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
            System.out.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}
