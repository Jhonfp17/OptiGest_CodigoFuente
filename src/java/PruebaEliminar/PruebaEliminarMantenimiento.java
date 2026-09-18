package PruebaEliminar;

import Controlador.MantenimientoDAO;
import java.util.Scanner;

public class PruebaEliminarMantenimiento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        MantenimientoDAO dao = new MantenimientoDAO();

        System.out.println("=== ELIMINAR MANTENIMIENTO ===");

        System.out.print("Ingrese el ID del mantenimiento a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Mantenimiento eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del mantenimiento.");
        }

        leer.close();
    }
}