package PruebaListar;

import Modelo.Mantenimiento;
import Controlador.MantenimientoDAO;
import java.util.Scanner;

public class PruebaListarMantenimiento {

    public static void main(String[] args) {

        Scanner leer = new Scanner(System.in);
        MantenimientoDAO dao = new MantenimientoDAO();

        System.out.println("=== BUSCAR MANTENIMIENTO POR ID ===");

        System.out.print("Ingrese el ID del mantenimiento a buscar: ");
        int id = Integer.parseInt(leer.nextLine());

        Mantenimiento m = dao.consultarMantenimiento(id);

        if (m != null) {
            System.out.println("ID: " + m.getId_mantenimiento()
                    + " | Fecha: " + m.getFecha_mante()
                    + " | Costo: " + m.getCosto()
                    + " | Descripcion: " + m.getDescripcion()
                    + " | ID Activo: " + m.getActivos_id_activos()
                    + " | ID Proveedor: " + m.getProveedores_idProveedores());
        } else {
            System.out.println("No se encontro mantenimiento con ID: " + id);
        }

        leer.close();
    }
}