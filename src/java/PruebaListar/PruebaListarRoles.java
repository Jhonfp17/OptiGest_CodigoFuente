package PruebaListar;

import Modelo.Roles;
import Controlador.RolesDAO;
import java.util.Scanner;

public class PruebaListarRoles {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        RolesDAO dao = new RolesDAO();

        System.out.println("=== BUSCAR ROL POR ID ===");

        System.out.print("Ingrese el ID del rol a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Roles r = dao.consultar(id);

        if (r != null) {
            System.out.println("ID: " + r.getIdRoles()
                    + " | Descripcion: " + r.getDescripcion_roles()
                    + " | Tipo acceso: " + r.getTipo_acceso());
        } else {
            System.out.println("No se encontro rol con ID: " + id);
        }

        leer.close();
    }
}