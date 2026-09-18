package PruebaEliminar;

import Controlador.Programacion_PersonalDAO;
import java.util.Scanner;

public class PruebaEliminarProgramacion_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Programacion_PersonalDAO dao = new Programacion_PersonalDAO();

        System.out.println("=== ELIMINAR PROGRAMACION DE PERSONAL ===");

        System.out.print("Ingrese el ID de la programacion a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Programacion eliminada correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID de la programacion.");
        }

        leer.close();
    }
}