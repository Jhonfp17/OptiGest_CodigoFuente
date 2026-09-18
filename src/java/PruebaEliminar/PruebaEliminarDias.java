package PruebaEliminar;

import Controlador.DiasDAO;
import java.util.Scanner;

public class PruebaEliminarDias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DiasDAO dao = new DiasDAO();

        System.out.println("=== ELIMINAR DIA ===");

        System.out.print("Ingrese el ID del dia a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Dia eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del dia.");
        }

        leer.close();
    }
}