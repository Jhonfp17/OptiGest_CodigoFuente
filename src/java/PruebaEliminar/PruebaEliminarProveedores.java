package PruebaEliminar;

import Controlador.ProveedoresDAO;
import java.util.Scanner;

public class PruebaEliminarProveedores {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        ProveedoresDAO dao = new ProveedoresDAO();

        System.out.println("=== ELIMINAR PROVEEDOR ===");

        System.out.print("Ingrese el ID del proveedor a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Proveedor eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del proveedor.");
        }

        leer.close();
    }
}