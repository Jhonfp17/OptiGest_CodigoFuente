package PruebaActualizar;

import Modelo.Estado_Personal;
import Controlador.Estado_PersonalDAO;
import java.util.Scanner;

public class PruebaActualizarEstado_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_PersonalDAO dao = new Estado_PersonalDAO();
        Estado_Personal estado = new Estado_Personal();

        System.out.println("=== ACTUALIZAR ESTADO DE PERSONAL ===");

        System.out.print("Ingrese el ID del estado de personal a modificar: ");
        estado.setId_estado(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva descripcion del estado de personal: ");
        estado.setDescripcion_estado(leer.nextLine());

        if (dao.actualizar(estado)) {
            System.out.println("Estado de personal actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del estado de personal.");
        }

        leer.close();
    }
}