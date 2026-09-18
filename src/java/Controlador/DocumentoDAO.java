package Controlador;

import Conexion.Conexion;
import Modelo.Documento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DocumentoDAO {

    private Conexion cn = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuDocumento(int opcion) {
        Documento doc = new Documento();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR DOCUMENTO ---");
                System.out.print("Descripcion documento: ");
                doc.setDescripcion_doc(sc.nextLine());

                if (insertar(doc)) {
                    System.out.println("Documento registrado con exito.");
                } else {
                    System.out.println("Error al registrar documento.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del documento a buscar: ");
                int idBuscar = Integer.parseInt(sc.nextLine());

                Documento encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getId_documento());
                    System.out.println("Descripcion: " + encontrado.getDescripcion_doc());
                } else {
                    System.out.println("No se encontro documento con ese ID.");
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE DOCUMENTOS ---");

                List<Documento> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay documentos registrados");
                } else {
                    for (Documento d : lista) {
                        System.out.println("ID: " + d.getId_documento());
                        System.out.println("Descripcion: " + d.getDescripcion_doc());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR DOCUMENTO ---");
                System.out.print("Ingrese el ID del documento a modificar: ");
                doc.setId_documento(Integer.parseInt(sc.nextLine()));

                System.out.print("Nueva descripcion documento: ");
                doc.setDescripcion_doc(sc.nextLine());

                if (actualizar(doc)) {
                    System.out.println("Documento actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar documento.");
                }
                break;

            case 5:
                System.out.print("\nIngrese el ID del documento a eliminar: ");
                int idEliminar = Integer.parseInt(sc.nextLine());

                if (eliminar(idEliminar)) {
                    System.out.println("Documento eliminado.");
                } else {
                    System.out.println("No se pudo eliminar documento.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Documento doc) {
        String sql = "INSERT INTO Documento (descripcion_doc) VALUES (?)";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, doc.getDescripcion_doc());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar documento: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Documento consultar(int id_documento) {
        Documento doc = null;
        String sql = "SELECT * FROM Documento WHERE id_documento = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_documento);
            rs = ps.executeQuery();

            if (rs.next()) {
                doc = new Documento();
                doc.setId_documento(rs.getInt("id_documento"));
                doc.setDescripcion_doc(rs.getString("descripcion_doc"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar documento: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return doc;
    }

    public List<Documento> listar() {
        List<Documento> lista = new ArrayList<>();
        String sql = "SELECT * FROM Documento";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Documento d = new Documento();
                d.setId_documento(rs.getInt("id_documento"));
                d.setDescripcion_doc(rs.getString("descripcion_doc"));
                lista.add(d);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar documentos: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Documento doc) {
        String sql = "UPDATE Documento SET descripcion_doc = ? WHERE id_documento = ?";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, doc.getDescripcion_doc());
            ps.setInt(2, doc.getId_documento());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar documento: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id_documento) {
        String sql = "UPDATE Documento SET activo = false, fecha_baja = NOW() WHERE id_documento = ? AND activo = true";

        try {
            con = cn.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_documento);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar documento: " + e.getMessage());
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
