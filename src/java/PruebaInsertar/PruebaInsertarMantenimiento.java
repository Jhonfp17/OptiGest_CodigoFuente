package PruebaInsertar;

import Modelo.Mantenimiento;
import Controlador.MantenimientoDAO;
import java.util.Scanner;

public class PruebaInsertarMantenimiento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        MantenimientoDAO dao = new MantenimientoDAO();
        Mantenimiento mante = new Mantenimiento();

        System.out.println("=== REGISTRO DE MANTENIMIENTO ===");

        System.out.print("Fecha mantenimiento (YYYY-MM-DD): ");
        mante.setFecha_mante(leer.nextLine());

        System.out.print("Costo: ");
        mante.setCosto(leer.nextLine());

        System.out.print("Descripcion: ");
        mante.setDescripcion(leer.nextLine());

        System.out.print("ID Activo (Debe existir): ");
        mante.setActivos_id_activos(Integer.parseInt(leer.nextLine()));

        System.out.print("ID Proveedor (Debe existir): ");
        mante.setProveedores_idProveedores(Integer.parseInt(leer.nextLine()));

        if (dao.insertar(mante)) {
            System.out.println("Mantenimiento guardado correctamente.");
        } else {
            System.out.println("Error: Verifica los IDs de Activo y Proveedor.");
        }

        leer.close();
    }
}