package PruebaEliminar;

import Controlador.ActivosDAO;
import java.util.Scanner;

public class PruebaEliminarActivos {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ActivosDAO dao = new ActivosDAO();

        System.out.println("=== ELIMINAR ACTIVO ===");

        System.out.print("Ingrese el ID del activo a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Activo eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del activo.");
        }

        leer.close();
    }
}