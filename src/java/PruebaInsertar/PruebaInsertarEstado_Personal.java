package PruebaInsertar;

import Modelo.Estado_Personal;
import Controlador.Estado_PersonalDAO;
import java.util.Scanner;

public class PruebaInsertarEstado_Personal {

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        Estado_PersonalDAO dao = new Estado_PersonalDAO();
        Estado_Personal ep = new Estado_Personal();

        System.out.println("=== REGISTRO DE ESTADO DE PERSONAL ===");

        System.out.print("Descripción (ej: Activo, Retirado): ");
        ep.setDescripcion_estado(leer.nextLine());

        if (dao.insertar(ep)) {
            System.out.println("✅ Estado registrado con éxito.");
        } else {
            System.out.println("❌ Error al registrar. Verifique si el ID ya existe.");
        }
        leer.close();
    }
}
