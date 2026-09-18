package PruebaEliminar;

import Controlador.HorariosDAO;
import java.util.Scanner;

public class PruebaEliminarHorarios {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        HorariosDAO dao = new HorariosDAO();

        System.out.println("=== ELIMINAR HORARIO ===");

        System.out.print("Ingrese el ID del horario a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Horario eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del horario.");
        }

        leer.close();
    }
}