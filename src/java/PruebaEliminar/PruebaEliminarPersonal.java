package PruebaEliminar;

import Controlador.PersonalDAO;
import java.util.Scanner;

public class PruebaEliminarPersonal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        PersonalDAO dao = new PersonalDAO();

        System.out.println("=== ELIMINAR PERSONAL ===");

        System.out.print("Ingrese la identificacion del personal a eliminar: ");
        String identificacion = leer.nextLine();

        if (dao.eliminar(identificacion)) {
            System.out.println("Personal eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica la identificacion del personal.");
        }

        leer.close();
    }
}