package PruebaListar;

import Modelo.Estado_Activo;
import Controlador.Estado_ActivoDAO;
import java.util.Scanner;

public class PruebaListarEstado_Activo {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_ActivoDAO dao = new Estado_ActivoDAO();

        System.out.println("=== BUSCAR ESTADO DE ACTIVO POR ID ===");

        System.out.print("Ingrese el ID del estado de activo a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Estado_Activo ea = dao.consultar(id);

        if (ea != null) {
            System.out.println("ID: " + ea.getIdEstado_Activo()
                    + " | Descripcion: " + ea.getDescripcion_activo());
        } else {
            System.out.println("No se encontro estado de activo con ID: " + id);
        }

        leer.close();
    }
}