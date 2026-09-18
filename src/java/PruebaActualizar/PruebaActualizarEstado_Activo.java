package PruebaActualizar;

import Modelo.Estado_Activo;
import Controlador.Estado_ActivoDAO;
import java.util.Scanner;

public class PruebaActualizarEstado_Activo {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_ActivoDAO dao = new Estado_ActivoDAO();
        Estado_Activo estado = new Estado_Activo();

        System.out.println("=== ACTUALIZAR ESTADO DE ACTIVO ===");

        System.out.print("Ingrese el ID del estado de activo a modificar: ");
        estado.setIdEstado_Activo(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del estado de activo: ");
        estado.setDescripcion_activo(leer.nextLine());

        if (dao.actualizar(estado)) {
            System.out.println("Estado de activo actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del estado de activo.");
        }

        leer.close();
    }
}