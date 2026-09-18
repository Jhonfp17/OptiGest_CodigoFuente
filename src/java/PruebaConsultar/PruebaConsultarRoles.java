package PruebaConsultar;

import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.List;

public class PruebaConsultarRoles {

    public static void main(String[] args) {

        RolesDAO dao = new RolesDAO();

        System.out.println("=== LISTADO DE ROLES ===");

        List<Roles> lista = dao.listar();

        if (lista.isEmpty()) {
            System.out.println("No hay roles registrados.");
        } else {
            for (Roles r : lista) {
                System.out.println("ID: " + r.getIdRoles()
                        + " | Descripcion: " + r.getDescripcion_roles()
                        + " | Tipo acceso: " + r.getTipo_acceso());
            }
        }
    }
}