package Controlador;

import Conexion.Conexion;
import Modelo.Proveedores;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProveedoresDAO {

    private Conexion conectar = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuProveedores(int opcion) {
        Proveedores prov = new Proveedores();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR NUEVO PROVEEDOR ---");
                System.out.print("Nombre: ");
                prov.setNombre(sc.nextLine());
                System.out.print("Telefono: ");
                prov.setTelefono(sc.nextLine());
                System.out.print("Direccion: ");
                prov.setDireccion(sc.nextLine());
                System.out.print("Email: ");
                prov.setEmail(sc.nextLine());

                if (insertar(prov)) {
                    System.out.println("Proveedor guardado con exito.");
                } else {
                    System.out.println("Error al guardar.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del proveedor a buscar: ");
                int idBuscar = sc.nextInt();
                sc.nextLine();

                Proveedores encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getIdProveedores());
                    System.out.println("Nombre: " + encontrado.getNombre());
                    System.out.println("Telefono: " + encontrado.getTelefono());
                    System.out.println("Direccion: " + encontrado.getDireccion());
                    System.out.println("Email: " + encontrado.getEmail());
                } else {
                    System.out.println("No existe proveedor con ID " + idBuscar);
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO DE PROVEEDORES ---");
                List<Proveedores> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay proveedores registrados");
                } else {
                    for (Proveedores p : lista) {
                        System.out.println("ID: " + p.getIdProveedores());
                        System.out.println("Nombre: " + p.getNombre());
                        System.out.println("Telefono: " + p.getTelefono());
                        System.out.println("Direccion: " + p.getDireccion());
                        System.out.println("Email: " + p.getEmail());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR PROVEEDOR ---");
                System.out.print("ID del proveedor a modificar: ");
                prov.setIdProveedores(sc.nextInt());
                sc.nextLine();

                System.out.print("Nuevo nombre: ");
                prov.setNombre(sc.nextLine());
                System.out.print("Nuevo telefono: ");
                prov.setTelefono(sc.nextLine());
                System.out.print("Nueva direccion: ");
                prov.setDireccion(sc.nextLine());
                System.out.print("Nuevo email: ");
                prov.setEmail(sc.nextLine());

                if (actualizar(prov)) {
                    System.out.println("Actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar.");
                }
                break;

            case 5:
                System.out.println("\n--- ELIMINAR PROVEEDOR ---");
                System.out.print("ID del proveedor a borrar: ");
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

    public boolean insertar(Proveedores p) {
        String sql = "INSERT INTO Proveedores (nombre, telefono, direccion, email) VALUES (?, ?, ?, ?)";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTelefono());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Proveedores consultar(int id) {
        Proveedores proveedor = null;
        String sql = "SELECT * FROM Proveedores WHERE idProveedores = ?";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                proveedor = new Proveedores();
                proveedor.setIdProveedores(rs.getInt("idProveedores"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setEmail(rs.getString("email"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar proveedor: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return proveedor;
    }

    public List<Proveedores> listar() {
        List<Proveedores> lista = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proveedores proveedor = new Proveedores();
                proveedor.setIdProveedores(rs.getInt("idProveedores"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setDireccion(rs.getString("direccion"));
                proveedor.setEmail(rs.getString("email"));
                lista.add(proveedor);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Proveedores p) {
        String sql = "UPDATE Proveedores SET nombre = ?, telefono = ?, direccion = ?, email = ? WHERE idProveedores = ?";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getTelefono());
            ps.setString(3, p.getDireccion());
            ps.setString(4, p.getEmail());
            ps.setInt(5, p.getIdProveedores());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar proveedor: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id) {
        String sql = "UPDATE Proveedores SET activo = false, fecha_baja = NOW() WHERE idProveedores = ? AND activo = true";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar proveedor: " + e.getMessage());
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
