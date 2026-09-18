package Controlador;

import Conexion.Conexion;
import Modelo.Dias;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiasDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuDias(int opcion) {
        Dias dia = new Dias();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR DIA ---");
                System.out.print("Descripcion dia: ");
                dia.setDescripcionDias(sc.nextLine());

                if (insertar(dia)) {
                    System.out.println("Dia registrado con exito.");
                } else {
                    System.out.println("Error al registrar dia.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del dia a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Dias encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getIdDias());
                    System.out.println("Descripcion: " + encontrado.getDescripcionDias());
                } else {
                    System.out.println("No se encontro dia con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE DIAS ---");

                List<Dias> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay dias registrados");
                } else {
                    for (Dias d : lista) {
                        System.out.println("ID: " + d.getIdDias());
                        System.out.println("Descripcion: " + d.getDescripcionDias());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR DIA ---");
                System.out.print("Ingrese el ID del dia a modificar: ");
                dia.setIdDias(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion dia: ");
                dia.setDescripcionDias(sc.nextLine());

                if (actualizar(dia)) {
                    System.out.println("Dia actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar dia.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del dia a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Dia eliminado.");
                } else {
                    System.out.println("No se pudo eliminar dia.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Dias dia) {
        String sql = "INSERT INTO Dias (descripcionDias) VALUES (?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, dia.getDescripcionDias());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar dia: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Dias consultar(int idDias) {
        Dias dia = null;
        String sql = "SELECT * FROM Dias WHERE idDias = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDias);
            rs = ps.executeQuery();

            if (rs.next()) {
                dia = new Dias();
                dia.setIdDias(rs.getInt("idDias"));
                dia.setDescripcionDias(rs.getString("descripcionDias"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar dia: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return dia;
    }

    public List<Dias> listar() {
        List<Dias> lista = new ArrayList<>();
        String sql = "SELECT * FROM Dias ORDER BY idDias";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Dias dia = new Dias();
                dia.setIdDias(rs.getInt("idDias"));
                dia.setDescripcionDias(rs.getString("descripcionDias"));
                lista.add(dia);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar dias: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Dias dia) {
        String sql = "UPDATE Dias SET descripcionDias = ? WHERE idDias = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, dia.getDescripcionDias());
            ps.setInt(2, dia.getIdDias());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar dia: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int idDias) {
        String sql = "UPDATE Dias SET activo = false, fecha_baja = NOW() WHERE idDias = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idDias);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar dia: " + e.getMessage());
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
}
