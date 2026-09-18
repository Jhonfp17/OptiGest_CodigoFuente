package PruebaEliminar;

import Controlador.CategoriasDAO;
import java.util.Scanner;

public class PruebaEliminarCategorias {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        CategoriasDAO dao = new CategoriasDAO();

        System.out.println("=== ELIMINAR CATEGORIA ===");

        System.out.print("Ingrese el ID de la categoria a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Categoria eliminada correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID de la categoria.");
        }

        leer.close();
    }
}