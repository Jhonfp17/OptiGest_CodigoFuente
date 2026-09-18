package Controlador;

import Conexion.Conexion;
import Modelo.Categorias;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoriasDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuCategorias(int opcion) {
        Categorias cat = new Categorias();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR CATEGORIA ---");
                System.out.print("Descripcion categoria: ");
                cat.setDescripcionCategoria(sc.nextLine());

                if (insertar(cat)) {
                    System.out.println("Categoria registrada con exito.");
                } else {
                    System.out.println("Error al registrar categoria.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID de la categoria a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Categorias encontrada = consultar(idBuscar);

                if (encontrada != null) {
                    System.out.println("ID: " + encontrada.getIdCategorias());
                    System.out.println("Descripcion: " + encontrada.getDescripcionCategoria());
                } else {
                    System.out.println("No se encontro categoria con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE CATEGORIAS ---");

                List<Categorias> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay categorias registradas");
                } else {
                    for (Categorias c : lista) {
                        System.out.println("ID: " + c.getIdCategorias());
                        System.out.println("Descripcion: " + c.getDescripcionCategoria());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR CATEGORIA ---");
                System.out.print("Ingrese el ID de la categoria a modificar: ");
                cat.setIdCategorias(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion categoria: ");
                cat.setDescripcionCategoria(sc.nextLine());

                if (actualizar(cat)) {
                    System.out.println("Categoria actualizada con exito.");
                } else {
                    System.out.println("Error al actualizar categoria.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID de la categoria a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Categoria eliminada.");
                } else {
                    System.out.println("No se pudo eliminar categoria.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Categorias cat) {
        String sql = "INSERT INTO Categorias (descripcionCategoria) VALUES (?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getDescripcionCategoria());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar categoria: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Categorias consultar(int idCategorias) {
        Categorias cat = null;
        String sql = "SELECT * FROM Categorias WHERE idCategorias = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCategorias);
            rs = ps.executeQuery();

            if (rs.next()) {
                cat = new Categorias();
                cat.setIdCategorias(rs.getInt("idCategorias"));
                cat.setDescripcionCategoria(rs.getString("descripcionCategoria"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar categoria: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return cat;
    }

    public List<Categorias> listar() {
        List<Categorias> lista = new ArrayList<>();
        String sql = "SELECT * FROM Categorias";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Categorias cat = new Categorias();
                cat.setIdCategorias(rs.getInt("idCategorias"));
                cat.setDescripcionCategoria(rs.getString("descripcionCategoria"));
                lista.add(cat);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar categorias: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Categorias cat) {
        String sql = "UPDATE Categorias SET descripcionCategoria = ? WHERE idCategorias = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, cat.getDescripcionCategoria());
            ps.setInt(2, cat.getIdCategorias());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar categoria: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int idCategorias) {
        String sql = "UPDATE Categorias SET activo = false, fecha_baja = NOW() WHERE idCategorias = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idCategorias);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar categoria: " + e.getMessage());
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
