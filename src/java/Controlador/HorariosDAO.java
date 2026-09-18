package Controlador;

import Conexion.Conexion;
import Modelo.Horarios;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HorariosDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuHorarios(int opcion) {
        Horarios horario = new Horarios();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR NUEVO HORARIO ---");

                System.out.print("Descripcion: ");
                horario.setDescripcion(sc.nextLine());

                System.out.print("Hora ingreso (HH:MM:SS): ");
                horario.setHora_ingreso(sc.nextLine());

                System.out.print("Hora salida (HH:MM:SS): ");
                horario.setHora_salida(sc.nextLine());

                if (insertar(horario)) {
                    System.out.println("Horario registrado con exito.");
                } else {
                    System.out.println("Error al registrar horario.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del horario a buscar: ");
                int idBusq = Integer.parseInt(sc.nextLine());

                Horarios encontrado = consultarHorario(idBusq);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getId_horarios());
                    System.out.println("Descripcion: " + encontrado.getDescripcion());
                    System.out.println("Hora ingreso: " + encontrado.getHora_ingreso());
                    System.out.println("Hora salida: " + encontrado.getHora_salida());
                } else {
                    System.out.println("No se encontro horario con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO GENERAL DE HORARIOS ---");

                List<Horarios> lista = consultarTodo();

                if (lista.isEmpty()) {
                    System.out.println("No hay horarios registrados");
                } else {
                    for (Horarios h : lista) {
                        System.out.println("ID: " + h.getId_horarios());
                        System.out.println("Descripcion: " + h.getDescripcion());
                        System.out.println("Hora ingreso: " + h.getHora_ingreso());
                        System.out.println("Hora salida: " + h.getHora_salida());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR HORARIO ---");

                System.out.print("Ingrese el ID del horario a modificar: ");
                horario.setId_horarios(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion: ");
                horario.setDescripcion(sc.nextLine());

                System.out.print("Nueva hora ingreso (HH:MM:SS): ");
                horario.setHora_ingreso(sc.nextLine());

                System.out.print("Nueva hora salida (HH:MM:SS): ");
                horario.setHora_salida(sc.nextLine());

                if (actualizar(horario)) {
                    System.out.println("Horario actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar horario.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del horario a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Horario eliminado.");
                } else {
                    System.out.println("No se pudo eliminar horario.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Horarios horario) {
        String sql = "INSERT INTO Horarios (descripcion, hora_ingreso, hora_salida) VALUES (?, ?, ?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, horario.getDescripcion());
            ps.setString(2, horario.getHora_ingreso());
            ps.setString(3, horario.getHora_salida());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al insertar horario: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Horarios consultarHorario(int id_horarios) {
        Horarios horario = null;
        String sql = "SELECT * FROM Horarios WHERE id_horarios = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_horarios);

            rs = ps.executeQuery();

            if (rs.next()) {
                horario = new Horarios();

                horario.setId_horarios(rs.getInt("id_horarios"));
                horario.setDescripcion(rs.getString("descripcion"));
                horario.setHora_ingreso(rs.getString("hora_ingreso"));
                horario.setHora_salida(rs.getString("hora_salida"));
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar horario: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return horario;
    }

    public List<Horarios> consultarTodo() {
        List<Horarios> lista = new ArrayList<>();
        String sql = "SELECT * FROM Horarios";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Horarios horario = new Horarios();

                horario.setId_horarios(rs.getInt("id_horarios"));
                horario.setDescripcion(rs.getString("descripcion"));
                horario.setHora_ingreso(rs.getString("hora_ingreso"));
                horario.setHora_salida(rs.getString("hora_salida"));

                lista.add(horario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar horarios: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public List<Horarios> consultar() {
        return consultarTodo();
    }

    public boolean actualizar(Horarios horario) {
        String sql = "UPDATE Horarios SET descripcion = ?, hora_ingreso = ?, hora_salida = ? WHERE id_horarios = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);

            ps.setString(1, horario.getDescripcion());
            ps.setString(2, horario.getHora_ingreso());
            ps.setString(3, horario.getHora_salida());
            ps.setInt(4, horario.getId_horarios());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar horario: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_horarios) {
        String sql = "UPDATE Horarios SET activo = false, fecha_baja = NOW() WHERE id_horarios = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_horarios);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar horario: " + e.getMessage());
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
