package Controlador;

import Conexion.Conexion;
import Modelo.Roles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RolesDAO {

    private Conexion conectar = new Conexion();
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    private Scanner sc = new Scanner(System.in);

    public void menuRoles(int opcion) {
        Roles rol = new Roles();

        switch (opcion) {
            case 1:
                System.out.println("\n--- REGISTRAR NUEVO ROL ---");

                System.out.print("Descripcion: ");
                rol.setDescripcion_roles(sc.nextLine());

                System.out.print("Tipo de acceso (ADMINISTRADOR, PERSONAL_FIJO, TEMPORAL): ");
                rol.setTipo_acceso(sc.nextLine());

                if (insertar(rol)) {
                    System.out.println("Guardado con exito.");
                } else {
                    System.out.println("Error al guardar.");
                }
                break;

            case 2:
                System.out.print("\nIngrese el ID del rol a buscar: ");
                int idBuscar = sc.nextInt();
                sc.nextLine();

                Roles encontrado = consultar(idBuscar);

                if (encontrado != null) {
                    System.out.println("ID: " + encontrado.getIdRoles());
                    System.out.println("Descripcion: " + encontrado.getDescripcion_roles());
                    System.out.println("Tipo de acceso: " + encontrado.getTipo_acceso());
                } else {
                    System.out.println("No existe el rol con ID " + idBuscar);
                }
                break;

            case 3:
                System.out.println("\n--- LISTADO COMPLETO DE ROLES ---");
                List<Roles> lista = listar();

                if (lista.isEmpty()) {
                    System.out.println("No hay roles registrados");
                } else {
                    for (Roles r : lista) {
                        System.out.println("ID: " + r.getIdRoles());
                        System.out.println("Descripcion: " + r.getDescripcion_roles());
                        System.out.println("Tipo de acceso: " + r.getTipo_acceso());
                        System.out.println("------------------------------");
                    }
                }
                break;

            case 4:
                System.out.println("\n--- ACTUALIZAR ROL ---");
                System.out.print("ID del rol a modificar: ");
                rol.setIdRoles(sc.nextInt());
                sc.nextLine();

                System.out.print("Nueva descripcion: ");
                rol.setDescripcion_roles(sc.nextLine());

                System.out.print("Nuevo tipo de acceso (ADMINISTRADOR, PERSONAL_FIJO, TEMPORAL): ");
                rol.setTipo_acceso(sc.nextLine());

                if (actualizar(rol)) {
                    System.out.println("Actualizado con exito.");
                } else {
                    System.out.println("Error al actualizar.");
                }
                break;

            case 5:
                System.out.println("\n--- ELIMINAR ROL ---");
                System.out.print("ID del rol a borrar: ");
                int idEliminar = sc.nextInt();
                sc.nextLine();

                if (eliminar(idEliminar)) {
                    System.out.println("Eliminado con exito.");
                } else {
                    System.out.println("Error: no se pudo eliminar.");
                }
                break;

            default:
                System.out.println("Opcion no valida.");
                break;
        }
    }

    public boolean insertar(Roles r) {
        String sql = "INSERT INTO Roles (descripcion_roles, tipo_acceso) VALUES (?, ?)";

        try {
            String tipoAcceso = normalizarTipoAcceso(r.getTipo_acceso());
            if (tipoAcceso.isEmpty()) {
                return false;
            }

            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getDescripcion_roles());
            ps.setString(2, tipoAcceso);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar rol: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public Roles consultar(int id) {
        Roles rol = null;
        String sql = "SELECT * FROM Roles WHERE idroles = ?";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                rol = new Roles();
                rol.setIdRoles(rs.getInt("idroles"));
                rol.setDescripcion_roles(rs.getString("descripcion_roles"));
                rol.setTipo_acceso(rs.getString("tipo_acceso"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar rol: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return rol;
    }

    public List<Roles> listar() {
        List<Roles> lista = new ArrayList<>();
        String sql = "SELECT * FROM Roles";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Roles rol = new Roles();
                rol.setIdRoles(rs.getInt("idroles"));
                rol.setDescripcion_roles(rs.getString("descripcion_roles"));
                rol.setTipo_acceso(rs.getString("tipo_acceso"));
                lista.add(rol);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar roles: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }

        return lista;
    }

    public boolean actualizar(Roles r) {
        String sql = "UPDATE Roles SET descripcion_roles = ?, tipo_acceso = ? WHERE idroles = ?";

        try {
            String tipoAcceso = normalizarTipoAcceso(r.getTipo_acceso());
            if (tipoAcceso.isEmpty()) {
                return false;
            }

            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, r.getDescripcion_roles());
            ps.setString(2, tipoAcceso);
            ps.setInt(3, r.getIdRoles());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar rol: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    public boolean eliminar(int id) {
        String sql = "UPDATE Roles SET activo = false, fecha_baja = NOW() WHERE idroles = ? AND activo = true";

        try {
            con = conectar.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar rol: " + e.getMessage());
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

    private String normalizarTipoAcceso(String tipoAcceso) {
        if (tipoAcceso == null) {
            return "";
        }

        String valor = tipoAcceso.trim().toUpperCase();
        if ("ADMINISTRADOR".equals(valor)
                || "PERSONAL_FIJO".equals(valor)
                || "TEMPORAL".equals(valor)) {
            return valor;
        }

        return "";
    }
}
