package PruebaListar;

import Modelo.Estado_Personal;
import Controlador.Estado_PersonalDAO;
import java.util.Scanner;

public class PruebaListarEstado_Personal {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        Estado_PersonalDAO dao = new Estado_PersonalDAO();

        System.out.println("=== BUSCAR ESTADO DE PERSONAL POR ID ===");

        System.out.print("Ingrese el ID del estado de personal a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Estado_Personal ep = dao.consultar(id);

        if (ep != null) {
            System.out.println("ID: " + ep.getId_estado()
                    + " | Descripcion: " + ep.getDescripcion_estado());
        } else {
            System.out.println("No se encontro estado de personal con ID: " + id);
        }

        leer.close();
    }
}