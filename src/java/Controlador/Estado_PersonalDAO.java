package Controlador;

import Conexion.Conexion;
import Modelo.Estado_Personal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Estado_PersonalDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuEstadoPersonal(int opcion) {
        Estado_Personal estado = new Estado_Personal();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR ESTADO PERSONAL ---");
                System.out.print("Descripcion estado: ");
                estado.setDescripcion_estado(sc.nextLine());

                if (insertar(estado)) {
                    System.out.println("Estado personal registrado con exito.");
                } else {
                    System.out.println("Error al registrar estado personal.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del estado a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Estado_Personal encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getId_estado());
                    System.out.println("Descripcion: " + encontrado.getDescripcion_estado());
                } else {
                    System.out.println("No se encontro estado personal con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE ESTADOS DE PERSONAL ---");

                List<Estado_Personal> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay estados de personal registrados");
                } else {
                    for (Estado_Personal e : lista) {
                        System.out.println("ID: " + e.getId_estado());
                        System.out.println("Descripcion: " + e.getDescripcion_estado());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR ESTADO PERSONAL ---");
                System.out.print("Ingrese el ID del estado a modificar: ");
                estado.setId_estado(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion estado: ");
                estado.setDescripcion_estado(sc.nextLine());

                if (actualizar(estado)) {
                    System.out.println("Estado personal actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar estado personal.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del estado a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Estado personal eliminado.");
                } else {
                    System.out.println("No se pudo eliminar estado personal.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Estado_Personal estado) {
        String sql = "INSERT INTO Estado_Personal (descripcion_estado) VALUES (?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getDescripcion_estado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar estado personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Estado_Personal consultar(int id_estado) {
        Estado_Personal estado = null;
        String sql = "SELECT * FROM Estado_Personal WHERE id_estado = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_estado);
            rs = ps.executeQuery();

            if (rs.next()) {
                estado = new Estado_Personal();
                estado.setId_estado(rs.getInt("id_estado"));
                estado.setDescripcion_estado(rs.getString("descripcion_estado"));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar estado personal: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return estado;
    }

    public List<Estado_Personal> listar() {
        List<Estado_Personal> lista = new ArrayList<>();
        String sql = "SELECT * FROM Estado_Personal";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Estado_Personal estado = new Estado_Personal();

                estado.setId_estado(rs.getInt("id_estado"));
                estado.setDescripcion_estado(rs.getString("descripcion_estado"));

                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estados de personal: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Estado_Personal estado) {
        String sql = "UPDATE Estado_Personal SET descripcion_estado = ? WHERE id_estado = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, estado.getDescripcion_estado());
            ps.setInt(2, estado.getId_estado());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estado personal: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_estado) {
        String sql = "UPDATE Estado_Personal SET activo = false, fecha_baja = NOW() WHERE id_estado = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_estado);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar estado personal: " + e.getMessage());
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
            System.out.println("Error al cerrar conexion: " + e.getMessage());
        }
    }
}
