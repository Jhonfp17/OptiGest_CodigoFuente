package PruebaEliminar;

import Controlador.Estado_PersonalDAO;
import java.util.Scanner;

public class PruebaEliminarEstado_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_PersonalDAO dao = new Estado_PersonalDAO();

        System.out.println("=== ELIMINAR ESTADO DE PERSONAL ===");

        System.out.print("Ingrese el ID del estado de personal a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Estado de personal eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del estado de personal.");
        }

        leer.close();
    }
}