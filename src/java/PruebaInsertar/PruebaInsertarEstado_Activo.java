package PruebaInsertar;

import Modelo.Estado_Activo;
import Controlador.Estado_ActivoDAO;
import java.util.Scanner;

public class PruebaInsertarEstado_Activo {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_ActivoDAO dao = new Estado_ActivoDAO();
        Estado_Activo estado = new Estado_Activo();

        System.out.println("=== REGISTRO DE ESTADO DE ACTIVO ===");

        System.out.print("Descripcion (ej: Disponible): ");
        estado.setDescripcion_activo(leer.nextLine());

        if (dao.insertar(estado)) {
            System.out.println("Estado de activo guardado correctamente.");
        } else {
            System.out.println("Error al registrar estado de activo.");
        }

        leer.close();
    }
}