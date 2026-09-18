package PruebaEliminar;

import Controlador.AsignacionesDAO;
import java.util.Scanner;

public class PruebaEliminarAsignaciones {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        AsignacionesDAO dao = new AsignacionesDAO();

        System.out.println("=== ELIMINAR ASIGNACION ===");

        System.out.print("Ingrese el ID de la asignacion a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Asignacion eliminada correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID de la asignacion.");
        }

        leer.close();
    }
}