package PruebaEliminar;

import Controlador.RolesDAO;
import java.util.Scanner;

public class PruebaEliminarRoles {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        RolesDAO dao = new RolesDAO();

        System.out.println("=== ELIMINAR ROL ===");

        System.out.print("Ingrese el ID del rol a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Rol eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del rol.");
        }

        leer.close();
    }
}