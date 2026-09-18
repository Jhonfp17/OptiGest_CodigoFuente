package Controlador;

import Conexion.Conexion;
import Modelo.Estado_Activo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Estado_ActivoDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuEstadoActivo(int opcion) {
        Estado_Activo estado = new Estado_Activo();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR ESTADO ACTIVO ---");
                System.out.print("Descripcion activo: ");
                estado.setDescripcion_activo(sc.nextLine());

                if (insertar(estado)) {
                    System.out.println("Estado activo registrado con exito.");
                } else {
                    System.out.println("Error al registrar estado activo.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del estado activo a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Estado_Activo encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getIdEstado_Activo());
                    System.out.println("Descripcion: " + encontrado.getDescripcion_activo());
                } else {
                    System.out.println("No se encontro estado activo con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE ESTADOS DE ACTIVO ---");

                List<Estado_Activo> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay estados de activo registrados");
                } else {
                    for (Estado_Activo e : lista) {
                        System.out.println("ID: " + e.getIdEstado_Activo());
                        System.out.println("Descripcion: " + e.getDescripcion_activo());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR ESTADO ACTIVO ---");
                System.out.print("Ingrese el ID del estado activo a modificar: ");
                estado.setIdEstado_Activo(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion activo: ");
                estado.setDescripcion_activo(sc.nextLine());

                if (actualizar(estado)) {
                    System.out.println("Estado activo actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar estado activo.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del estado activo a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Estado activo eliminado.");
                } else {
                    System.out.println("No se pudo eliminar estado activo.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Estado_Activo estado) {
        String sql = "INSERT INTO Estado_Activo (descripcion_activo) VALUES (?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getDescripcion_activo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar estado de activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Estado_Activo consultar(int idEstado_Activo) {
        Estado_Activo estado = null;
        String sql = "SELECT * FROM Estado_Activo WHERE idEstado_Activo = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstado_Activo);
            rs = ps.executeQuery();

            if (rs.next()) {
                estado = new Estado_Activo();
                estado.setIdEstado_Activo(rs.getInt("idEstado_Activo"));
                estado.setDescripcion_activo(rs.getString("descripcion_activo"));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar estado activo: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return estado;
    }

    public List<Estado_Activo> listar() {
        List<Estado_Activo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estado_Activo";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Estado_Activo estado = new Estado_Activo();
                estado.setIdEstado_Activo(rs.getInt("idEstado_Activo"));
                estado.setDescripcion_activo(rs.getString("descripcion_activo"));

                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estados de activo: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Estado_Activo estado) {
        String sql = "UPDATE Estado_Activo SET descripcion_activo = ? WHERE idEstado_Activo = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getDescripcion_activo());
            ps.setInt(2, estado.getIdEstado_Activo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado activo: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int idEstado_Activo) {
        String sql = "UPDATE Estado_Activo SET activo = false, fecha_baja = NOW() WHERE idEstado_Activo = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idEstado_Activo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar estado activo: " + e.getMessage());
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
