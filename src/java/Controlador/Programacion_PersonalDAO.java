package Controlador;

import Conexion.Conexion;
import Modelo.Programacion_Personal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Programacion_PersonalDAO {

    private Conexion conectar = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuProgramacion(int opcion) {
        Programacion_Personal prog = new Programacion_Personal();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR PROGRAMACION DE PERSONAL ---");
                System.out.print("Descripcion: ");
                prog.setDescripcion_programacion(sc.nextLine());
                System.out.print("Fecha desde (YYYY-MM-DD): ");
                prog.setFecha_desde(sc.nextLine());
                System.out.print("Fecha hasta (YYYY-MM-DD): ");
                prog.setFecha_hasta(sc.nextLine());
                System.out.print("ID Dia: ");
                prog.setDias_idDias(sc.nextInt());
                System.out.print("ID Personal: ");
                prog.setPersonal_id_personal(sc.nextInt());
                System.out.print("ID Horario: ");
                prog.setHorarios_id_horarios(sc.nextInt());
                sc.nextLine();

                if (insertar(prog)) {
                    System.out.println("Programacion guardada con exito.");
                } else {
                    System.out.println("Error al guardar.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID de programacion a buscar: ");
                int idBuscar = sc.nextInt();
                sc.nextLine();

                Programacion_Personal encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getIdProgramacion_Personal());
                    System.out.println("Descripcion: " + encontrado.getDescripcion_programacion());
                    System.out.println("Fecha desde: " + encontrado.getFecha_desde());
                    System.out.println("Fecha hasta: " + encontrado.getFecha_hasta());
                    System.out.println("ID Dia: " + encontrado.getDias_idDias());
                    System.out.println("ID Personal: " + encontrado.getPersonal_id_personal());
                    System.out.println("ID Horario: " + encontrado.getHorarios_id_horarios());
                } else {
                    System.out.println("No existe programacion con ID " + idBuscar);
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE PROGRAMACION PERSONAL ---");
                List<Programacion_Personal> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay programaciones registradas");
                } else {
                    for (Programacion_Personal p : lista) {
                        System.out.println("ID: " + p.getIdProgramacion_Personal());
                        System.out.println("Descripcion: " + p.getDescripcion_programacion());
                        System.out.println("Fecha desde: " + p.getFecha_desde());
                        System.out.println("Fecha hasta: " + p.getFecha_hasta());
                        System.out.println("ID Dia: " + p.getDias_idDias());
                        System.out.println("ID Personal: " + p.getPersonal_id_personal());
                        System.out.println("ID Horario: " + p.getHorarios_id_horarios());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR PROGRAMACION ---");
                System.out.print("ID de programacion a modificar: ");
                prog.setIdProgramacion_Personal(sc.nextInt());
                sc.nextLine();

                System.out.print("Nueva descripcion: ");
                prog.setDescripcion_programacion(sc.nextLine());
                System.out.print("Nueva fecha desde (YYYY-MM-DD): ");
                prog.setFecha_desde(sc.nextLine());
                System.out.print("Nueva fecha hasta (YYYY-MM-DD): ");
                prog.setFecha_hasta(sc.nextLine());
                System.out.print("Nuevo ID Dia: ");
                prog.setDias_idDias(sc.nextInt());
                System.out.print("Nuevo ID Personal: ");
                prog.setPersonal_id_personal(sc.nextInt());
                System.out.print("Nuevo ID Horario: ");
                prog.setHorarios_id_horarios(sc.nextInt());
                sc.nextLine();

                if (actualizar(prog)) {
                    System.out.println("Actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar.");
                }
                break;

            case 5:
                System.out.println("\n--- ELIMINAR PROGRAMACION ---");
                System.out.print("ID de programacion a borrar: ");
                int idEliminar = sc.nextInt();
                sc.nextLine();

                if (eliminar(idEliminar)) {
                    System.out.println("Eliminado con exito.");
                } else {
                    System.out.println("Error al eliminar.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Programacion_Personal p) {
        String sql = "INSERT INTO Programacion_Personal (descripcion_programacion, fecha_desde, fecha_hasta, Dias_idDias, Personal_id_personal, Horarios_id_horarios) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getDescripcion_programacion());
            ps.setString(2, p.getFecha_desde());
            ps.setString(3, p.getFecha_hasta());
            ps.setInt(4, p.getDias_idDias());
            ps.setInt(5, p.getPersonal_id_personal());
            ps.setInt(6, p.getHorarios_id_horarios());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar programacion: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Programacion_Personal consultar(int id) {
        Programacion_Personal prog = null;
        String sql = "SELECT * FROM Programacion_Personal WHERE idProgramacion_Personal = ?";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                prog = new Programacion_Personal();
                prog.setIdProgramacion_Personal(rs.getInt("idProgramacion_Personal"));
                prog.setDescripcion_programacion(rs.getString("descripcion_programacion"));
                prog.setFecha_desde(rs.getString("fecha_desde"));
                prog.setFecha_hasta(rs.getString("fecha_hasta"));
                prog.setDias_idDias(rs.getInt("Dias_idDias"));
                prog.setPersonal_id_personal(rs.getInt("Personal_id_personal"));
                prog.setHorarios_id_horarios(rs.getInt("Horarios_id_horarios"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar programacion: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return prog;
    }

    public List<Programacion_Personal> listar() {
        List<Programacion_Personal> lista = new ArrayList<>();
        String sql = "SELECT * FROM Programacion_Personal";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Programacion_Personal prog = new Programacion_Personal();
                prog.setIdProgramacion_Personal(rs.getInt("idProgramacion_Personal"));
                prog.setDescripcion_programacion(rs.getString("descripcion_programacion"));
                prog.setFecha_desde(rs.getString("fecha_desde"));
                prog.setFecha_hasta(rs.getString("fecha_hasta"));
                prog.setDias_idDias(rs.getInt("Dias_idDias"));
                prog.setPersonal_id_personal(rs.getInt("Personal_id_personal"));
                prog.setHorarios_id_horarios(rs.getInt("Horarios_id_horarios"));
                lista.add(prog);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar programaciones: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Programacion_Personal p) {
        // Solo se modifica una programacion futura. Una vez vigente se conserva como evidencia.
        String sql = "UPDATE Programacion_Personal SET descripcion_programacion = ?, fecha_desde = ?, fecha_hasta = ?, Dias_idDias = ?, Personal_id_personal = ?, Horarios_id_horarios = ? "
                + "WHERE idProgramacion_Personal = ? AND activo = true AND fecha_desde > CURDATE()";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getDescripcion_programacion());
            ps.setString(2, p.getFecha_desde());
            ps.setString(3, p.getFecha_hasta());
            ps.setInt(4, p.getDias_idDias());
            ps.setInt(5, p.getPersonal_id_personal());
            ps.setInt(6, p.getHorarios_id_horarios());
            ps.setInt(7, p.getIdProgramacion_Personal());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar programacion: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id) {
        // La cancelacion solo procede antes de que la programacion entre en vigencia.
        String sql = "UPDATE Programacion_Personal SET activo = false, fecha_baja = NOW() "
                + "WHERE idProgramacion_Personal = ? AND activo = true AND fecha_desde > CURDATE()";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar programacion: " + e.getMessage());
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
