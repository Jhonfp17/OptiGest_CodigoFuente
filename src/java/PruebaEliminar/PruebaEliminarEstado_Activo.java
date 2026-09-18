package PruebaEliminar;

import Controlador.Estado_ActivoDAO;
import java.util.Scanner;

public class PruebaEliminarEstado_Activo {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_ActivoDAO dao = new Estado_ActivoDAO();

        System.out.println("=== ELIMINAR ESTADO DE ACTIVO ===");

        System.out.print("Ingrese el ID del estado de activo a eliminar: ");
        int id = Integer.parseInt(leer.nextLine());

        if (dao.eliminar(id)) {
            System.out.println("Estado de activo eliminado correctamente.");
        } else {
            System.out.println("Error al eliminar. Verifica el ID del estado de activo.");
        }

        leer.close();
    }
}