package PruebaEliminar;

import Controlador.DocumentoDAO;
import java.util.Scanner;

public class PruebaEliminarDocumento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        DocumentoDAO dao = new DocumentoDAO();

        System.out.println("=== ELIMINAR DOCUMENTO ===");

        System.out.print("Ingrese el ID del documento a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Documento eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del documento.");
        }

        leer.close();
    }
}