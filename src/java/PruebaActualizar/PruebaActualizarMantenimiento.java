package PruebaActualizar;

import Modelo.Mantenimiento;
import Controlador.MantenimientoDAO;
import java.util.Scanner;

public class PruebaActualizarMantenimiento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        MantenimientoDAO dao = new MantenimientoDAO();
        Mantenimiento mante = new Mantenimiento();

        System.out.println("=== ACTUALIZAR MANTENIMIENTO ===");

        System.out.print("Ingrese el ID del mantenimiento a modificar: ");
        mante.setId_mantenimiento(Integer.parseInt(leer.nextLine()));

        System.out.print("Nueva fecha mantenimiento (YYYY-MM-DD): ");
        mante.setFecha_mante(leer.nextLine());

        System.out.print("Nuevo costo: ");
        mante.setCosto(leer.nextLine());

        System.out.print("Nueva descripcion: ");
        mante.setDescripcion(leer.nextLine());

        System.out.print("Nuevo ID Activo (Debe existir): ");
        mante.setActivos_id_activos(Integer.parseInt(leer.nextLine()));

        System.out.print("Nuevo ID Proveedor (Debe existir): ");
        mante.setProveedores_idProveedores(Integer.parseInt(leer.nextLine()));

        if (dao.actualizar(mante)) {
            System.out.println("Mantenimiento actualizado correctamente.");
        } else {
            System.out.println("Error al actualizar. Verifica el ID del mantenimiento.");
        }

        leer.close();
    }
}